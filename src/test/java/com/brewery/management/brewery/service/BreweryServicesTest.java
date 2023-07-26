package com.brewery.management.brewery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.brewery.management.models.Brewery;
import com.brewery.management.repositories.BreweryRepository;
import com.brewery.management.services.BeerServices;
import com.brewery.management.services.BreweryServices;
import com.brewery.management.services.WholeSalerServices;

@ExtendWith(MockitoExtension.class)
 class BreweryServicesTest {

	@Mock
	private BeerServices beerServices;

	@Mock
	WholeSalerServices wholeSalerServices;
	
	@InjectMocks
	private BreweryServices breweryServices;

	@Mock
	BreweryRepository breweryRepository;
	
	@Test
	void testFindByBreweryId() {

		long breweryId = 1;
		
		Brewery brewery = new Brewery();
		brewery.setBreweryName("Test Brewery");
		brewery.setBreweryId(1);
				

		lenient().when(breweryServices.findByBreweryId(breweryId)).thenReturn(brewery);


		Brewery result = breweryRepository.findByBreweryId(breweryId);

		assertEquals(brewery, result);

	}
}
