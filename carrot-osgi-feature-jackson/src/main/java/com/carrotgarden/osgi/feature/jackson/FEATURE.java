package com.carrotgarden.osgi.feature.jackson;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.feature.FeatureCollection;
import com.carrotgarden.utils.json.JSON;

public class FEATURE {

	private static final Logger log = LoggerFactory.getLogger(FEATURE.class);

	public static FeatureCollection decode(URL jsonURL) {

		try {

			return JSON.fromURL(jsonURL, FeatureCollection.class);

		} catch (Exception e) {
			log.error("", e);
		}

		return null;

	}

	public static String encode(FeatureCollection instance) {

		return JSON.intoText(instance);

	}

}
