package com.bluescript.demo.jpa;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.bluescript.demo.entity.CustomerSecureEntity;

public interface IInsertCustomerSecureJpa extends JpaRepository<CustomerSecureEntity, Integer> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "INSERT INTO CUSTOMER_SECURE ( CUSTOMERNUMBER , CUSTOMERPASS , STATE_INDICATOR , PASS_CHANGES ) VALUES ( :db2CustomernumInt , :d2CustsecrPass , :d2CustsecrState , :db2CustomercntInt )", nativeQuery = true)
    void insertCustomerSecureForDb2CustomernumIntAndD2CustsecrPassAndD2CustsecrState(
            @Param("db2CustomernumInt") int db2CustomernumInt, @Param("d2CustsecrPass") String d2CustsecrPass,
            @Param("d2CustsecrState") String d2CustsecrState, @Param("db2CustomercntInt") int db2CustomercntInt);
}
