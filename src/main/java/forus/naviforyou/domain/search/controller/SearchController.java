package forus.naviforyou.domain.search.controller;

import forus.naviforyou.domain.member.dto.request.SignUpReq;
import forus.naviforyou.domain.search.dto.request.SearchReq;
import forus.naviforyou.domain.search.dto.response.SearchRes;
import forus.naviforyou.domain.search.service.SearchService;
import forus.naviforyou.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    @GetMapping("/{name}")
    public ResponseEntity<?> getSearch(@RequestBody SearchReq searchReq, @PathVariable String name){
        SearchRes searchRes = searchService.searchInfo(searchReq , name);
        return BaseResponse.ok(searchRes);
    }


}
