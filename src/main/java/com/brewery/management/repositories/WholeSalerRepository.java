package com.brewery.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.brewery.management.models.WholeSaler;



@Repository
public interface WholeSalerRepository extends JpaRepository<WholeSaler, Long>, JpaSpecificationExecutor<WholeSaler>{

	WholeSaler findBywholeSalerName(String wholeSalerName);

}
