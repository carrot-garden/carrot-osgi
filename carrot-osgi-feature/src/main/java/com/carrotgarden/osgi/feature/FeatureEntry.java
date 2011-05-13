package com.carrotgarden.osgi.feature;

import java.util.Collection;

public class FeatureEntry {

	private String name;

	private String version;

	private Collection<FeatureBundle> bundles;

	//

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Collection<FeatureBundle> getBundles() {
		return bundles;
	}

	public void setBundles(Collection<FeatureBundle> bundles) {
		this.bundles = bundles;
	}

}
