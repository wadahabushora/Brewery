package com.brewery.management.exceptions;

import com.brewery.management.models.Beers;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BeerAlreadyExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381895799350615142L;

	private final Beers beers = new Beers();


	

}
