package com.carrotgarden.osgi.feature;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;

public class FeatureCollection {

	@JsonProperty("entries")
	@JsonManagedReference
	private Collection<FeatureEntry> entries;

	//

	public Collection<FeatureEntry> getEntries() {
		return entries;
	}

	public void setEntries(Collection<FeatureEntry> entries) {
		this.entries = entries;
	}

}
