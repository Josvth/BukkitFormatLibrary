package com.conventnunnery.libraries.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.InputStream;

public interface ConventConfiguration {

    boolean load();

    boolean save();

    void setDefaults(InputStream inputStream);

	@Deprecated
	void saveDefaults(InputStream inputStream);

	boolean needToUpdate();

	boolean backup();

	String getName();

	FileConfiguration getFileConfiguration();

	String getVersion();

}
