package com.carrotgarden.osgi.feature.pivot;

import java.io.InputStream;
import java.net.URL;

import org.apache.pivot.collections.Map;
import org.apache.pivot.json.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.feature.FeatureRepository;

public class FEATURE {

	private static final Logger log = LoggerFactory.getLogger(FEATURE.class);

	public static FeatureRepository decode(URL jsonURL) {

		try {

			InputStream input = jsonURL.openStream();

			JSONSerializer codec = new JSONSerializer();

			Map<String, ?> map = (Map<String, ?>) codec.readObject(input);

			log.info("map : {}", map);

			FeatureRepository result = new FeatureRepository();

			return result;

		} catch (Exception e) {
			log.error("", e);
		}

		return null;

	}

	public static String encode(FeatureRepository instance) {

		// JSONSerializer codec = new JSONSerializer();
		// codec.setAlwaysDelimitMapKeys(true);

		try {
			return JSONSerializer.toString(instance, true);
		} catch (Exception e) {
			log.error("", e);
		}

		return null;

	}

}
