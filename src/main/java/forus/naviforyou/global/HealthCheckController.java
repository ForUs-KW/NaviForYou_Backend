package forus.naviforyou.global;

import forus.naviforyou.global.common.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/healthCheck")
    public ResponseEntity<?> healthCheck(){
        return BaseResponse.ok("ok");
    }
}
