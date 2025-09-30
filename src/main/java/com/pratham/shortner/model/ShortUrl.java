package com.pratham.shortner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="short_urls",indexes = {@Index(columnList = "alias", name = "idx_alias")})
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = true, unique = true, length = 128)
    private String alias;

    @Column(nullable = false,length = 2048)
    private String longUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private long clicks = 0L;
}
