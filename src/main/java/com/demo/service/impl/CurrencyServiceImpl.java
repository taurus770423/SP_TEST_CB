package com.demo.service.impl;

import com.demo.entity.CurrencyEntity;
import com.demo.exec.CommonException;
import com.demo.reposiroty.CurrencyRepository;
import com.demo.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Override
    @Transactional
    public Page<CurrencyEntity> list(String code, String label, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo-1, pageSize, Sort.Direction.ASC, "code");
        return currencyRepository.findAllByCodeAndLabel(code, label, pageable);
    }

    @Override
    @Transactional
    public boolean exists(String code) {
        return this.currencyRepository.existsById(code);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String code, String label) {
        Optional<CurrencyEntity> currencyOpt = this.currencyRepository.findById(code);
        CurrencyEntity currency = currencyOpt.orElseGet(() -> CurrencyEntity.builder().code(code).build());
        currency.setLabel(label);
        this.currencyRepository.save(currency);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String code) throws CommonException {
        Optional<CurrencyEntity> currencyOpt = this.currencyRepository.findById(code);
        if(currencyOpt.isEmpty()) throw new CommonException("查無幣別資料");
        this.currencyRepository.delete(currencyOpt.get());
    }
}
