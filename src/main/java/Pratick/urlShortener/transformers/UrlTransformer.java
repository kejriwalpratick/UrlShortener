package Pratick.urlShortener.transformers;

import Pratick.urlShortener.dto.Url;
import Pratick.urlShortener.models.UrlEntity;
import org.springframework.stereotype.Service;

@Service
public class UrlTransformer {

    public UrlEntity transformUrlToEntity(Url url){

        return UrlEntity.builder()
                .id(url.getId())
                .createdAt(url.getCreatedAt())
                .modifiedAt(url.getModifiedAt())
                .originalUrl(url.getOriginalUrl())
                .shortenedUrl(url.getShortenedUrl())
                .build();
    }

    public Url transformEntityToUrl(UrlEntity urlEntity){
        return Url.builder()
                .id(urlEntity.getId())
                .createdAt(urlEntity.getCreatedAt())
                .modifiedAt(urlEntity.getModifiedAt())
                .originalUrl(urlEntity.getOriginalUrl())
                .shortenedUrl(urlEntity.getShortenedUrl())
                .build();
    }
}
