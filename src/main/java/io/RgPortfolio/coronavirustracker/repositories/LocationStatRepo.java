package io.RgPortfolio.coronavirustracker.repositories;

import io.RgPortfolio.coronavirustracker.model.LocationStats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationStatRepo extends CrudRepository<LocationStats,Long> {
}
