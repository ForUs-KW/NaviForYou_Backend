package forus.naviforyou.domain.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.member.dto.google.GoogleResInfo;
import forus.naviforyou.domain.search.dto.response.Item;
import forus.naviforyou.domain.search.dto.response.SearchRes;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

//    public String searchInfo(String name){
//
//        SearchRes searchInfo = getSearchInfo(name);
//
//        return searchInfo;
//    }

    public SearchRes getSearchInfo(String name) {

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

//        System.out.println(result.getBody());
        SearchRes searchRes = null;

        // 원하는 형태로 매핑
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            searchRes = objectMapper.readValue(result.getBody(), SearchRes.class);
//            for (Item item : searchRes.getItems()) {
//                System.out.println("Title: " + item.getTitle());
//                System.out.println("MapX: " + item.getMapx());
//                System.out.println("MapY: " + item.getMapy());
//                System.out.println("\n");
//            }
        }
        catch (Exception e) {
            throw new BaseException(ErrorCode.NO_SUCH_SEARCH);
        }
        return searchRes;
    }
}
