package com.brewery.management.models;

import lombok.Data;

@Data
public class ClientRequest {

	private String wholeSalerName;
	private String beerName;
	private double quote;
	private double beerPrice;
	private double totalAmount;
	private String responseMassage;
	private String discountNotification;

	
}
