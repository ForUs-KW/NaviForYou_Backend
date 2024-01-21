package forus.naviforyou.domain.search.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    @Getter
    @Value("${social.naver.params.clientId}")
    private String clientId;

    @Getter
    @Value("${social.naver.params.clientSecret}")
    private String clientSecret;



    public String naver(@PathVariable String name) {

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com/")
                .path("v1/search/local.json")
                .queryParam("query", name) //query=검색어&display=10&start=1&sort=random
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "random")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", getClientId())
                .header("X-Naver-Client-Secret",getClientSecret())
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        return result.getBody();
    }
}
