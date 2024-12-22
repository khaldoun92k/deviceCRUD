package com.crud.device.controller;

import com.crud.device.dto.DeviceDTO;
import com.crud.device.service.impl.DeviceServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DeviceControllerTest {

    @Autowired
    private MockMvc mvc;
    // Using @MockitoBean instead of @MockBean since @MockBean is marked for removal in 3.4.0
    @MockitoBean
    private DeviceServiceImpl deviceService;

    private AutoCloseable mocks;

    @BeforeEach
    public void setup() {
        mocks = MockitoAnnotations.openMocks(this);

        DeviceDTO device1 = new DeviceDTO(1L, "Smartphone", "Apple", null);
        DeviceDTO device2 = new DeviceDTO(2L, "TV", "Samsung", null);

        given(deviceService.getAllDevices()).willReturn(Arrays.asList(device1, device2));
        given(deviceService.getDeviceById(1L)).willReturn(Optional.of(device1));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @WithMockUser(username = "MockUser")
    void getAllDevicesTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/devices")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", hasItems(1, 2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Smartphone"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand").value("Apple"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("TV"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].brand").value("Samsung"));
    }

    @Test
    @WithMockUser(username = "MockUser")
    void saveDeviceTest() throws Exception {
        DeviceDTO newDevice = new DeviceDTO(null, "NewDevice", "NewBrand", null);
        DeviceDTO savedDevice = new DeviceDTO(3L, "NewDevice", "NewBrand", null);

        given(deviceService.saveDevice(any(DeviceDTO.class))).willReturn(savedDevice);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/devices")
                        .content(asJsonString(newDevice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("NewDevice"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand").value("NewBrand"));
    }

    @Test
    @WithMockUser(username = "MockUser")
    void getDeviceByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/devices/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Smartphone"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand").value("Apple"));
    }

    @Test
    @WithMockUser(username = "MockUser")
    void updateDeviceTest() throws Exception {
        DeviceDTO updatedDevice = new DeviceDTO(1L, "UpdatedDevice", "UpdatedBrand", null);

        given(deviceService.updateDevice(any(Long.class), any(DeviceDTO.class))).willReturn(updatedDevice);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/devices/{id}", 1L)
                        .content(asJsonString(updatedDevice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("UpdatedDevice"));
    }

    @Test
    @WithMockUser(username = "MockUser")
    void partialUpdateDeviceTest() throws Exception{
        DeviceDTO updatedDevice = new DeviceDTO(1L, "Mac", "Apple", null);

        Map<String, String> updates = new HashMap<>();
        updates.put("name", "Mac");

        given(deviceService.partialUpdateDevice(any(Long.class), anyMap()))
                .willReturn(updatedDevice);

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/devices/{id}", 1L)
                        .content(asJsonString(updates))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mac"));
    }

    @Test
    @WithMockUser(username = "MockUser")
    void searchDeviceByBrandTest() throws Exception {
        DeviceDTO deviceWithBrand= new DeviceDTO(2L, "TV", "Samsung", null);

        given(deviceService.searchDeviceByBrand("Samsung")).willReturn(List.of(deviceWithBrand));

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/devices/search")
                        .param("brand", "Samsung")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TV"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand").value("Samsung"));
    }


    @Test
    @WithMockUser(username = "MockUser")
    void deleteDeviceTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/devices/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
