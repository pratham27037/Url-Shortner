package com.pratham.shortner.service;

import com.pratham.shortner.dto.CreateUrlRequest;
import com.pratham.shortner.dto.createUrlResponse;
import com.pratham.shortner.exception.AliasAlreadyExistsException;
import com.pratham.shortner.exception.InvalidUrlException;
import com.pratham.shortner.exception.NotFoundException;
import com.pratham.shortner.model.ShortUrl;
import com.pratham.shortner.repository.ShortUrlRepository;
import com.pratham.shortner.util.Base62;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

@Service
public class UrlService {

    private final ShortUrlRepository repo;
    private final String baseUrl;

    public UrlService(ShortUrlRepository repo, @Value("${app.base-url}") String baseUrl) {
        this.repo = repo;
        this.baseUrl = baseUrl;
    }

    private void validateLongUrl(String longUrl){
        try{
            URL u = new URL(longUrl);
            String protocol = u.getProtocol();
            if(!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)){
                throw new InvalidUrlException("Only http/https URLs are allowed");
            }
        }catch (MalformedURLException e){
            throw new InvalidUrlException("Invalid url format");
        }
    }

    public createUrlResponse createShortUrl(CreateUrlRequest req){
        validateLongUrl(req.getLongUrl());

        if(req.getCustomAlias() != null && !req.getCustomAlias().isBlank()){
            String alias = req.getCustomAlias();
            if(repo.existsByAlias(alias)){
                throw new AliasAlreadyExistsException("Alias already in use: "+alias);
            }
            ShortUrl s = new ShortUrl();
            s.setAlias(alias);
            s.setLongUrl(req.getLongUrl());
            s.setCreatedAt(LocalDateTime.now());
            try{
                s = repo.save(s);
            }catch (DataIntegrityViolationException ex){
                throw new AliasAlreadyExistsException("Alias already in use: "+alias);
            }
            String shortUrl = buildShortUrl(alias);
            return new createUrlResponse(alias, shortUrl, s.getLongUrl());
        }
        // no custom alias then
        ShortUrl s = new ShortUrl();
        s.setLongUrl(req.getLongUrl());
        s.setCreatedAt(LocalDateTime.now());
        s = repo.save(s);// get the id

        String alias = Base62.encode(s.getId());
        s.setAlias(alias);

        try{
            s = repo.save(s);
        }catch (DataIntegrityViolationException ex){
            throw new AliasAlreadyExistsException("Alias generated conflit for id: "+s.getId());
        }

        String shortUrl = buildShortUrl(alias);
        return new createUrlResponse(alias,shortUrl,s.getLongUrl());
    }

    private String buildShortUrl(String alias) {
        return baseUrl.endsWith("/") ? baseUrl + alias : baseUrl + "/" + alias;
    }

    @Transactional
    public String getLongUrlForRedirect(String alias) {
        ShortUrl s = repo.findByAlias(alias)
                .orElseThrow(() -> new NotFoundException("Alias not found: " + alias));
        s.setClicks(s.getClicks() + 1);
        repo.save(s);
        return s.getLongUrl();
    }

}
