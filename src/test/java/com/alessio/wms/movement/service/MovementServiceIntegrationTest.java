package com.alessio.wms.movement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.alessio.wms.inventory.entity.Inventory;
import com.alessio.wms.inventory.repository.InventoryRepository;
import com.alessio.wms.material.entity.Material;
import com.alessio.wms.material.repository.MaterialRepository;
import com.alessio.wms.movement.entity.Movement;
import com.alessio.wms.movement.entity.MovementType;
import com.alessio.wms.movement.repository.MovementRepository;

@SpringBootTest
@Transactional
public class MovementServiceIntegrationTest {
    @Autowired
    private MovementService movementService;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private MovementRepository movementRepository;

    @Test
    void whenLoadNewMaterial_thenInventoryIsCreatedAndMovementSaved() {

        Material material = new Material();
        material.setCode("WIRE-001");
        material.setDescription("copper wire");
        material.setActive(true);
        materialRepository.save(material);

        movementService.registerMovement("WIRE-001", MovementType.LOAD, 21, "Louis", null);

        Inventory inventoryDB = inventoryRepository.findById(material.getId()).orElseThrow();
        assertEquals(21, inventoryDB.getQuantity());

        Movement movementDB = movementRepository.findAll().get(0);
        assertEquals(21, movementDB.getQuantity());
        assertEquals(MovementType.LOAD, movementDB.getType());
        assertEquals("Louis", movementDB.getOperator());
    }
}
