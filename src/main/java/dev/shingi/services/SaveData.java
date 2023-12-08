package dev.shingi.services;

import java.io.Serializable;

import dev.shingi.models.FileConfigManager;

public class SaveData implements Serializable {
    
	private FileConfigManager fileConfigManager;
	private String defaultInputPath;
	private String defaultOutputPath;


    public FileConfigManager getFileConfigManager() {
        return fileConfigManager;
    }
    public void setFileConfigManager(FileConfigManager fileConfigManager) {
        this.fileConfigManager = fileConfigManager;
    }
    public String getDefaultInputPath() {
        return defaultInputPath;
    }
    public void setDefaultInputPath(String defaultInputPath) {
        this.defaultInputPath = defaultInputPath;
    }
    public String getDefaultOutputPath() {
        return defaultOutputPath;
    }
    public void setDefaultOutputPath(String defaultOutputPath) {
        this.defaultOutputPath = defaultOutputPath;
    }
}
