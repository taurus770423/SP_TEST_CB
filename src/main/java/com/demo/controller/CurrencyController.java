package com.demo.controller;

import com.demo.entity.CurrencyEntity;
import com.demo.exec.CommonException;
import com.demo.model.CurrencyListModel;
import com.demo.model.ResponseModel;
import com.demo.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @PostMapping("/list")
    public ResponseEntity<ResponseModel<CurrencyListModel.Response>> list(@RequestBody CurrencyListModel.Request request) {
        Page<CurrencyEntity> currencyPage = this.currencyService.list(request.getCode(), request.getLabel(), request.getPageNo(), request.getPageSize());
        CurrencyListModel.Response response = CurrencyListModel.Response.builder()
            .totalPages(currencyPage.getTotalPages())
            .currencyList(
                currencyPage.get().map(currency ->
                    CurrencyListModel.Currency.builder()
                        .code(currency.getCode())
                        .label(currency.getLabel())
                        .createTime(currency.getCreateTime())
                        .modifyTime(currency.getModifyTime())
                        .build()
                ).toList()
            ).build();
        return ResponseEntity.ok(ResponseModel.<CurrencyListModel.Response>builder().data(response).build());
    }

    @PostMapping("/exists")
    public ResponseEntity<ResponseModel<Boolean>> exists(@RequestBody CurrencyListModel.Request request) {
        boolean result = this.currencyService.exists(request.getCode());
        return ResponseEntity.ok(ResponseModel.<Boolean>builder().data(result).build());
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseModel<Boolean>> save(@RequestBody CurrencyListModel.Request request) {
        this.currencyService.save(request.getCode(), request.getLabel());
        return ResponseEntity.ok(ResponseModel.<Boolean>builder().data(true).build());
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseModel<Boolean>> delete(@RequestBody CurrencyListModel.Request request) throws CommonException {
        this.currencyService.delete(request.getCode());
        return ResponseEntity.ok(ResponseModel.<Boolean>builder().data(true).build());
    }
}
