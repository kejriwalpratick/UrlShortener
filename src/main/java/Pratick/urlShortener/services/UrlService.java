package Pratick.urlShortener.services;

import Pratick.urlShortener.dto.Url;
import Pratick.urlShortener.models.UrlEntity;
import Pratick.urlShortener.repositories.UrlRepository;
import Pratick.urlShortener.transformers.UrlTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private static Set<Long> set = new HashSet<>();

    public String createUrl(Url url) {

        if(isNull(urlRepository.findByOriginalUrl(url.getOriginalUrl()))) {

            UrlEntity urlEntity = urlTransformer.transformUrlToEntity(url);
            urlEntity.setShortenedUrl(generateShortUrl());
            urlRepository.save(urlEntity);
            return urlEntity.getShortenedUrl();
        }
        return urlRepository.findByOriginalUrl(url.getOriginalUrl()).getShortenedUrl();
    }

    private static String generateStringFromNumber(Long number) {

        StringBuilder stringBuilder = new StringBuilder();
        while (number > 0) {
            Long remainder = number % 26;
            if (remainder == 0) {
                stringBuilder.append("z");
                number = (number / 26) - 1;
            }
            else {
                stringBuilder.append((char)((remainder - 1) + 'a'));
                number = number / 26;
            }
        }
        return stringBuilder.reverse().toString();
    }

    private static Long generateNumberFromString(String shortUrl){

        Long stringNumber = 0L;
        for (int i = 0; i < shortUrl.length(); i++) {
            stringNumber *= 26;
            stringNumber += shortUrl.charAt(i) - 'a' + 1;
        }
        return stringNumber;
    }

    private String generateShortUrl(){

        while(true){
            Long randomNumber = ThreadLocalRandom.current().nextLong(1,10000001);
            if(!set.contains(randomNumber)){
                set.add(randomNumber);
                return generateStringFromNumber(randomNumber) + ".ly";
            }
        }
    }

    public String getOriginalUrl(String shortUrl) {

        if(isNull(urlRepository.findByShortenedUrl(shortUrl)))
            return "Not found in DB";

        UrlEntity urlEntity = urlRepository.findByShortenedUrl(shortUrl);
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
        Long stringNumber = generateNumberFromString(shortUrl);
        System.out.println("removed " + stringNumber);
        set.remove(stringNumber);
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
