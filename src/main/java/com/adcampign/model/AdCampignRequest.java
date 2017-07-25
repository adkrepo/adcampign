package com.adcampign.model;

import java.util.Date;

public class AdCampignRequest {

	private int duration;
	
	private String partner_id;

	private String ad_content;

	private Date adCreationTime;

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

	public Date getAdCreationTime() {
		return adCreationTime;
	}

	public void setAdCreationTime(Date adCreationTime) {
		this.adCreationTime = adCreationTime;
	}

}
