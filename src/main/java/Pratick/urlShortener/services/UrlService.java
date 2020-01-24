package Pratick.urlShortener.services;

import Pratick.urlShortener.dto.Url;
import Pratick.urlShortener.models.UrlEntity;
import Pratick.urlShortener.repositories.UrlRepository;
import Pratick.urlShortener.transformers.UrlTransformer;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String createUrl(Url url) {

        if(isNull(urlRepository.findByOriginalUrl(url.getOriginalUrl()))) {

            UrlEntity urlEntity = urlTransformer.transformUrlToEntity(url);
            urlEntity.setShortenedUrl(ShorteningService.generateShortUrl());
            urlRepository.save(urlEntity);
            return urlEntity.getShortenedUrl();
        }
        return urlRepository.findByOriginalUrl(url.getOriginalUrl()).getShortenedUrl();
    }

    public String getOriginalUrl(String shortUrl) {

        if(isNull(urlRepository.findByShortenedUrl(shortUrl)))
            return "Not found in DB";

        return urlRepository.findByShortenedUrl(shortUrl).getOriginalUrl();
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
        ShorteningService.deleteShortUrl(shortUrl);
        urlRepository.delete(urlEntity);
        return url + " is deleted";
    }

    public String redirectToOriginal(String shortUrl) {

        UrlEntity urlEntity = urlRepository.findByShortenedUrl(shortUrl);
        String originalUrl = urlEntity.getOriginalUrl();
        String redirectUrl = "https://" + originalUrl;
        return redirectUrl;
    }
}
