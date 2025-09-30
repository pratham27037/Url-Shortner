package com.pratham.shortner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratham.shortner.dto.CreateUrlRequest;
import com.pratham.shortner.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // if you want test-specific config
public class UrlControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortUrlRepository repo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createShortUrl_returns201() throws Exception {
        CreateUrlRequest req = new CreateUrlRequest();
        req.setLongUrl("https://spring.io");

        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias", notNullValue()))
                .andExpect(jsonPath("$.shortUrl", startsWith("http://localhost:8080/")))
                .andExpect(jsonPath("$.longUrl", is("https://spring.io")));
    }

    @Test
    void redirect_existingAlias_returns302() throws Exception {
        // First create alias
        CreateUrlRequest req = new CreateUrlRequest();
        req.setLongUrl("https://spring.io");

        String alias = mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String extractedAlias = objectMapper.readTree(alias).get("alias").asText();

        // Now call redirect
        mockMvc.perform(get("/" + extractedAlias))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://spring.io"));
    }
}
