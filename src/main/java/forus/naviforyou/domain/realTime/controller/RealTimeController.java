package forus.naviforyou.domain.realTime.controller;

import forus.naviforyou.domain.member.dto.request.SignUpReq;
import forus.naviforyou.domain.realTime.dto.request.BusStationReq;
import forus.naviforyou.domain.realTime.dto.response.ItemList;
import forus.naviforyou.domain.realTime.dto.response.ServiceResult;
import forus.naviforyou.domain.realTime.service.BusService;
import forus.naviforyou.global.common.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/app/realTime")
@RequiredArgsConstructor
public class RealTimeController {

    private final BusService busService;

    @ApiOperation(tags = "6. Real Time", value = "실시간 버스 정류장 정보", notes = "정류장 좌표를 통해 실시간 버스 정보를 보여줍니다")
    @GetMapping("/busStation")
    public ResponseEntity<?> getBusInfo(@RequestBody BusStationReq busStationReq) throws UnsupportedEncodingException {
        List<ItemList> stationInfoRes = busService.stationInfo(busStationReq);
        return BaseResponse.ok(stationInfoRes);
    }
}
