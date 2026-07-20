package com.alessio.wms.movement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.alessio.wms.BaseIntegrationTest;
import com.alessio.wms.material.entity.Material;
import com.alessio.wms.material.repository.MaterialRepository;
import com.alessio.wms.movement.dto.MovementRequest;
import com.alessio.wms.site.entity.Site;
import com.alessio.wms.site.repository.SiteRepository;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MovementControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    SiteRepository siteRepository;

    @Test
    void shouldLoadMaterialSuccessfully() throws Exception {
        createTestMaterial();

        MovementRequest request = new MovementRequest("WIRE-001", 10, "Louis", null);

        mockMvc.perform(post("/api/movements/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUnloadMaterialSuccessfully() throws Exception {
        createTestMaterial();
        Site site = new Site();
        site.setName("Cantiere Albore");
        siteRepository.save(site);

        MovementRequest loadRequest = new MovementRequest("WIRE-001", 10, "Louis", null);
        mockMvc.perform(post("/api/movements/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loadRequest)))
                .andExpect(status().isOk());

        MovementRequest unloadRequest = new MovementRequest("WIRE-001", 5, "Louis", "Cantiere Albore");
        mockMvc.perform(post("/api/movements/unload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unloadRequest)))
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
