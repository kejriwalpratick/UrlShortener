package Pratick.urlShortener.services;

import Pratick.urlShortener.models.UrlEntity;
import Pratick.urlShortener.repositories.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PergeOldUrls {

    @Autowired
    UrlRepository urlRepository;

    private final Long MAX_DAYS_ALLOWED = 30L;

    public void pergeOldest(){

        urlRepository.delete(urlRepository.findFirstByOrderByModifiedAtAsc());
    }

    public void pergeThirtyDaysOldUrls(){

        List<UrlEntity> urlEntityList = urlRepository.findAll();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        for (UrlEntity urlEntity:urlEntityList) {

            if ( TimeUnit.MILLISECONDS.toDays(urlEntity.getModifiedAt().getTime() -  currentTimestamp.getTime()) >= MAX_DAYS_ALLOWED ){
                urlRepository.delete(urlEntity);
            }
        }
    }
}
