/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.osgi.feature.jackson;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.feature.FeatureRepository;
import com.carrotgarden.util.json.JSON;

public class FEATURE {

	private static final Logger log = LoggerFactory.getLogger(FEATURE.class);

	public static FeatureRepository decode(URL jsonURL) {

		try {

			return JSON.fromURL(jsonURL, FeatureRepository.class);

		} catch (Exception e) {
			log.error("", e);
		}

		return null;

	}

	public static String encode(FeatureRepository instance) {

		return JSON.intoText(instance);

	}

}
