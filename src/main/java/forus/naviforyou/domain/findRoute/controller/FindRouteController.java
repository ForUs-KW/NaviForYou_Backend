package forus.naviforyou.domain.findRoute.controller;


import forus.naviforyou.domain.findRoute.dto.request.CarRouteReq;
import forus.naviforyou.domain.findRoute.dto.request.TravelRouteReq;
import forus.naviforyou.domain.findRoute.dto.request.WalkRouteReq;
import forus.naviforyou.domain.findRoute.dto.response.CarRouteRes;
import forus.naviforyou.domain.findRoute.dto.response.TravelRouteRes;
import forus.naviforyou.domain.findRoute.dto.response.WalkRouteRes;
import forus.naviforyou.domain.findRoute.service.CarFindRouteService;
import forus.naviforyou.domain.findRoute.service.TravelFindRouteService;
import forus.naviforyou.domain.findRoute.service.WalkFindRouteService;
import forus.naviforyou.global.common.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/findRoute")
@RequiredArgsConstructor
public class FindRouteController {

    private final CarFindRouteService carFindRouteService;
    private final WalkFindRouteService walkFindRouteService;
    private final TravelFindRouteService travelFindRouteService;


    @ApiOperation(tags = "1. Find Route", value = "보행자 길찾기", notes = "보행자용 길찾기 정보를 제공합니다")

    @GetMapping("/walk")
    public ResponseEntity<?> getWalkRoute(@RequestBody WalkRouteReq response){
        WalkRouteRes walkRouteRes = walkFindRouteService.getWalkRoute(response);

        return BaseResponse.ok(walkRouteRes);
    }

    @ApiOperation(tags = "1. Find Route", value = "대중교통 길찾기", notes = "대중교통 길찾기 정보를 제공합니다")
    @GetMapping("/travel")
    public ResponseEntity<?> getTravelRoute(@RequestBody TravelRouteReq response){
        TravelRouteRes travelRouteReq =travelFindRouteService.getTravelRoute(response);

        return BaseResponse.ok(travelRouteReq);
    }

    @ApiOperation(tags = "1. Find Route", value = "차량 길찾기", notes = "차량용 길찾기 정보를 제공합니다")
    @GetMapping("/car")
    public ResponseEntity<?> getCarRoute(@RequestBody CarRouteReq response){
        CarRouteRes carRouteReq =carFindRouteService.getCarRoute(response);

        return BaseResponse.ok(carRouteReq);
    }
}
