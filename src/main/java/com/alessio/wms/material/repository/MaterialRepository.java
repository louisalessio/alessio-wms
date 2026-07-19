package com.alessio.wms.material.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alessio.wms.material.entity.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByCode(String code);
}