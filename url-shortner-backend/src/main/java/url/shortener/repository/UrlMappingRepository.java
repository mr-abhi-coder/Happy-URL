package url.shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import url.shortener.models.UrlMapping;
import url.shortener.models.User;

import java.util.List;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    UrlMapping findByShortUrl(String shortUrl);
    List<UrlMapping> findByUser(User user);
}
