package com.adcampign.service.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.adcampign.model.AdCampignRequest;
import com.adcampign.model.AdCampignResponse;
import com.adcampign.service.AdCampignService;
import com.adcampign.util.AdCache;

public class AdCampignServiceTest {

	final static Logger logger = Logger.getLogger(AdCampignServiceTest.class);

	private AdCache adCache;
	private AdCampignService service;
	private AdCampignRequest request;
	private AdCampignRequest requestTwo;

	@Before
	public void setup() {
		adCache = mock(AdCache.class);
		service = new AdCampignService();
		service.setAdCache(adCache);
	}

	@Test
	public void createAd_happyPath() {

		request = new AdCampignRequest();
		request.setAd_content("GYM Fitness Center Ad");
		request.setAdCreationTime(new Date());
		request.setDuration(100);
		request.setPartner_id("100");

		// Unit under test
		Response response = service.createAd(request);

		assertEquals(201, response.getStatus());

	}
	
	@Test
	public void createAd_fail_adAlreadyExistsForPartner() {

		request = new AdCampignRequest();
		request.setAd_content("Govt Welfare Program Ad");
		request.setAdCreationTime(new Date());
		request.setDuration(100);
		request.setPartner_id("100");
		when(adCache.get("100")).thenReturn(request);

		// Unit under test
		Response response = service.createAd(request);

		assertEquals(406, response.getStatus());
	}
	
	@Test
	public void getAdInfo_happyPath() {
		Date date = new Date();
		Calendar calender = Calendar.getInstance();
		calender.setTimeInMillis(date.getTime());
		calender.add(Calendar.SECOND, 100);
		Date adExpiryDate = calender.getTime();

		request = new AdCampignRequest();
		request.setAd_content("CapitalOne Bank CreditCard Ad");
		request.setAdCreationTime(adExpiryDate);
		request.setDuration(100);
		request.setPartner_id("100");

		when(adCache.get("100")).thenReturn(request);

		// Unit under test
		Response response = service.getAdInfo("100");

		assertEquals(200, response.getStatus());
	}

	@Test
	public void getAdInfo_fail_adExpiredNoContentToShow() {

		request = new AdCampignRequest();
		request.setAd_content("CapitalOne Bank CreditCard Ad");
		request.setAdCreationTime(new Date());
		request.setDuration(100);
		request.setPartner_id("100");

		when(adCache.get("100")).thenReturn(request);

		// Unit under test
		Response response = service.getAdInfo("100");

		assertEquals(204, response.getStatus());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void getAllAdsContent_happyPath() {
		Date date = new Date();
		Calendar calender = Calendar.getInstance();
		calender.setTimeInMillis(date.getTime());
		calender.add(Calendar.SECOND, 100);
		Date changeDate = calender.getTime();

		request = new AdCampignRequest();
		request.setAd_content("Apartments Available Ad");
		request.setAdCreationTime(changeDate);
		request.setDuration(100);
		request.setPartner_id("100");

		requestTwo = new AdCampignRequest();
		requestTwo.setAd_content("Thanksgiving Sale Ad");
		requestTwo.setAdCreationTime(changeDate);
		requestTwo.setDuration(200);
		requestTwo.setPartner_id("200");

		List<AdCampignRequest> adCampignRequestList = new ArrayList<AdCampignRequest>();
		Map<String, AdCampignRequest> map = new HashMap<String, AdCampignRequest>();
		map.put("100", request);
		map.put("200", requestTwo);
		adCampignRequestList.add(request);
		adCampignRequestList.add(requestTwo);
		when(adCache.getAdCacheMap()).thenReturn(map);

		// Unit under test
		Response response = service.getAllAdsContent();

		assertTrue(response != null && response.getEntity() != null);
		List<AdCampignResponse> adCampignResponses = (List<AdCampignResponse>) response.getEntity();
		assertEquals(adCampignResponses.size(), 2);

	}

}
