package Pratick.urlShortener.controllers;

import Pratick.urlShortener.dto.Url;
import Pratick.urlShortener.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/url-shortener")
public class UrlController {

    @Autowired
    UrlService urlService;

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public String createUrl(@Valid @RequestBody Url url){
        return urlService.createUrl(url);
    }

    @RequestMapping(value = "/get-original-url/{short_url}",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getOriginalUrl(@PathVariable(value = "short_url") String shortUrl){
        return urlService.getOriginalUrl(shortUrl);
    }

    @RequestMapping(value = "/delete-url/{url}",
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public String deleteUrl(@PathVariable(value = "url") String url){
        return urlService.deleteUrl(url);
    }

    @RequestMapping("/{short-url}")
    @ResponseStatus(HttpStatus.OK)
    public void redirectToOriginal(@PathVariable(value = "short-url")String shortUrl, HttpServletResponse httpServletResponse) throws IOException {

        String redirectUrl = urlService.redirectToOriginal(shortUrl);
        httpServletResponse.sendRedirect(redirectUrl);
    }
}
