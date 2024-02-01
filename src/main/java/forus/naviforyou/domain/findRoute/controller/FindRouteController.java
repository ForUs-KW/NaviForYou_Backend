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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/findRoute")
@RequiredArgsConstructor
public class FindRouteController {

    private final CarFindRouteService carFindRouteService;
    private final WalkFindRouteService walkFindRouteService;
    private final TravelFindRouteService travelFindRouteService;


    @GetMapping("/walk") // walk?stairs=false
    public ResponseEntity<?> getWalkRoute(@RequestParam(name = "stairs", defaultValue = "true") boolean includeStairs , @RequestBody WalkRouteReq response){
        WalkRouteRes walkRouteRes = walkFindRouteService.getWalkRoute(includeStairs,response);
        return BaseResponse.ok(walkRouteRes);
    }

    @GetMapping("/travel")
    public ResponseEntity<?> getTravelRoute(@RequestBody TravelRouteReq response){
        TravelRouteRes travelRouteReq =travelFindRouteService.getTravelRoute(response);

        return BaseResponse.ok(travelRouteReq);
    }

    @GetMapping("/car")
    public ResponseEntity<?> getCarRoute(@RequestBody CarRouteReq response){
        CarRouteRes carRouteReq =carFindRouteService.getCarRoute(response);

        return BaseResponse.ok(carRouteReq);
    }
}
