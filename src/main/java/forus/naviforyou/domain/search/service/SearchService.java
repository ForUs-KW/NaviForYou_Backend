package forus.naviforyou.domain.search.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.search.dto.request.SearchReq;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

    private static final double KATECH_FACTOR = 0.00001;

    public SearchRes searchInfo(SearchReq searchReq, String name , String order){


        SearchRes searchInfo = getSearchInfo(name);
        searchInfo = removeTags(searchInfo);
        searchInfo = calculateDistance(searchInfo,searchReq.getX(),searchReq.getY());

        if(order.equals("distance")){
            List<Item> itemList = searchInfo.getItems();
            itemList.sort(Comparator.comparingDouble(Item::getDistance));
            searchInfo.setItems(itemList);
        }

        return searchInfo;
    }

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

        SearchRes searchRes = null;

        // 원하는 형태로 매핑
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            searchRes = objectMapper.readValue(result.getBody(), SearchRes.class);
        }
        catch (Exception e) {
            throw new BaseException(ErrorCode.NO_SUCH_SEARCH);
        }
        return searchRes;
    }

    public SearchRes removeTags(SearchRes searchRes){
        for (Item item : searchRes.getItems()){
            String title = item.getTitle();
            title = removeHtmlTags(title);
            item.setTitle(title);
        }
        return searchRes;
    }

    private static String removeHtmlTags(String html) {
        Document doc = Jsoup.parse(html);
        Element body = doc.body();
        return body.text();
    }

    public SearchRes calculateDistance(SearchRes searchRes,double x1, double y1){
        for (Item item : searchRes.getItems()){
            double x2 = Double.parseDouble(item.getMapx());
            double y2 = Double.parseDouble(item.getMapy());

            // KATECH 좌표계 거리 계산
            double deltaX = (x1 - x2) * KATECH_FACTOR;
            double deltaY = (y1 - y2) * KATECH_FACTOR;

            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            item.setDistance(distance);
        }
        return searchRes;
    }

}
