package com.alessio.wms.movement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.alessio.wms.BaseIntegrationTest;
import com.alessio.wms.material.entity.Material;
import com.alessio.wms.material.repository.MaterialRepository;
import com.alessio.wms.movement.entity.Movement;
import com.alessio.wms.movement.entity.MovementType;
import com.alessio.wms.site.entity.Site;
import com.alessio.wms.site.repository.SiteRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MovementRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Test
    void saveAndReadMovement() {
        // to test this we need to create a site and a material first
        Material material = new Material();
        material.setCode("WIRE-25-MM");
        material.setDescription("electric copper wire 2.5mm");
        material.setCost(new BigDecimal("14.50"));
        // save in the DB
        Material savedMaterial = materialRepository.save(material);
        // flush cache to re-read from the DB
        materialRepository.flush();
        Material materialFound = materialRepository.findById(savedMaterial.getId()).orElseThrow();
        // create an active site
        Site activeSite = new Site();
        activeSite.setName("Cantiere Rossiazzo");
        activeSite.setAddress("Via Colosseo 1");
        activeSite.setActive(true);
        siteRepository.save(activeSite);
        // tries the method we added on the repository
        
        List<Site> activeSites = siteRepository.findAllByActiveTrue();

        // now we can add the movement since we have a site and a material
        Movement movement = new Movement();
        movement.setMaterial(materialFound);
        movement.setSite(activeSite);
        movement.setQuantity(10);
        movement.setType(MovementType.LOAD);
        movement.setOperator("Louis");
        // save in the DB
        Movement savedMovement = movementRepository.save(movement);
        // flush cache to re-read from the DB
        movementRepository.flush();
        Movement movementFound = movementRepository.findById(savedMovement.getId()).orElseThrow();

        // we check that things have been saved on the DB or fail the test
        assertThat(savedMovement.getId()).isNotNull();
        assertThat(movementFound.getMaterial().getCode()).isEqualTo("WIRE-25-MM");
        assertThat(movementFound.getSite().getName()).isEqualTo("Cantiere Rossiazzo");
        assertThat(activeSites.contains(activeSite)).isTrue();
        assertThat(movementFound.getMovementDate()).isNotNull();
    }
}