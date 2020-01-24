package Pratick.urlShortener.services;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ShorteningService {

    private Set<Long> set = new HashSet<>();

    private String generateStringFromNumber(Long number) {

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

    private Long generateNumberFromString(String shortUrl){

        Long stringNumber = 0L;
        for (int i = 0; i < shortUrl.length(); i++) {
            stringNumber *= 26;
            stringNumber += shortUrl.charAt(i) - 'a' + 1;
        }
        return stringNumber;
    }

    public String generateShortUrl(){

        while(true){
            Long randomNumber = ThreadLocalRandom.current().nextLong(1,10000001);
            if(!set.contains(randomNumber)){
                set.add(randomNumber);
                return generateStringFromNumber(randomNumber) + ".ly";
            }
        }
    }

    public void deleteShortUrl(String shortUrl) {

        Long stringNumber = generateNumberFromString(shortUrl);
        System.out.println("removed " + stringNumber);
        set.remove(stringNumber);
    }
}
