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
