package com.demo.reposiroty;

import com.demo.entity.CurrencyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String>, JpaSpecificationExecutor<CurrencyEntity> {
    @Query(value = "select c from CurrencyEntity c " +
            "where (:code is null or :code = '' or c.code like %:code%) " +
            "and (:label is null or :label = '' or c.label like %:label%)")
    Page<CurrencyEntity> findAllByCodeAndLabel(String code, String label, Pageable pageable);
}
