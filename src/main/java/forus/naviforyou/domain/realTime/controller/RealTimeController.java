package forus.naviforyou.domain.realTime.controller;

import forus.naviforyou.domain.member.dto.request.SignUpReq;
import forus.naviforyou.domain.realTime.service.BusService;
import forus.naviforyou.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/app/realTime")
@RequiredArgsConstructor
public class RealTimeController {

    private final BusService busService;

    @GetMapping("/bus")
    public ResponseEntity<?> getBusInfo() throws UnsupportedEncodingException {
        busService.formalBus("a");
        return BaseResponse.ok("회원가입 성공");
    }
}
