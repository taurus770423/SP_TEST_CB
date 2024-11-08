package com.demo.service;

import com.demo.exec.CommonException;
import com.demo.model.CoinDetailsModel;

public interface CoindeskService {
    CoinDetailsModel getDetailsModel() throws CommonException;
}
