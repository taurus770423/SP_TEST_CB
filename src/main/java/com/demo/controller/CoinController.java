package com.demo.controller;

import com.demo.exec.CommonException;
import com.demo.model.CoinDetailsModel;
import com.demo.model.ResponseModel;
import com.demo.service.CoindeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coin")
public class CoinController {
    private final CoindeskService coindeskService;

    @GetMapping("/details")
    public ResponseEntity<ResponseModel<CoinDetailsModel>> getCoinDetails() throws CommonException {
        CoinDetailsModel details = this.coindeskService.getDetailsModel();
        return ResponseEntity.ok(ResponseModel.<CoinDetailsModel>builder().data(details).build());
    }
}
