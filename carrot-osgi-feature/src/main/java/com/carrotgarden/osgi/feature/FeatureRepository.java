package com.carrotgarden.osgi.feature;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;

public class FeatureRepository {

	@JsonProperty("features")
	@JsonManagedReference
	private Collection<FeatureEntry> features;

	//

	public Collection<FeatureEntry> getFeatures() {
		return features;
	}

	public void setFeatures(Collection<FeatureEntry> entries) {
		this.features = entries;
	}

	public FeatureEntry findFeatureByName(String name) {

		if (features == null || name == null) {
			return null;
		}

		for (FeatureEntry entry : features) {
			if (name.equals(entry.getName())) {
				return entry;
			}
		}

		return null;

	}

}
