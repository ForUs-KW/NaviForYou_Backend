package forus.naviforyou.domain.findRoute.controller;


import forus.naviforyou.domain.findRoute.dto.request.WalkRouteReq;
import forus.naviforyou.domain.findRoute.service.FindRouteService;
import forus.naviforyou.global.common.BaseResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/findRoute")
@RequiredArgsConstructor
public class FindRouteController {

    private final FindRouteService findRouteService;

    @GetMapping("/walk")
    public ResponseEntity<?> getWalkRoute(@RequestBody WalkRouteReq response){
        findRouteService.getWalkRoute(response);

        return BaseResponse.ok("작업중 보행자 루트");
    }
}
