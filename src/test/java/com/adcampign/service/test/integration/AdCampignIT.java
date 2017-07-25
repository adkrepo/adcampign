package com.adcampign.service.test.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;

public class AdCampignIT {

	final static Logger logger = Logger.getLogger(AdCampignIT.class);

	@BeforeClass
	public static void setup() {
		String port = System.getProperty("server.port");
		if (port == null) {
			RestAssured.port = Integer.valueOf(8080);
		} else {
			RestAssured.port = Integer.valueOf(port);
		}

		String basePath = System.getProperty("server.base");
		if (basePath == null) {
			basePath = "/";
		}
		RestAssured.basePath = basePath;

		String baseHost = System.getProperty("server.host");
		if (baseHost == null) {
			baseHost = "http://localhost";
		}
		RestAssured.baseURI = baseHost;

	}

	@Test
	public void getAd_success() {
		String partnerId = getRandomPartnerId();
		String adContent = getRandomAdContent();
		Map<String, String> adMap = new HashMap<>();
		adMap.put("partner_id", partnerId);
		adMap.put("duration", "600");
		adMap.put("ad_content", adContent);

		given().contentType("application/json").body(adMap).when().post("/ad").then().statusCode(201);

		given().when().get("/ad/" + partnerId).then().statusCode(200);
	}
	
	@Test
	public void getAd_fail_adExpired() {
		String partnerId = getRandomPartnerId();
		String adContent = getRandomAdContent();
		Map<String, String> adMap = new HashMap<>();
		adMap.put("partner_id", partnerId);
		adMap.put("duration", "1");
		adMap.put("ad_content", adContent);

		given().contentType("application/json").body(adMap).when().post("/ad").then().statusCode(201);

		given().when().get("/ad/" + partnerId).then().statusCode(204);
	}

	@Test
	public void createAd_success() {
		String partnerId = getRandomPartnerId();
		String adContent = getRandomAdContent();
		Map<String, String> adMap = new HashMap<>();
		adMap.put("partner_id", partnerId);
		adMap.put("duration", "100");
		adMap.put("ad_content", adContent);

		String response = given().contentType("application/json").body(adMap).when().post("/ad").asString();
		logger.debug("Ad Status:"+response);
		assertEquals("Succesfully adcampign created", response);
	}
	
	@Test
	public void createAd_fail_adAlreadyExistsForPartner() {
		String partnerId = getRandomPartnerId();
		String adContent = getRandomAdContent();
		Map<String, String> adMap = new HashMap<>();
		adMap.put("partner_id", partnerId);
		adMap.put("duration", "6000");
		adMap.put("ad_content", adContent);

		String response = given().contentType("application/json").body(adMap).when().post("/ad").asString();
		logger.debug("Ad Status:"+response);
		assertEquals("Succesfully adcampign created", response);
		
		adMap = new HashMap<>();
		String adContentForSecondAd = getRandomAdContent();
		adMap.put("partner_id", partnerId);
		adMap.put("duration", "60");
		adMap.put("ad_content", adContentForSecondAd);

		String responseForSecondAd = given().contentType("application/json").body(adMap).when().post("/ad").asString();
		logger.debug("Ad Status:"+responseForSecondAd);
		assertEquals("Only one active campaign can exist for a given partner", responseForSecondAd);
	}
	
	@Test
	public void getAllAdsContent_success() {
		given().when().get("/ad/all").then().statusCode(200);
	}

	private String getRandomPartnerId() {
		Random randomGenerator = new Random();
		int randomPartnerId = 0;
		randomPartnerId = randomGenerator.nextInt(1000);
		return String.valueOf(randomPartnerId);
	}

	private String getRandomAdContent() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
			buffer.append((char) randomLimitedInt);
		}
		String generatedAdContent = buffer.toString();
		logger.debug("AdContent generated randomly:" + generatedAdContent);
		return generatedAdContent;
	}
}
