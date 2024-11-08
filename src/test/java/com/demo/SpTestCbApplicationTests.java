package com.demo;

import com.demo.entity.CurrencyEntity;
import com.demo.exec.CommonException;
import com.demo.model.CoindeskGetModel;
import com.demo.model.CurrencyListModel;
import com.demo.model.ResponseModel;
import com.demo.reposiroty.CurrencyRepository;
import com.demo.service.CoindeskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpTestCbApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void test1() throws CommonException, JsonProcessingException {
        System.out.println();
        System.out.println("### 1.測試呼叫查詢幣別對應表資料API，並顯示其內容。 ###");

        String url = "http://localhost:8080/currency/list";
        String body = "{\"code\":null,\"label\":null,\"pageNo\":1,\"pageSize\":5}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("Request: " + body);
        System.out.println("Response: " + response.getBody());
        System.out.println();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        JsonNode root = objectMapper.readTree(response.getBody());

        //伺服器剛啟動必定為 3 筆資料
        Assert.isTrue(root.get("data").get("currencyList").size() == 3, "資料數量不正確");
    }

    @Test
    void test2() throws CommonException {
        System.out.println();
        System.out.println("### 2.測試呼叫新增幣別對應表資料API。 ###");
        String url = "http://localhost:8080/currency/save";
        String body = "{\"code\":\"JPY\",\"label\":\"日元\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("Request: " + body);
        System.out.println("Response: " + response.getBody());
        System.out.println();

        url = "http://localhost:8080/currency/exists";
        body = "{\"code\":\"JPY\",\"label\":null,\"pageNo\":null,\"pageSize\":null}";

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        request = new HttpEntity<>(body, headers);
        ResponseEntity<ResponseModel> model = restTemplate.postForEntity(url, request, ResponseModel.class);

        System.out.println("Request: " + body);
        System.out.println("Response: " + response.getBody());
        System.out.println();

        Assert.isTrue((Boolean) model.getBody().getData(), "JPY 新增失敗");
    }

    @Test
    void test3() throws CommonException, JsonProcessingException {
        System.out.println();
        System.out.println("### 3.測試呼叫更新幣別對應表資料API，並顯示其內容。 ###");

        String url = "http://localhost:8080/currency/save";
        String body = "{\"code\":\"JPY\",\"label\":\"日幣\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("Request: " + body);
        System.out.println("Response: " + response.getBody());
        System.out.println();

        url = "http://localhost:8080/currency/list";
        body = "{\"code\":\"JPY\",\"label\":null,\"pageNo\":1,\"pageSize\":1}";

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        request = new HttpEntity<>(body, headers);
        response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("Request: " + body);
        System.out.println("Response: " + response.getBody());
        System.out.println();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        JsonNode root = objectMapper.readTree(response.getBody());

        //檢查資料是否正確更新
        Assert.isTrue(root.get("data").get("currencyList").get(0).get("label").asText().equals("日幣"), "更新失敗");
    }

    @Test
    void test4() throws CommonException, JsonProcessingException {
        System.out.println();
        System.out.println("### 4.測試呼叫刪除幣別對應表資料API。 ###");

        String url = "http://localhost:8080/currency/delete";
        String body = "{\"code\":\"JPY\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("Request: " + body);
        System.out.println("Response: " + response.getBody());
        System.out.println();

        url = "http://localhost:8080/currency/exists";
        body = "{\"code\":\"JPY\"}";

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        request = new HttpEntity<>(body, headers);
        ResponseEntity<ResponseModel> model = restTemplate.postForEntity(url, request, ResponseModel.class);

        System.out.println("Request: " + body);
        System.out.println("Response: " + model.getBody());
        System.out.println();

        Assert.isTrue(!((Boolean) model.getBody().getData()), "JPY 刪除失敗");

    }

    @Test
    void test5() throws CommonException, JsonProcessingException {
        System.out.println();
        System.out.println("### 5. 測試呼叫 coindesk API，並顯示其內容。 ###");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CoindeskGetModel> response = restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice.json", CoindeskGetModel.class);
        System.out.println("Response: " + response.getBody());
        System.out.println();
    }

    @Test
    void test6() throws CommonException, JsonProcessingException {
        System.out.println();
        System.out.println("### 6. 測試呼叫資料轉換的API，並顯示其內容。 ###");

        String url = "http://localhost:8080/coin/details";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        System.out.println("Response: " + response.getBody());
        System.out.println();
    }
}
