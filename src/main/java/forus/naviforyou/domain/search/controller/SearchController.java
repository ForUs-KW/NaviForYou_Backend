package forus.naviforyou.domain.search.controller;

import forus.naviforyou.domain.member.dto.request.SignUpReq;
import forus.naviforyou.domain.search.dto.request.SearchReq;
import forus.naviforyou.domain.search.dto.response.SearchRes;
import forus.naviforyou.domain.search.service.SearchService;
import forus.naviforyou.global.common.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @ApiOperation(tags = "5. search", value = "키워드 검색", notes = "검색하여 결과를 출력합니다")

    @GetMapping("/{name}")
    public ResponseEntity<?> getSearch(@RequestParam(name = "order", defaultValue = "accuracy") String order , @RequestBody SearchReq searchReq, @PathVariable String name){
        SearchRes searchRes = searchService.searchInfo(searchReq , name , order);
        return BaseResponse.ok(searchRes);
    }


}
