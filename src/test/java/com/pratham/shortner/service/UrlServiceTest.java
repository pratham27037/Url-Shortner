package com.pratham.shortner.service;

import com.pratham.shortner.dto.CreateUrlRequest;
import com.pratham.shortner.dto.createUrlResponse;
import com.pratham.shortner.exception.AliasAlreadyExistsException;
import com.pratham.shortner.model.ShortUrl;
import com.pratham.shortner.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlServiceTest {

    private ShortUrlRepository repo;
    private UrlService service;

    @BeforeEach
    void setUp() {
        repo = mock(ShortUrlRepository.class);
        service = new UrlService(repo, "http://localhost:8080");
    }

    @Test
    void createWithGeneratedAlias_succeeds() {
        CreateUrlRequest req = new CreateUrlRequest();
        req.setLongUrl("https://example.com/page");

        ShortUrl savedWithId = new ShortUrl();
        savedWithId.setId(123L);
        savedWithId.setLongUrl(req.getLongUrl());
        savedWithId.setCreatedAt(LocalDateTime.now());

        // first save returns object with id
        when(repo.save(any(ShortUrl.class))).thenReturn(savedWithId).thenReturn(savedWithId);

        createUrlResponse resp = service.createShortUrl(req);
        assertNotNull(resp);
        assertEquals(req.getLongUrl(), resp.getLongUrl());
        assertNotNull(resp.getAlias());
        assertTrue(resp.getShortUrl().contains(resp.getAlias()));
        verify(repo, atLeastOnce()).save(any(ShortUrl.class));
    }

    @Test
    void createWithCustomAlias_alreadyExists_throws() {
        CreateUrlRequest req = new CreateUrlRequest();
        req.setLongUrl("https://example.com/page");
        req.setCustomAlias("myAlias");

        when(repo.existsByAlias("myAlias")).thenReturn(true);

        assertThrows(AliasAlreadyExistsException.class, () -> service.createShortUrl(req));
        verify(repo, never()).save(any(ShortUrl.class));
    }

    @Test
    void redirect_existingAlias_incrementsClicks() {
        ShortUrl s = new ShortUrl();
        s.setId(1L);
        s.setAlias("abc");
        s.setLongUrl("https://example.com");
        s.setCreatedAt(LocalDateTime.now());
        s.setClicks(5);

        when(repo.findByAlias("abc")).thenReturn(Optional.of(s));
        when(repo.save(any(ShortUrl.class))).thenAnswer(inv -> inv.getArgument(0));

        String url = service.getLongUrlForRedirect("abc");

        assertEquals("https://example.com", url);
        assertEquals(6, s.getClicks());
    }

}
