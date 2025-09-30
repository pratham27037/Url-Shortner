package com.pratham.shortner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUrlRequest {
    @NotBlank(message = "longUrl is required")
    private String longUrl;

    // optional custom alias: letters, numbers, -, _
    @Pattern(regexp = "^[A-Za-z0-9_-]{3,50}$", message = "customAlias must be 3-50 chars (A-Z a-z 0-9 _ -)")
    private String customAlias;
}
