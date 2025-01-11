package com.ht.qlktx.modules.region;

import com.ht.qlktx.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, String> {
    List<Region> findByIdContainingIgnoreCase(String keyword);

    boolean existsByName(String name);

    Optional<Region> findByIdAndDeletedIsFalse(String id);

    List<Region> findAllByDeletedIsFalse();

    List<Region> findByIdContainingIgnoreCaseAndDeletedIsFalse(String keyword);

    Long countByDeletedIsFalse();

    boolean existsByIdAndDeletedIsFalse(String id);

    boolean existsByNameAndDeletedIsFalse(String name);
}
