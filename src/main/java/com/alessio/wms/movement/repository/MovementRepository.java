package com.alessio.wms.movement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alessio.wms.material.entity.Material;
import com.alessio.wms.movement.entity.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    Optional<Movement> findByMaterial(Material m);
}