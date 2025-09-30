package com.pratham.shortner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class createUrlResponse {
    private String alias;
    private String shortUrl;
    private String longUrl;

}
