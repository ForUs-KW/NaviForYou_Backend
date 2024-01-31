package forus.naviforyou.domain.place.controller;

import forus.naviforyou.domain.place.dto.request.BuildingInfoReq;
import forus.naviforyou.domain.place.dto.request.EditAccessibilityReq;
import forus.naviforyou.domain.place.dto.response.BuildingAccessibilityListRes;
import forus.naviforyou.domain.place.service.PlaceService;
import forus.naviforyou.global.common.BaseResponse;
import forus.naviforyou.global.common.BaseResultRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/app/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping("/convenientFacility")
    public ResponseEntity<?> getBuildingAccessibilityList(@RequestBody BuildingInfoReq req, String member){
        BuildingAccessibilityListRes res = placeService.getBuildingAccessibilityList(req, member);
        return BaseResponse.ok(res);
    }

    @PostMapping("/convenientFacility/editPage")
    public ResponseEntity<?> getBuildingAccessibilityInfoList(@RequestBody BuildingInfoReq req, String member){
        BuildingAccessibilityListRes res = placeService.getBuildingAccessibilityInfoList(req, member);
        return BaseResponse.ok(res);
    }

    @PostMapping("/convenientFacility/edit")
    public ResponseEntity<?> editBuildingAccessibility(@RequestBody EditAccessibilityReq req){
        placeService.editBuildingAccessibility(req);
        return BaseResponse.ok(new BaseResultRes(true));
    }

}
