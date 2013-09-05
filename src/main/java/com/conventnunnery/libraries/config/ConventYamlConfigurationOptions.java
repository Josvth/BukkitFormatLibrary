package com.conventnunnery.libraries.config;

import org.bukkit.configuration.file.YamlConfigurationOptions;

public class ConventYamlConfigurationOptions extends YamlConfigurationOptions {

	private boolean updateOnLoad = true;		// This will update the file every time the load() method is called
	private boolean backupOnUpdate = false;		// This will make a backup of the file every time the update() method is called
	private boolean createDefaultFile = true;   // This will create a default file if there is no existing one found

	public ConventYamlConfigurationOptions(ConventYamlConfiguration configuration) {
		super(configuration);
	}

	public boolean updateOnLoad() {
		return updateOnLoad;
	}

	public void updateOnLoad(boolean updateOnLoad) {
		this.updateOnLoad = updateOnLoad;
	}

	public boolean backupOnUpdate() {
		return backupOnUpdate;
	}

	public void backupOnUpdate(boolean backupOnUpdate) {
		this.backupOnUpdate = backupOnUpdate;
	}

	public boolean createDefaultFile() {
		return createDefaultFile;
	}

	public void createDefaultFile(boolean createDefaultFile) {
		this.createDefaultFile = createDefaultFile;
	}
}
