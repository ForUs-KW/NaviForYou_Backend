package forus.naviforyou.domain.place.controller;

import forus.naviforyou.domain.place.dto.request.ExistenceFacilityListReq;
import forus.naviforyou.domain.place.dto.request.EditFacilityReq;
import forus.naviforyou.domain.place.dto.response.ExistenceFacilityListRes;
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
    public ResponseEntity<?> getExistenceFacilityList(@RequestBody ExistenceFacilityListReq req, String member){
        ExistenceFacilityListRes res = placeService.getExistenceFacilityList(req, member);
        return BaseResponse.ok(res);
    }

    @PostMapping("/convenientFacility/editPage")
    public ResponseEntity<?> getFacilityList(@RequestBody ExistenceFacilityListReq req, String member){
        ExistenceFacilityListRes res = placeService.getFacilityList(req, member);
        return BaseResponse.ok(res);
    }

    @PostMapping("/convenientFacility/edit")
    public ResponseEntity<?> editFacility(@RequestBody EditFacilityReq req){
        placeService.editFacility(req);
        return BaseResponse.ok(new BaseResultRes(true));
    }

}
