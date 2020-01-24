package Pratick.urlShortener.repositories;

import Pratick.urlShortener.models.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlEntity,Long> {

    UrlEntity findByOriginalUrl(String originalUrl);

    UrlEntity findByShortenedUrl(String shortUrl);
}
