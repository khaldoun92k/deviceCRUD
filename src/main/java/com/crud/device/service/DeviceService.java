package com.crud.device.service;

import com.crud.device.dto.DeviceDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DeviceService {

    DeviceDTO saveDevice(DeviceDTO device);

    Optional<DeviceDTO> getDeviceById(Long id);

    List<DeviceDTO> getAllDevices();

    DeviceDTO updateDevice(Long id, DeviceDTO updatedDevice);

    DeviceDTO partialUpdateDevice(Long id, Map<String, Object> updates);

    void deleteDevice(Long id);

    List<DeviceDTO> searchDeviceByBrand(String brand);
}
