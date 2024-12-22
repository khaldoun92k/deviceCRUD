package com.crud.device.service;

import com.crud.device.dto.DeviceDTO;
import com.crud.device.model.Device;
import com.crud.device.repository.DeviceRepository;
import com.crud.device.service.impl.DeviceServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceServiceImpl deviceService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void saveDevice_ShouldSaveAndReturnDevice() {
        DeviceDTO deviceDTO = new DeviceDTO(1L, "Phone", "Apple", null);
        Device savedDevice = new Device(1L, "Phone", "Apple", null);


        when(deviceRepository.save(any(Device.class))).thenReturn(savedDevice);

        DeviceDTO result = deviceService.saveDevice(deviceDTO);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Phone", result.name());
        assertEquals("Apple", result.brand());
    }

    @Test
    void getDeviceById_ShouldReturnDevice_WhenFound() {
        Device device = new Device(1L, "Phone", "Apple", null);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        Optional<DeviceDTO> result = deviceService.getDeviceById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().id());
        assertEquals("Phone", result.get().name());
        assertEquals("Apple", result.get().brand());
    }

    @Test
    void getDeviceById_ShouldReturnEmpty_WhenNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<DeviceDTO> result = deviceService.getDeviceById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllDevices_ShouldReturnDeviceList() {
        List<Device> devices = Arrays.asList(
                new Device("Phone", "Apple"),
                new Device("Laptop", "Dell")
        );
        when(deviceRepository.findAll()).thenReturn(devices);

        List<DeviceDTO> result = deviceService.getAllDevices();

        assertEquals(2, result.size());
        assertEquals("Phone", result.get(0).name());
        assertEquals("Apple", result.get(0).brand());
        assertEquals("Laptop", result.get(1).name());
        assertEquals("Dell", result.get(1).brand());
    }

    @Test
    void updateDevice_ShouldUpdateAndReturnDevice() {
        Device existingDevice = new Device("Phone", "Apple");
        Device updatedDevice = new Device("Tablet", "Samsung");
        DeviceDTO updatedDeviceDTO = new DeviceDTO(1L, "Tablet", "Samsung", null);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existingDevice));
        when(deviceRepository.save(any(Device.class))).thenReturn(updatedDevice);

        DeviceDTO result = deviceService.updateDevice(1L, updatedDeviceDTO);

        assertNotNull(result);
        assertEquals("Tablet", result.name());
        assertEquals("Samsung", result.brand());
    }

    @Test
    void partialUpdateDevice_ShouldUpdateFieldsAndReturnDevice() {
        Device existingDevice = new Device("Phone", "Apple");
        Device updatedDevice = new Device("Mac", "Apple");
        Map<String, Object> updates = Map.of("name", "Mac");

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existingDevice));
        when(deviceRepository.save(any(Device.class))).thenReturn(updatedDevice);

        DeviceDTO result = deviceService.partialUpdateDevice(1L, updates);

        assertNotNull(result);
        assertEquals("Mac", result.name());
        assertEquals("Apple", result.brand());
    }

    @Test
    void deleteDevice_ShouldDelete_WhenDeviceExists() {
        when(deviceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(deviceRepository).deleteById(1L);

        assertDoesNotThrow(() -> deviceService.deleteDevice(1L));
        verify(deviceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDevice_ShouldThrowException_WhenDeviceDoesNotExist() {
        when(deviceRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> deviceService.deleteDevice(1L));

        assertEquals("Device not found with ID: 1", exception.getMessage());
    }

    @Test
    void searchDeviceByBrand_ShouldReturnMatchingDevices() {
        List<Device> devices = List.of(
                new Device(1L, "Phone", "Apple", null),
                new Device(2L, "TV", "Samsung", null),
                new Device(3L, "Watch", "Samsung", null)
        );

        when(deviceRepository.findByBrandContainingIgnoreCase("Samsung")).thenReturn(devices.stream().filter(x->x.getBrand().equals("Samsung")).toList());

        List<DeviceDTO> result = deviceService.searchDeviceByBrand("Samsung");

        assertEquals(2, result.size());
        assertEquals(2L, result.getFirst().id());
        assertEquals("TV", result.getFirst().name());
        assertEquals("Samsung", result.getFirst().brand());
        assertEquals(3L, result.getLast().id());
        assertEquals("Watch", result.getLast().name());
        assertEquals("Samsung", result.getLast().brand());
    }
}
