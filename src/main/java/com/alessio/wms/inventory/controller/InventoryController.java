package com.alessio.wms.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alessio.wms.inventory.entity.Inventory;
import com.alessio.wms.inventory.repository.InventoryRepository;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping
    public ResponseEntity<List<Inventory>> getInventory() {
        List<Inventory> inventory = inventoryRepository.findAll();
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }
}