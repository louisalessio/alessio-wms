package com.alessio.wms.movement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alessio.wms.movement.dto.MovementRequest;
import com.alessio.wms.movement.entity.MovementType;
import com.alessio.wms.movement.service.MovementService;

@RestController
@RequestMapping("/api/movements")
public class MovementController {

    @Autowired
    MovementService movementService;

    @PostMapping("/load")
    public void loadMaterial(@RequestBody MovementRequest request) {
        movementService.registerMovement(
            request.materialCode(),
            MovementType.LOAD,
            request.quantity(),
            request.operator(),
            null
        );
    }

    @PostMapping("/unload")
    public void unloadMaterial(@RequestBody MovementRequest request) {
        movementService.registerMovement(
            request.materialCode(),
            MovementType.UNLOAD,
            request.quantity(),
            request.operator(),
            request.siteName()
        );
    }

}
