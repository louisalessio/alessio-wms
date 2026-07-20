package com.alessio.wms.movement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import com.alessio.wms.BaseIntegrationTest;
import com.alessio.wms.material.entity.Material;
import com.alessio.wms.material.repository.MaterialRepository;
import com.alessio.wms.movement.dto.MovementRequest;

@SpringBootTest
@AutoConfigureMockMvc
class MovementControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MaterialRepository materialRepository;

    @Test
    void shouldLoadMaterialSuccessfully() throws Exception {
        createTestMaterial();

        MovementRequest request = new MovementRequest("WIRE-001", 10, "Louis", null);

        mockMvc.perform(post("/api/movements/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    Material createTestMaterial() {
        Material material = new Material();
        material.setCode("WIRE-001");
        material.setDescription("copper wire");
        material.setActive(true);
        return materialRepository.save(material);
    }
}
