package com.brewery.management.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class AllBreweryBeers{
	
	private long breweryId; 
	private String breweryName;
	@JsonIgnoreProperties({"systemId","version","status","notes","dateCreated","dateUpdated","breweryId"})
	private List<Beers> beers;
	private String responseMassage;
	

	
	
	

}
