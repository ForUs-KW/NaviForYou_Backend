package forus.naviforyou.domain.realTime.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import forus.naviforyou.domain.findRoute.dto.response.TravelRouteRes;
import forus.naviforyou.domain.realTime.dto.request.BusStationReq;
import forus.naviforyou.domain.realTime.dto.response.ItemList;
import forus.naviforyou.domain.realTime.dto.response.ServiceResult;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BusService {

    @Getter
    @Value("${realtime.serviceKey}")
    private String serviceKey;

    public List<ItemList> stationInfo(BusStationReq busStationReq , Boolean lowStation){

        double doubleX = Double.parseDouble(busStationReq.getX());
        double doubleY = Double.parseDouble(busStationReq.getY());
        String stationId = getStationByName(doubleX,doubleY);


        if(lowStation){
            return getLowStationByUidItem(stationId);
        }
       return getStationByUidItem(stationId);

    }


    public List<ItemList> filterBusInfoList(List<ItemList> busInfoList, String fullBusNum) {

        String busNum = fullBusNum.contains(":") ? fullBusNum.substring(fullBusNum.indexOf(":") + 1) : fullBusNum;

        return busInfoList.stream()
                .filter(item -> busNum.equals(item.getBusRouteAbrv()))
                .collect(Collectors.toList());
    }

    public String getStationByName(double doubleX , double doubleY) {

        try{
            String SERVICE_KEY = serviceKey;
            String URL = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos";
            StringBuilder urlBuilder = new StringBuilder(URL);
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("tmX", "UTF-8") + "=" + doubleX);
            urlBuilder.append("&" + URLEncoder.encode("tmY", "UTF-8") + "=" + doubleY);
            urlBuilder.append("&" + URLEncoder.encode("radius", "UTF-8") + "=" + 100);

            URL url = new URL(urlBuilder.toString());

            RestTemplate restTemplate = new RestTemplate();
            RequestEntity<Void> req = RequestEntity
                    .get(url.toURI())
                    .build();
            ResponseEntity<String> result = restTemplate.exchange(req, String.class);

            return parseStationId(result.getBody());
        }catch (Exception e) {
            throw new BaseException(ErrorCode.NO_MAPPING_STATION_NUM);
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

        if(stationId.equals("0")){
            throw new BaseException(ErrorCode.NO_MAPPING_STATION_NUM);
        }

        return stationId;
    }

    public List<ItemList> getStationByUidItem(String stationId){
        try{
            String SERVICE_KEY = serviceKey;
            StringBuilder urlBuilder = new StringBuilder("http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("arsId", "UTF-8") + "=" + URLEncoder.encode(stationId, "UTF-8"));


            URL url = new URL(urlBuilder.toString());

            RestTemplate restTemplate = new RestTemplate();
            RequestEntity<Void> req = RequestEntity
                    .get(url.toURI())
                    .build();
            ResponseEntity<String> result = restTemplate.exchange(req, String.class);


            return parseXmlToJson(result.getBody());


        }catch (Exception e) {
            throw new BaseException(ErrorCode.NO_MAPPING_STATION_INFO);
        }
    }

    public List<ItemList> getLowStationByUidItem(String stationId){
        try{
            String SERVICE_KEY = serviceKey;
            StringBuilder urlBuilder = new StringBuilder("http://ws.bus.go.kr/api/rest/stationinfo/getLowStationByUid");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("arsId", "UTF-8") + "=" + URLEncoder.encode(stationId, "UTF-8"));


            URL url = new URL(urlBuilder.toString());

            RestTemplate restTemplate = new RestTemplate();
            RequestEntity<Void> req = RequestEntity
                    .get(url.toURI())
                    .build();
            ResponseEntity<String> result = restTemplate.exchange(req, String.class);


            return parseXmlToJson(result.getBody());


        }catch (Exception e) {
            throw new BaseException(ErrorCode.NO_MAPPING_STATION_INFO);
        }
    }



    public  List<ItemList> parseXmlToJson(String xml){

        JSONObject json = XML.toJSONObject(xml);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json.toString());

            JsonNode itemListNode = rootNode
                    .path("ServiceResult")
                    .path("msgBody")
                    .path("itemList");

            if (itemListNode.isArray()) {
                List<ItemList> busInfoList = objectMapper.readValue(itemListNode.toString(), new TypeReference<List<ItemList>>() {});
                return busInfoList;
            } else if (itemListNode.isObject()) {
                ItemList busInfoItem = objectMapper.readValue(itemListNode.toString(), ItemList.class);
                return Collections.singletonList(busInfoItem);
            }


        } catch (Exception e) {
            throw new BaseException(ErrorCode.NO_MAPPING_ROUTE);
        }
        return null;

    }



}
