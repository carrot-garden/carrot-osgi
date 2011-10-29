package com.carrotgarden.osgi.anno.scr.visit;

import com.carrotgarden.osgi.anno.scr.bean.AggregatorBean;
import com.carrotgarden.osgi.anno.scr.bean.ComponentBean;
import com.carrotgarden.osgi.anno.scr.bean.ImplementationBean;
import com.carrotgarden.osgi.anno.scr.bean.PropertyBean;
import com.carrotgarden.osgi.anno.scr.bean.ProvideBean;
import com.carrotgarden.osgi.anno.scr.bean.ReferenceBean;
import com.carrotgarden.osgi.anno.scr.bean.ServiceBean;

public interface BeanVisitor {

	void visit(AggregatorBean aggregator);

	void visit(ComponentBean component);

	void visit(ImplementationBean implementation);

	void visit(PropertyBean property);

	void visit(ProvideBean provide);

	void visit(ReferenceBean reference);

	void visit(ServiceBean service);

	//

	class Adapter implements BeanVisitor {

		@Override
		public void visit(final AggregatorBean aggregator) {
		}

		@Override
		public void visit(final ComponentBean component) {
		}

		@Override
		public void visit(final ImplementationBean implementation) {
		}

		@Override
		public void visit(final PropertyBean provide) {
		}

		@Override
		public void visit(final ProvideBean provide) {
		}

		@Override
		public void visit(final ReferenceBean reference) {
		}

		@Override
		public void visit(final ServiceBean service) {
		}

	}

}
