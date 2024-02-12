package forus.naviforyou.domain.place.controller;

import forus.naviforyou.domain.place.dto.request.BuildingInfoReq;
import forus.naviforyou.domain.place.dto.request.EditAccessibilityReq;
import forus.naviforyou.domain.place.dto.response.BuildingAccessibilityListRes;
import forus.naviforyou.domain.place.dto.response.SubwayRealTimeRes;
import forus.naviforyou.domain.place.service.PlaceService;
import forus.naviforyou.global.common.BaseResponse;
import forus.naviforyou.global.common.BaseResultRes;
import forus.naviforyou.global.common.collection.member.Member;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/app/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @ApiOperation(tags = "4. place", value = "편의 시설 정보", notes = "건물의 편의시설 정보를 가져옵니다")

    @PostMapping("/convenientFacility")
    public ResponseEntity<?> getBuildingAccessibilityList(@RequestBody BuildingInfoReq req, @AuthenticationPrincipal Member member){
        BuildingAccessibilityListRes res = placeService.getBuildingAccessibilityList(req, member.getNickname());
        return BaseResponse.ok(res);
    }

    @PostMapping("/convenientFacility/editPage")
    public ResponseEntity<?> getBuildingAccessibilityInfoList(@RequestBody BuildingInfoReq req, @AuthenticationPrincipal Member member){
        BuildingAccessibilityListRes res = placeService.getBuildingAccessibilityInfoList(req, member.getNickname());
        return BaseResponse.ok(res);
    }

    @PostMapping("/convenientFacility/edit")
    public ResponseEntity<?> editBuildingAccessibility(@RequestBody EditAccessibilityReq req, @AuthenticationPrincipal Member member){
        placeService.editBuildingAccessibility(req, member.getNickname());
        return BaseResponse.ok(new BaseResultRes(true));
    }

    @ApiOperation(tags = "4. place", value = "실시간 지하철 도착 정보", notes = "지하철역의 특정 호선의 실시간 도착 정보를 가져옵니다.")
    @GetMapping("/subway/{name}/{line}")
    public ResponseEntity<?> getSubwayRealTime(@PathVariable String name, @PathVariable String line){
        SubwayRealTimeRes res = placeService.getSubwayRealTime(name,line);
        return BaseResponse.ok(res);
    }

}
