package forus.naviforyou.domain.place.controller;

import forus.naviforyou.domain.place.dto.request.ConvenientFacilityReq;
import forus.naviforyou.domain.place.dto.response.ConvenientFacilityRes;
import forus.naviforyou.domain.place.service.PlaceService;
import forus.naviforyou.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/app/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping("/convenientFacility")
    public ResponseEntity<?> getConvenientFacility(@RequestBody ConvenientFacilityReq convenientFacilityReq){
        ConvenientFacilityRes res = placeService.getConvenientFacility(convenientFacilityReq);
        return BaseResponse.ok(res);
    }

}
