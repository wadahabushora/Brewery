package com.brewery.management.exceptions;

import com.brewery.management.models.WholeSaler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class OrderGreaterThanStockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381895799350615142L;

	private final WholeSaler wholeSaler = new WholeSaler();

	
}
