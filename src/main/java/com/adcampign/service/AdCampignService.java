package com.adcampign.service;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.adcampign.model.AdCampignRequest;
import com.adcampign.model.AdCampignResponse;
import com.adcampign.util.AdCache;
import com.adcampign.util.AdCampignConstants;

/**
 * REST service to create ad campaign and get ad(for one partner or all
 * partners) information
 * 
 */
@Path("/ad")
public class AdCampignService {

	final static Logger logger = Logger.getLogger(AdCampignService.class);
	private AdCache adCache = AdCache.getInstance();

	public void setAdCache(AdCache adCache) {
		this.adCache = adCache;
	}

	/**
	 * Create only one Ad Campaign for a given partner
	 * 
	 * @param adcampign
	 *            AdCampign request data
	 * @return Response Ad Status either ad successfully created or Not
	 *         acceptable to create new ad
	 */
	@POST
	@Consumes("application/json")
	public Response createAd(AdCampignRequest adcampign) {
		adcampign.setAdCreationTime(addAdDurationToCreationDate(adcampign.getDuration()));
		if (adCache != null && adCache.get(adcampign.getPartner_id()) != null) {
			Long duration = getRemainingAdDuration(adcampign.getPartner_id());
			if (duration >= 0) {
				return Response.status(Status.NOT_ACCEPTABLE).entity(AdCampignConstants.ONLYONE_AD_EXISTS_FOR_PARTNER)
						.build();
			}
		}
		adCache.put(adcampign.getPartner_id(), adcampign);
		logger.debug("Ad Status:" + AdCampignConstants.AD_CREATED_SUCCESS);
		logger.debug("Total number of Ads exists:" + adCache.size());
		return Response.status(201).entity(AdCampignConstants.AD_CREATED_SUCCESS).build();

	}

	/**
	 * Fetch Ad Campaign for a Partner If Ad not expired
	 * 
	 * @param id
	 *            partner id
	 * @return Response Displays Ad Info if ad not expired otherwise no content
	 *         displayed
	 */
	@GET
	@Path("{partner_id}")
	@Produces("application/json")
	public Response getAdInfo(@PathParam("partner_id") String id) {
		Response response = null;
		if (adCache != null && adCache.get(id) != null) {
			Long adDurationToExpire = getRemainingAdDuration(id);
			if (adDurationToExpire <= 0) {
				// No active ad campaigns exist for the given partner
				response = Response.status(Status.NO_CONTENT).build();
			} else {
				// Display Ad content for the given partner
				response = Response.status(200).entity(getAdCampignResponse(adCache.get(id))).build();
			}
		}
		return response;

	}

	/**
	 * Get list of all campaigns for all partners
	 * 
	 * @param
	 * @return List list of all campaigns
	 */
	@GET
	@Path("/all")
	@Produces("application/json")
	public Response getAllAdsContent() {
		List<AdCampignResponse> allAds = new ArrayList<AdCampignResponse>();
		if (adCache.getAdCacheMap() != null) {
			for (AdCampignRequest ad : adCache.getAdCacheMap().values()) {
				if (ad != null) {
					allAds.add(getAdCampignResponse(ad));
				}
			}
		}
		return Response.status(200).entity(allAds).build();
	}

	/**
	 * Get the ad expiry date by adding ad duration with ad creation date
	 * 
	 * @param sec
	 *            ad duration in seconds
	 * @return Date ad expiry date
	 */
	private Date addAdDurationToCreationDate(int sec) {
		Date date = new Date();
		Calendar calender = Calendar.getInstance();
		calender.setTimeInMillis(date.getTime());
		calender.add(Calendar.SECOND, sec);
		Date changeDate = calender.getTime();
		return changeDate;
	}

	/**
	 * Get the time difference between adExpiryDate and Current Date in seconds
	 * 
	 * @param partnerId
	 *            partner id
	 * @return Long time difference in seconds
	 */
	private Long getRemainingAdDuration(String partnerId) {
		Date date = new Date();
		Long duration = adCache.get(partnerId).getAdCreationTime() != null
				? (adCache.get(partnerId).getAdCreationTime().getTime() - date.getTime()) / 1000 : 0L;
		return duration;
	}

	/**
	 * Prepare the response AdCampignResponse from AdCampignRequest
	 * 
	 * @param ad
	 *            adCampign request
	 * @return AdCampignResponse response
	 */
	private AdCampignResponse getAdCampignResponse(AdCampignRequest ad) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		AdCampignResponse adCampignResponse = new AdCampignResponse();
		String adExpiryDateStr = dateFormat.format(ad.getAdCreationTime());
		adCampignResponse.setAd_content(ad.getAd_content());
		adCampignResponse.setAdExpiryDate(adExpiryDateStr);
		adCampignResponse.setDuration(ad.getDuration());
		adCampignResponse.setPartner_id(ad.getPartner_id());
		return adCampignResponse;
	}
}