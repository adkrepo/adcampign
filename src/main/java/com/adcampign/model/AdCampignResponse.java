package com.adcampign.model;

public class AdCampignResponse {

	private int duration;

	private String partner_id;

	private String ad_content;

	private String adExpiryDate;

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getAd_content() {
		return ad_content;
	}

	public void setAd_content(String ad_content) {
		this.ad_content = ad_content;
	}

	public String getAdExpiryDate() {
		return adExpiryDate;
	}

	public void setAdExpiryDate(String adExpiryDate) {
		this.adExpiryDate = adExpiryDate;
	}

}
