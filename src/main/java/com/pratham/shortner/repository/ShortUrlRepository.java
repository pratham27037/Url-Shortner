package com.pratham.shortner.repository;

import com.pratham.shortner.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl,Long> {
    Optional<ShortUrl> findByAlias(String alias);
    boolean existsByAlias(String alias);
}
