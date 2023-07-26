package com.brewery.management.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.brewery.management.common.model.CommonModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Table(name = "beers")
@Data
@EqualsAndHashCode(callSuper=false)

public class Beers extends CommonModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long breweryId;
	private String beerName; 
	private float alcoholContent; 
	private double price;
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
