package com.pratham.shortner.controller;

import com.pratham.shortner.service.UrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RedirectController {
    private final UrlService urlService;
    public RedirectController(UrlService urlService) { this.urlService = urlService; }

    @GetMapping("/{alias}")
    public ResponseEntity<Void> redirect(@PathVariable String alias) {
        String longUrl = urlService.getLongUrlForRedirect(alias);
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, longUrl)
                .build();
    }
}
