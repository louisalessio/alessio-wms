package com.alessio.wms.movement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.alessio.wms.BaseIntegrationTest;
import com.alessio.wms.inventory.entity.Inventory;
import com.alessio.wms.inventory.repository.InventoryRepository;
import com.alessio.wms.material.entity.Material;
import com.alessio.wms.material.repository.MaterialRepository;
import com.alessio.wms.movement.entity.Movement;
import com.alessio.wms.movement.entity.MovementType;
import com.alessio.wms.movement.exception.InsufficientStockException;
import com.alessio.wms.movement.repository.MovementRepository;
import com.alessio.wms.site.entity.Site;
import com.alessio.wms.site.repository.SiteRepository;

@SpringBootTest
@Transactional
class MovementServiceIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private MovementService movementService;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private MovementRepository movementRepository;
    @Autowired
    private SiteRepository siteRepository;

    @Test
    void whenLoadNewMaterial_thenInventoryIsCreatedAndMovementSaved() {
        Material material = createTestMaterial();

        movementService.registerMovement("WIRE-001", MovementType.LOAD, 21, "Louis", null);

        Inventory inventoryDB = inventoryRepository.findById(material.getId()).orElseThrow();
        assertEquals(21, inventoryDB.getQuantity());

        Movement movementDB = movementRepository.findAll().get(0);
        assertEquals(21, movementDB.getQuantity());
        assertEquals(MovementType.LOAD, movementDB.getType());
        assertEquals("Louis", movementDB.getOperator());
    }

    @Test
    void whenUnloadExistingMaterial_thenQuantityIsDecreased() {
        Material material = createTestMaterial();
        Site site = new Site();
        site.setName("Alessio srl");
        siteRepository.save(site);
        movementService.registerMovement(material.getCode(), MovementType.LOAD, 10, "Louis", null);
        // now unload
        movementService.registerMovement(material.getCode(), MovementType.UNLOAD, 3, "Louis", site.getName());
        Inventory inventoryDB = inventoryRepository.findById(material.getId()).orElseThrow();
        assertEquals(7, inventoryDB.getQuantity());
        assertEquals(movementRepository.findAll().size(), 2);

    }

    @Test
    void whenUnloadMoreThanAvailable_thenThrowInsufficientStockException() {
        Material material = createTestMaterial();
        movementService.registerMovement(material.getCode(), MovementType.LOAD, 10, "Louis", null);
        // now unload more than available
        assertThrows(InsufficientStockException.class, () -> {
            movementService.registerMovement("WIRE-001", MovementType.UNLOAD, 20, "Louis", null);
        });
    }

    Material createTestMaterial() {
        Material material = new Material();
        material.setCode("WIRE-001");
        material.setDescription("copper wire");
        material.setActive(true);
        return materialRepository.save(material);
    }
}
