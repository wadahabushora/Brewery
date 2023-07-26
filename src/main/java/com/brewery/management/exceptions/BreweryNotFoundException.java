package com.brewery.management.exceptions;

import com.brewery.management.models.Brewery;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class BreweryNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381895799350615142L;

	private final Brewery brewery = new Brewery();

	
	

}
