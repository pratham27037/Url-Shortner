package com.pratham.shortner.controller;

import com.pratham.shortner.dto.CreateUrlRequest;
import com.pratham.shortner.dto.createUrlResponse;
import com.pratham.shortner.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/urls")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<createUrlResponse> create(@Valid @RequestBody CreateUrlRequest req){
        createUrlResponse res = urlService.createShortUrl(req);
        return ResponseEntity.created(java.net.URI.create(res.getShortUrl())).body(res);
    }
}
