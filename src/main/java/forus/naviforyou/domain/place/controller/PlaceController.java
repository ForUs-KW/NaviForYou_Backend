package forus.naviforyou.domain.place.controller;

import forus.naviforyou.domain.place.dto.request.BuildingInfoReq;
import forus.naviforyou.domain.place.dto.request.EditAccessibilityReq;
import forus.naviforyou.domain.place.dto.request.LocationReq;
import forus.naviforyou.domain.place.dto.response.BuildingAccessibilityListRes;
import forus.naviforyou.domain.place.dto.response.LocationRes;
import forus.naviforyou.domain.place.dto.response.BuildingInfoRes;
import forus.naviforyou.domain.place.dto.tmap.PoiBuildingInfo;
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

    @ApiOperation(tags = "4. place", value = "좌표 주소 변환", notes = "좌표 정보를 주소 정보로 변환합니다")
    @PostMapping
    public ResponseEntity<?> getBuildingInfo(@RequestBody LocationReq req){
        LocationRes res = placeService.convertLocationToAddress(req);
        return BaseResponse.ok(res);
    }

    @ApiOperation(tags = "4. place", value = "건물 정보", notes = "건물 정보 및 건물의 장애인 편의 시설 정보를 가져옵니다")
    @PostMapping("/building")
    public ResponseEntity<?> getBuildingInfo(@RequestBody BuildingInfoReq req, @AuthenticationPrincipal Member member){
        BuildingInfoRes res = placeService.getBuildingInfo(req);
        res.setBuildingAccessibilityList(placeService.getBuildingAccessibilityList(req, (member == null) ? "guest" : member.getNickname()));
        return BaseResponse.ok(res);
    }


    @ApiOperation(tags = "4. place", value = "장애인 편의 시설 정보", notes = "장애인 편의 시설 정보를 가져옵니다")
    @PostMapping("/convenientFacility")
    public ResponseEntity<?> getBuildingAccessibilityInfoList(@RequestBody BuildingInfoReq req, @AuthenticationPrincipal Member member){
        BuildingAccessibilityListRes res = placeService.getBuildingAccessibilityInfoList(req, member.getNickname());
        return BaseResponse.ok(res);
    }

    @ApiOperation(tags = "4. place", value = "장애인 편의 시설 수정", notes = "장애인 편의 시설 정보를 수정합니다.")
    @PostMapping("/convenientFacility/edit")
    public ResponseEntity<?> editBuildingAccessibility(@RequestBody EditAccessibilityReq req, @AuthenticationPrincipal Member member){
        placeService.editBuildingAccessibility(req, member.getNickname());
        return BaseResponse.ok(new BaseResultRes(true));
    }

    @ApiOperation(tags = "4. place", value = "실시간 지하철 도착 정보", notes = "지하철역의 특정 호선의 실시간 도착 정보를 가져옵니다.")
    @GetMapping("/subway/{name}")
    public ResponseEntity<?> getSubwayRealTime(@PathVariable String name, @RequestParam String line){
        SubwayRealTimeRes res = placeService.getSubwayRealTime(name,line);
        return BaseResponse.ok(res);
    }

}
