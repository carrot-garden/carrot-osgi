/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.feature;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonProperty;

public class FeatureBundle {

	@JsonBackReference
	private FeatureEntry parent;

	@JsonProperty("location")
	private String location;

	@JsonProperty("startLevel")
	private int startLevel;

	//

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStartLevel() {
		return startLevel;
	}

	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}

	public FeatureEntry getParent() {
		return parent;
	}

	public void setParent(FeatureEntry parent) {
		this.parent = parent;
	}

}
