package Pratick.urlShortener.services;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ShorteningService {

    private static Set<Long> set = new HashSet<>();

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

    public static String generateShortUrl(){

        while(true){
            Long randomNumber = ThreadLocalRandom.current().nextLong(1,10000001);
            if(!set.contains(randomNumber)){
                set.add(randomNumber);
                return generateStringFromNumber(randomNumber) + ".ly";
            }
        }
    }

    public static void deleteShortUrl(String shortUrl) {

        Long stringNumber = generateNumberFromString(shortUrl);
        System.out.println("removed " + stringNumber);
        set.remove(stringNumber);
    }
}
