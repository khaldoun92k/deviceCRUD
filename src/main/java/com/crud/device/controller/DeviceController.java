package com.crud.device.controller;

import com.crud.device.dto.DeviceDTO;
import com.crud.device.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // Endpoint to save a new device
    @PostMapping
    public ResponseEntity<DeviceDTO> saveDevice(@RequestBody DeviceDTO deviceDTO) {
        DeviceDTO savedDevice = deviceService.saveDevice(deviceDTO);
        return new ResponseEntity<>(savedDevice, HttpStatus.CREATED);
    }

    // Endpoint to get a device by ID
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getDeviceById(@PathVariable Long id) {
        Optional<DeviceDTO> device = deviceService.getDeviceById(id);
        return device.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to get all devices
    @GetMapping
    public List<DeviceDTO> getAllDevices() {
        return deviceService.getAllDevices();
    }

    // Endpoint to update a device completely
    @PutMapping("/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable Long id, @RequestBody DeviceDTO updatedDeviceDTO) {
        DeviceDTO updatedDevice = deviceService.updateDevice(id, updatedDeviceDTO);
        return ResponseEntity.ok(updatedDevice);
    }

    // Endpoint for partial update of a device
    @PatchMapping("/{id}")
    public ResponseEntity<DeviceDTO> partialUpdateDevice(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        DeviceDTO updatedDevice = deviceService.partialUpdateDevice(id, updates);
        return ResponseEntity.ok(updatedDevice);
    }

    // Endpoint to delete a device
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to search devices by brand
    @GetMapping("/search")
    public List<DeviceDTO> searchDeviceByBrand(@RequestParam String brand) {
        return deviceService.searchDeviceByBrand(brand);
    }
}
