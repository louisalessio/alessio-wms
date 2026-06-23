package com.alessio.wms.material.repository;

import com.alessio.wms.BaseIntegrationTest;
import com.alessio.wms.material.entity.Material;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional // to rollback at the end of the test
class MaterialRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MaterialRepository materialRepository;

    @Test
    void saveAndReadMaterial() {
        Material materialToSave = new Material();
        materialToSave.setCode("WIRE-25-MM");
        materialToSave.setDescription("electric copper wire 2.5mm");
        materialToSave.setCost(new BigDecimal("14.50"));

        //save on the DB
        Material savedMaterial = materialRepository.save(materialToSave);
        
        //flush cache to re-read from the DB
        materialRepository.flush(); 
        
        Material materialFound = materialRepository.findById(savedMaterial.getId()).orElseThrow();

        //we check that things have been saved on the DB or fail the test
        assertThat(savedMaterial.getId()).isNotNull();
        assertThat(materialFound.getCode()).isEqualTo("WIRE-25-MM");
        assertThat(materialFound.isActive()).isTrue();
    }
}