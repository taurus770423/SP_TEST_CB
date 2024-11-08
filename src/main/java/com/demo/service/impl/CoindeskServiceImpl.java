package com.demo.service.impl;

import com.demo.entity.CurrencyEntity;
import com.demo.exec.CommonException;
import com.demo.model.CoinDetailsModel;
import com.demo.model.CoindeskGetModel;
import com.demo.reposiroty.CurrencyRepository;
import com.demo.service.CoindeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoindeskServiceImpl implements CoindeskService {
    private final CurrencyRepository currencyRepository;

    @Override
    public CoinDetailsModel getDetailsModel() throws CommonException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CoindeskGetModel> response = restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice.json", CoindeskGetModel.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            CoindeskGetModel body = response.getBody();

            if(ObjectUtils.isEmpty(body)) throw new CommonException("查無資料");

            OffsetDateTime offsetDateTime = OffsetDateTime.parse(body.getTime().getUpdatedISO());
            LocalDateTime updateTime = offsetDateTime.toLocalDateTime();
            LocalDateTime updateTimeTW = offsetDateTime.atZoneSameInstant(ZoneId.of("Asia/Taipei")).toLocalDateTime();

                    List<CoinDetailsModel.CoinDetails> detailsList = new ArrayList<>();
            body.getBpi().forEach((key, value) -> {
                detailsList.add(
                    CoinDetailsModel.CoinDetails.builder()
                        .code(key)
                        .label(this.getCoinLabel(key))
                        .rate(value.getRateFloat())
                        .build()
                );
            });

            return CoinDetailsModel.builder()
                .chartName(body.getChartName())
                .updateTime(updateTime)
                .updateTimeTW(updateTimeTW)
                .detailsList(detailsList)
                .build();
        }
        return null;
    }

    private String getCoinLabel(String code) {
        Optional<CurrencyEntity> currency = this.currencyRepository.findById(code);
        return currency.map(CurrencyEntity::getLabel).orElse(null);
    }
}
