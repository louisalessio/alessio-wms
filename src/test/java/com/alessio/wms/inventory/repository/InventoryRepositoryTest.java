package com.alessio.wms.inventory.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.alessio.wms.BaseIntegrationTest;
import com.alessio.wms.inventory.entity.Inventory;
import com.alessio.wms.material.entity.Material;
import com.alessio.wms.material.repository.MaterialRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InventoryRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldThrowExceptionOnConcurrentUpdates() {
        Material wire = new Material();
        wire.setCode("WIRE-001");
        wire.setDescription("copper wire");
        Material savedWire = materialRepository.save(wire);

        Inventory inv = new Inventory();
        inv.setMaterial(savedWire);
        inv.setQuantity(100);

        inventoryRepository.saveAndFlush(inv);

        entityManager.clear(); 

        //Mario reads from the database
        Inventory marioInventory = inventoryRepository.findById(savedWire.getId()).orElseThrow();
        entityManager.clear();  
        //Luigi reads from the database at the same time
        Inventory luigiInventory = inventoryRepository.findById(savedWire.getId()).orElseThrow();
        entityManager.clear();
        //Mario updates and saves first
        marioInventory.setQuantity(80);
        inventoryRepository.saveAndFlush(marioInventory);   

        //Luigi tries to update his old version
        luigiInventory.setQuantity(70);

        //we expect a exception of optimistic locking
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            inventoryRepository.saveAndFlush(luigiInventory);
        });
    }
}