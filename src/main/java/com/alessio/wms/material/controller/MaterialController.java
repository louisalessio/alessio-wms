package com.alessio.wms.material.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alessio.wms.material.dto.MaterialRequest;
import com.alessio.wms.material.entity.Material;
import com.alessio.wms.material.repository.MaterialRepository;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    @PostMapping
    public ResponseEntity<Void> createMaterial(@RequestBody MaterialRequest request) {
        Material material = new Material();
        material.setCode(request.code());
        material.setDescription(request.description());
        material.setCost(request.cost());
        material.setActive(true);

        materialRepository.save(material);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}