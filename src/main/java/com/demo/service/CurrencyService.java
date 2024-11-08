package com.demo.service;

import com.demo.entity.CurrencyEntity;
import com.demo.exec.CommonException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface CurrencyService {
    @Transactional
    Page<CurrencyEntity> list(String code, String label, int pageNo, int pageSize);

    @Transactional
    boolean exists(String code);

    @Transactional(rollbackFor = Exception.class)
    void save(String code, String label);

    @Transactional(rollbackFor = Exception.class)
    void delete(String code) throws CommonException;
}
