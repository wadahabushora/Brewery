package com.brewery.management.models;


import javax.persistence.Entity;
import javax.persistence.Table;
import com.brewery.management.common.model.CommonModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Table(name = "whole_saler")
@Data
@EqualsAndHashCode(callSuper=false)
public class WholeSaler extends CommonModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String wholeSalerName; 
	private String beerName; 
	private double stock;
	
	
	
	
	@Override
	protected void init() {
		//review		
	}
	@Override
	protected boolean isValid() {
		return false;
	}
	@Override
	protected void clean() {
		//review
	}
	
	
	
	
}
