package com.brewery.management.models;

import lombok.Data;

@Data
public class AddBeer{

	private long breweryId;
	private String beerName; 
	private float alcoholContent; 
	private double price;
	private String responseMassege;
	
	
	
	
}
