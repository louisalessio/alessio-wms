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
        materialRepository.save(wire);

        Inventory inv = new Inventory();
        inv.setMaterial(wire);
        inv.setQuantity(100);
        inventoryRepository.save(inv);

        entityManager.flush(); // write it into the database
        entityManager.clear(); // clear the cache of Hibernate to simulate 2 different users

        // Mario reads from the database
        Inventory marioInventory = inventoryRepository.findById(wire.getId()).orElseThrow();

        // Luigi reads from the database at the same time
        Inventory luigiInventory = inventoryRepository.findById(wire.getId()).orElseThrow();

        // Mario updates and saves first
        marioInventory.setQuantity(80);
        inventoryRepository.save(marioInventory);
        entityManager.flush(); // the database updates the version from 0 to 1

        // Luigi tries to update his old version (which still has version 0)
        luigiInventory.setQuantity(70);

        // we expect a exception of optimistic locking
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            inventoryRepository.save(luigiInventory);
            entityManager.flush(); // error
        });
    }
}