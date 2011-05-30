package com.carrotgarden.osgi.feature.jackson;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.osgi.feature.FeatureRepository;
import com.carrotgarden.osgi.feature.FeatureEntry;
import com.carrotgarden.utils.json.JSON;

public class TestFEATURE {

	private static final Logger log = LoggerFactory
			.getLogger(TestFEATURE.class);

	@Test
	public void testDecode() {

		URL jsonURL = getClass().getResource("/feature-collection.json");

		FeatureRepository result = FEATURE.decode(jsonURL);

		assertTrue(true);

	}

	@Test
	public void testEncode() {

		URL jsonURL = getClass().getResource("/feature-collection.json");

		FeatureRepository result = FEATURE.decode(jsonURL);

		String json = FEATURE.encode(result);

		log.info("json : {}", json);

		assertTrue(true);

	}

	@Test
	public void testFind() {

		URL jsonURL = getClass().getResource("/feature-collection.json");

		FeatureRepository result = FEATURE.decode(jsonURL);

		FeatureEntry feature = result.findFeatureByName("osgi-core");

		log.info("json : {}", JSON.intoText(feature));

		assertTrue(true);

	}

}
