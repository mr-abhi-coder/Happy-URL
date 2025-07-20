package url.shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import url.shortener.models.ClickEvent;
import url.shortener.models.UrlMapping;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    // For a single UrlMapping
    List<ClickEvent> findByUrlMappingAndClickDateBetween(
            UrlMapping urlMapping,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // For multiple UrlMappings
    List<ClickEvent> findByUrlMappingInAndClickDateBetween(
            List<UrlMapping> urlMappings,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
