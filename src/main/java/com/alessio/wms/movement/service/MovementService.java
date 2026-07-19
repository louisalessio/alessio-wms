package com.alessio.wms.movement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;

@Transactional
@Service
public class MovementService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private SiteRepository siteRepository;

    public void registerMovement(String materialCode, MovementType type, int quantity, String operator,
            String siteName) {
        Material material = materialRepository.findByCode(materialCode).orElseThrow(
                () -> new IllegalArgumentException("Error: Material Code doesn't exists (" + materialCode + ")"));
        Site site = null;
        if (siteName != null && !siteName.trim().isEmpty()) {
            site = siteRepository.findByName(siteName)
                    .orElseThrow(() -> new IllegalArgumentException("Error: Site doesn't exist (" + siteName + ")"));
        }
        Inventory inventory;
        Movement movement = new Movement();
        if (type == MovementType.UNLOAD) {
            inventory = inventoryRepository.findById(material.getId()).orElseThrow(() -> new IllegalArgumentException(
                    "Inventory doesn't have a record for the material: " + material.getCode()));
            if (inventory.getQuantity() < quantity) {
                throw new InsufficientStockException("Scorte insufficienti per lo scarico");
            }
            inventory.setQuantity(inventory.getQuantity() - quantity);
        } else if (type == MovementType.LOAD) {
            // can be the first time that is added
            inventory = inventoryRepository.findById(material.getId()).orElseGet(() -> {
                Inventory newInventory = new Inventory();
                newInventory.setMaterial(material);
                newInventory.setQuantity(0);
                return newInventory;
            });
            inventory.setQuantity(inventory.getQuantity() + quantity);
        } else {
            throw new IllegalArgumentException("Tipo di movimento non supportato: " + type);
        }
        movement.setMaterial(material);
        movement.setOperator(operator);
        movement.setQuantity(quantity);
        movement.setSite(site);
        movement.setType(type);
        inventoryRepository.save(inventory);
        movementRepository.save(movement);
    }
}