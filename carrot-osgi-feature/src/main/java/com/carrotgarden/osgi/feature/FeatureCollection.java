package com.carrotgarden.osgi.feature;

import java.util.Collection;

public class FeatureCollection {

	public static final String PROP_ENTRIES = "entries";

	private Collection<FeatureEntry> entries;

	//

	public Collection<FeatureEntry> getEntries() {
		return entries;
	}

	public void setEntries(Collection<FeatureEntry> entries) {
		this.entries = entries;
	}

}
