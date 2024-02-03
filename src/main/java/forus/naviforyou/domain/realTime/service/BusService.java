package forus.naviforyou.domain.realTime.service;

import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.HttpURLConnection;

@RequiredArgsConstructor
@Service
public class BusService {

    @Getter
    @Value("${realtime.serviceKey}")
    private String serviceKey;

    public String formalBus(String x,String y){
        String stationId = getStationByName(x,y);

        return "최종";
    }

    public String getStationByName(String x , String y) {

//        double doubleX = Double.parseDouble(x);
//        double doubleY = Double.parseDouble(y);
        double doubleX = 127.05864722222222;
        double doubleY = 37.620194444444444;

        try{
            String SERVICE_KEY = serviceKey;
            String URL = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos";
            StringBuilder urlBuilder = new StringBuilder(URL);
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("tmX", "UTF-8") + "=" + doubleX);
            urlBuilder.append("&" + URLEncoder.encode("tmY", "UTF-8") + "=" + doubleY);
            urlBuilder.append("&" + URLEncoder.encode("radius", "UTF-8") + "=" + 100);

            URL url = new URL(urlBuilder.toString());
            System.out.println(url.toString());

            RestTemplate restTemplate = new RestTemplate();
            RequestEntity<Void> req = RequestEntity
                    .get(url.toURI())
                    .build();
            ResponseEntity<String> result = restTemplate.exchange(req, String.class);

            return parseStationId(result.getBody());


        }catch (Exception e) {
            throw new BaseException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

    }

    public String parseStationId(String xml) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());

        Document document = builder.parse(input);

        NodeList itemListNodes = document.getElementsByTagName("itemList");
        Element firstItemList = (Element) itemListNodes.item(0);

        String stationId = firstItemList.getElementsByTagName("arsId").item(0).getTextContent();

        System.out.println(stationId);
        return stationId;
    }



}
