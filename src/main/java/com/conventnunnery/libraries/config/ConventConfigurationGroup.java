package com.conventnunnery.libraries.config;

import java.util.Arrays;
import java.util.List;

public class ConventConfigurationGroup {

	List<ConventConfiguration> conventConfigurations;

	public ConventConfigurationGroup(ConventConfiguration... conventConfigurations) {
		this(Arrays.asList(conventConfigurations));
	}

	public ConventConfigurationGroup(List<ConventConfiguration> conventConfigurations) {
		this.conventConfigurations = conventConfigurations;
	}

	public boolean addConventConfiguration(ConventConfiguration c) {
		conventConfigurations.add(c);
		return conventConfigurations.contains(c);
	}

	public boolean removeConventConfiguration(String name) {
		ConventConfiguration c = getConventConfiguration(name);
		return c == null || removeConventConfiguration(c);
	}

	public ConventConfiguration getConventConfiguration(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		for (ConventConfiguration c : conventConfigurations) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public boolean removeConventConfiguration(ConventConfiguration c) {
		conventConfigurations.remove(c);
		return !conventConfigurations.contains(c);
	}

	public List<ConventConfiguration> getConventConfigurations() {
		return conventConfigurations;
	}

	public void loadAll() {
		for (ConventConfiguration c : conventConfigurations) {
			c.load();
		}
	}

	public void saveAll() {
		for (ConventConfiguration c : conventConfigurations) {
			c.save();
		}
	}

}
