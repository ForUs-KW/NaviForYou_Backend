package forus.naviforyou.domain.place.repository;


import forus.naviforyou.global.common.collection.building.Building;
import forus.naviforyou.global.common.collection.building.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuildingRepository extends MongoRepository<Building, Long> {
    Optional<Building> findByLocation(Location location);
}
