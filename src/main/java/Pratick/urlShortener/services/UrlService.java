package Pratick.urlShortener.services;

import Pratick.urlShortener.dto.Url;
import Pratick.urlShortener.models.UrlEntity;
import Pratick.urlShortener.repositories.UrlRepository;
import Pratick.urlShortener.transformers.UrlTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Objects.isNull;


@Service
public class UrlService {

    @Autowired
    UrlTransformer urlTransformer;
    @Autowired
    UrlRepository urlRepository;
    @Autowired
    ShorteningService shorteningService;
    @Autowired
    PergeOldUrls pergeOldUrls;

    private final Long MAX_COUNT = 10000000L;

    public String createUrl(Url url) {

        if(isNull(urlRepository.findByOriginalUrl(url.getOriginalUrl()))) {

            if(urlRepository.count() == MAX_COUNT) {

                pergeOldUrls.pergeOldest();
            }
            UrlEntity urlEntity = urlTransformer.transformUrlToEntity(url);
            urlEntity.setShortenedUrl(shorteningService.generateShortUrl());
            urlEntity.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            urlRepository.save(urlEntity);
            return urlEntity.getShortenedUrl();
        }
        UrlEntity urlEntity = urlRepository.findByOriginalUrl(url.getOriginalUrl());
        urlEntity.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        urlRepository.save(urlEntity);
        return  urlEntity.getShortenedUrl();
    }

    public String getOriginalUrl(String shortUrl) {

        if(isNull(urlRepository.findByShortenedUrl(shortUrl)))
            return "Not found in DB";

        UrlEntity urlEntity = urlRepository.findByShortenedUrl(shortUrl);
        urlEntity.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        urlRepository.save(urlEntity);
        return urlEntity.getOriginalUrl();
    }

    public String deleteUrl(String url){

        UrlEntity urlEntity;
        if(isNull(urlRepository.findByOriginalUrl(url)) && isNull(urlRepository.findByShortenedUrl(url))){
            return "Url not found in DB";
        }
        else if(isNull(urlRepository.findByOriginalUrl(url))) {
            urlEntity = urlRepository.findByShortenedUrl(url);
        }
        else {
            urlEntity = urlRepository.findByOriginalUrl(url);
        }
        String shortUrl = urlEntity.getShortenedUrl().substring( 0 , urlEntity.getShortenedUrl().length()-3 );
        shorteningService.deleteShortUrl(shortUrl);
        urlRepository.delete(urlEntity);
        return url + " is deleted";
    }

    public String redirectToOriginal(String shortUrl) {

        UrlEntity urlEntity = urlRepository.findByShortenedUrl(shortUrl);
        String originalUrl = urlEntity.getOriginalUrl();
        String redirectUrl = "https://" + originalUrl;
        return redirectUrl;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void pergeOldUrls(){
        pergeOldUrls.pergeThirtyDaysOldUrls();
    }
}
