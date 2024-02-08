package forus.naviforyou.domain.search.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.search.dto.request.SearchReq;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    @Getter
    @Value("${tmap.app-key}")
    private String appKey;

    public SearchRes searchInfo(SearchReq req){
        return getSearchInfo(req);
    }

    public SearchRes getSearchInfo(SearchReq searchReq) {

        URI uri = UriComponentsBuilder
                .fromUriString("https://apis.openapi.sk.com/")
                .path("tmap/pois")
                .queryParam("version",1)
                .queryParam("appKey", appKey)
                .queryParam("searchKeyword", searchReq.getSearchKeyword())
                .queryParam("page", searchReq.getPage())
                .queryParam("count", searchReq.getCount())
                .queryParam("searchtypCd", searchReq.getSearchType())
                .queryParam("radius", searchReq.getRadius())
                .queryParam("centerLon",searchReq.getX())
                .queryParam("centerLat",searchReq.getY())
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        SearchRes searchRes;

        // 원하는 형태로 매핑
        try {
            searchRes = new ObjectMapper().readValue(result.getBody(), SearchRes.class);
        }
        catch (Exception e) {
            log.info("e:",e);
            throw new BaseException(ErrorCode.NO_SUCH_SEARCH);
        }
        return searchRes;
    }




}
