package com.carrotgarden.osgi.feature;

import org.codehaus.jackson.annotate.JsonProperty;

public class FeatureRepository {

	@JsonProperty("location")
	private String location;

	//

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
