/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.feature;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;

public class FeatureEntry {

	@JsonBackReference
	private FeatureRepository parent;

	@JsonProperty("name")
	private String name;

	@JsonProperty("version")
	private String version;

	@JsonManagedReference
	@JsonProperty("bundles")
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

	public FeatureRepository getParent() {
		return parent;
	}

	public void setParent(FeatureRepository parent) {
		this.parent = parent;
	}

}
