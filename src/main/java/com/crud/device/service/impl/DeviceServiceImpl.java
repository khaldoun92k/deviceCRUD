package com.crud.device.service.impl;

import com.crud.device.dto.DeviceDTO;
import com.crud.device.model.Device;
import com.crud.device.repository.DeviceRepository;
import com.crud.device.service.DeviceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {
    private static final String DEVICE_NOT_FOUND_MSG = "Device not found with ID: ";

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public DeviceDTO saveDevice(DeviceDTO deviceDTO) {
        Device device = convertToEntity(deviceDTO);
        Device savedDevice = deviceRepository.save(device);
        return convertToDTO(savedDevice);
    }

    @Override
    public Optional<DeviceDTO> getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public List<DeviceDTO> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream().map(this::convertToDTO).toList();
    }

    @Override
    public DeviceDTO updateDevice(Long id, DeviceDTO updatedDeviceDTO) {
        Device existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(DEVICE_NOT_FOUND_MSG + id));

        existingDevice.setName(updatedDeviceDTO.name());
        existingDevice.setBrand(updatedDeviceDTO.brand());

        Device updatedDevice = deviceRepository.save(existingDevice);
        return convertToDTO(updatedDevice);
    }

    @Override
    public DeviceDTO partialUpdateDevice(Long id, Map<String, Object> updates) {
        Device existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(DEVICE_NOT_FOUND_MSG + id));

        updates.forEach((fieldName, fieldValue) -> {
            switch (fieldName) {
                case "name":
                    existingDevice.setName((String) fieldValue);
                    break;
                case "brand":
                    existingDevice.setBrand((String) fieldValue);
                    break;
                default:
            }
        });

        Device updatedDevice = deviceRepository.save(existingDevice);
        return convertToDTO(updatedDevice);
    }

    @Override
    public void deleteDevice(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new EntityNotFoundException(DEVICE_NOT_FOUND_MSG + id);
        }
        deviceRepository.deleteById(id);
    }

    @Override
    public List<DeviceDTO> searchDeviceByBrand(String brand) {
        List<Device> devices = deviceRepository.findByBrandContainingIgnoreCase(brand);
        return devices.stream().map(this::convertToDTO).toList();
    }

    private DeviceDTO convertToDTO(Device device) {
        return new DeviceDTO(
                device.getId(),
                device.getName(),
                device.getBrand(),
                device.getCreationTime()
        );
    }

    private Device convertToEntity(DeviceDTO deviceDTO) {
        return new Device(
                deviceDTO.name(),
                deviceDTO.brand()
        );
    }
}
