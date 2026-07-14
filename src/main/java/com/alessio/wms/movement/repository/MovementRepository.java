package com.alessio.wms.movement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alessio.wms.movement.entity.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

}