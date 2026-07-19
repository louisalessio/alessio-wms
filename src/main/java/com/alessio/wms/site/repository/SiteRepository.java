package com.alessio.wms.site.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alessio.wms.site.entity.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    List<Site> findAllByActiveTrue();

    Optional<Site> findByIdAndActiveTrue(Long id);

    Optional<Site> findByName(String name);
}