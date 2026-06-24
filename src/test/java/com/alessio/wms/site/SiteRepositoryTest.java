package com.alessio.wms.site;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.alessio.wms.BaseIntegrationTest;
import com.alessio.wms.site.entity.Site;
import com.alessio.wms.site.repository.SiteRepository;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // so spring doesn't create a DB just for
                                                                             // tests
@DataJpaTest // implicit transactional annotation
class SiteRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private SiteRepository siteRepository;

    @Test
    void shouldFindOnlyActiveSites() {
        // create an active and an inactive site
        Site activeSite = new Site();
        activeSite.setName("Cantiere Roma");
        activeSite.setAddress("Via Colosseo 1");
        activeSite.setActive(true);
        siteRepository.save(activeSite);

        Site inactiveSite = new Site();
        inactiveSite.setName("Cantiere Milano");
        inactiveSite.setAddress("Piazza Duomo 2");
        inactiveSite.setActive(false); // soft delete simulation
        siteRepository.save(inactiveSite);

        // tries the method we added on the repository
        List<Site> activeSites = siteRepository.findAllByActiveTrue();

        assertThat(activeSites).hasSize(1);
        assertThat(activeSites.get(0).getName()).isEqualTo("Cantiere Roma");
    }

    @Test
    void shouldFindActiveSiteById() {
        // create an active and an inactive site
        Site activeSite = new Site();
        activeSite.setName("Cantiere Roma");
        activeSite.setActive(true);
        siteRepository.save(activeSite);

        // tries the method we added on the repository
        Optional<Site> activeSingleSite = siteRepository.findByIdAndActiveTrue(activeSite.getId());

        assertThat(activeSingleSite).isPresent();
        assertThat(activeSingleSite.get().getName()).isEqualTo("Cantiere Roma");
    }

}
