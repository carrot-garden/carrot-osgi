/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package bench.question;

import java.util.Map;

import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CompMangerProvider implements CompManager {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private ComponentFactory factory;

	@Reference( //
	target = "(component.factory=bench.*)", //
	cardinality = ReferenceCardinality.MULTIPLE, //
	policy = ReferencePolicy.DYNAMIC //
	)
	protected void bind(final ComponentFactory s,
			final Map<String, Object> props) {

		factory = s;

		/** this is in the osgi spec */
		log.info("bind component.factory : {}", props.get("component.factory"));
		log.info("bind component.name    : {}", props.get("component.name"));

		/** this is NOT in the osgi spec, WHY? */
		log.info("bind custom-value      : {}", props.get("custom-value"));

	}

	protected void unbind(final ComponentFactory s) {

		log.info("unbind : {}", s);

		factory = null;

	}

}
