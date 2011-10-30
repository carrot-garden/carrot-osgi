package com.carrotgarden.osgi.anno.scr.bean;

import java.util.Set;
import java.util.TreeSet;

import org.osgi.service.component.annotations.ConfigurationPolicy;

import com.carrotgarden.osgi.anno.scr.conv.ConfigurationPolicyConverter;
import com.carrotgarden.osgi.anno.scr.visit.BeanAcceptor;
import com.carrotgarden.osgi.anno.scr.visit.BeanVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("component")
public class ComponentBean implements BeanAcceptor {

	@XStreamAsAttribute
	@XStreamAlias("name")
	public String name;

	@XStreamAsAttribute
	@XStreamAlias("enabled")
	public boolean enabled = true;

	@XStreamAsAttribute
	@XStreamAlias("factory")
	public String factory;

	@XStreamAsAttribute
	@XStreamAlias("immediate")
	public boolean immediate = false;

	@XStreamAsAttribute
	@XStreamAlias("configuration-policy")
	@XStreamConverter(ConfigurationPolicyConverter.class)
	public ConfigurationPolicy policy;

	//

	@XStreamAsAttribute
	@XStreamAlias("activate")
	public String activate;

	@XStreamAsAttribute
	@XStreamAlias("deactivate")
	public String deactivate;

	@XStreamAsAttribute
	@XStreamAlias("modified")
	public String modified;

	//

	public ImplementationBean implementation = new ImplementationBean();

	public ServiceBean service = new ServiceBean();

	//

	@XStreamImplicit(itemFieldName = "property")
	public Set<PropertyBean> propertySet = new TreeSet<PropertyBean>();

	@XStreamImplicit(itemFieldName = "properties")
	public Set<PropertyFileBean> propertyFileSet = new TreeSet<PropertyFileBean>();

	@XStreamImplicit(itemFieldName = "reference")
	public Set<ReferenceBean> referenceSet = new TreeSet<ReferenceBean>();

	//

	@Override
	public void accept(final BeanVisitor visitor) {
		visitor.visit(this);
	}

}
