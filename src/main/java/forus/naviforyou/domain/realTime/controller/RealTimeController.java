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
        List<ItemList> stationInfoRes = busService.stationInfo(busStationReq,false);
        return BaseResponse.ok(stationInfoRes);
    }

    @ApiOperation(tags = "6. Real Time", value = "실시간 버스 정보 ", notes = "정류장 좌표와 버스 번호를 통해 원하는 버스 실시간 정보를 보여줍니다")
    @GetMapping("/busStation/detail")
    public ResponseEntity<?> getBusInfoDetail(@RequestParam String busXum , @RequestBody BusStationReq busStationReq) throws UnsupportedEncodingException {
        List<ItemList> stationInfoRes = busService.stationInfo(busStationReq,false);
        List<ItemList> stationDetailInfoRes = busService.filterBusInfoList(stationInfoRes,busXum);

        return BaseResponse.ok(stationDetailInfoRes);
    }

    @ApiOperation(tags = "6. Real Time", value = "실시간 저상버스 정류장 정보", notes = "정류장 좌표를 통해 실시간 저상 버스 정보를 보여줍니다")
    @GetMapping("/lowBusStation")
    public ResponseEntity<?> getLowBusInfo(@RequestBody BusStationReq busStationReq) throws UnsupportedEncodingException {
        List<ItemList> stationInfoRes = busService.stationInfo(busStationReq,true);
        return BaseResponse.ok(stationInfoRes);
    }

    @ApiOperation(tags = "6. Real Time", value = "실시간 저상버스 정보 ", notes = "정류장 좌표와 버스 번호를 통해 원하는 저상버스 실시간 정보를 보여줍니다")
    @GetMapping("/lowBusStation/detail")
    public ResponseEntity<?> getLowBusInfoDetail(@RequestParam String busXum , @RequestBody BusStationReq busStationReq) throws UnsupportedEncodingException {
        List<ItemList> stationInfoRes = busService.stationInfo(busStationReq,true);
        List<ItemList> stationDetailInfoRes = busService.filterBusInfoList(stationInfoRes,busXum);

        return BaseResponse.ok(stationDetailInfoRes);
    }

}
