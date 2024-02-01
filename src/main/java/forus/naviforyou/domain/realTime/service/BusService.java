package forus.naviforyou.domain.realTime.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RequiredArgsConstructor
@Service
public class BusService {

    @Getter
    @Value("${realtime.serviceKey}")
    private String serviceKey;

    public String formalBus(String registrationId) throws UnsupportedEncodingException {
        String SERVICE_KEY = serviceKey;
        String URL = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid";
        StringBuilder urlBuilder = new StringBuilder(URL);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8")+"="+SERVICE_KEY);
        urlBuilder.append("&" + URLEncoder.encode("arsId", "UTF-8") + "=" + URLEncoder.encode("arsID값", "UTF-8"));

        System.out.println(urlBuilder.toString());

        return "a";
    }
}
