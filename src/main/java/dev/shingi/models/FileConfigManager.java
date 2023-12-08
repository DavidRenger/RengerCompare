package dev.shingi.models;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;

import com.google.gson.Gson;
import com.google.gson.reflect.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileConfigManager {    
    public final String DATE_FORMAT;
    public final String AMOUNT_FORMAT;
    public final String DESCRIPTION_FORMAT;
    public final String DEBET_OR_CREDIT_FORMAT;

    private ObservableList<FileConfig> configs;
    
    private static volatile FileConfigManager instance;

    private FileConfigManager() {
        this.DATE_FORMAT = CellType.NUMERIC.toString(); // Replace with actual method
        this.AMOUNT_FORMAT = CellType.NUMERIC.toString(); // Replace with actual method
        this.DESCRIPTION_FORMAT = CellType.STRING.toString(); // Replace with actual method
        this.DEBET_OR_CREDIT_FORMAT = CellType.STRING.toString(); // Replace with actual method

        configs = FXCollections.observableArrayList();
    }

    public static FileConfigManager getInstance() {
        if (instance == null) {
            synchronized(FileConfigManager.class) {
                if (instance == null) {
                    instance = new FileConfigManager();
                }
            }
        }
        return instance;
    }

    // Method to add a new FileConfig
    public void addConfig(FileConfig config) {
        configs.add(config);
    }

    // Method to remove an existing FileConfig
    public void removeConfig(String sourceName) {
        configs.remove(getConfig(sourceName));
    }

    // Method to save all existing configs in json format
    public void saveConfigs(String filePath) {
        Gson gson = new Gson();
        String json = gson.toJson(configs);
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load all configs in the program's respective json file
    public void loadConfigs(String filePath) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, FileConfig>>(){}.getType();
        
        try (Reader reader = new FileReader(filePath)) {
            configs = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get a FileConfig by source name
    public FileConfig getConfig(String sourceName) {
        for (FileConfig config : configs) {
            if (config.getSourceName().equals(sourceName)) {
                return config;
            }
        }

        System.out.println("Config with this name not found.");
        return null;
    }

    public String getDATE_FORMAT() {
        return DATE_FORMAT;
    }

    public String getAMOUNT_FORMAT() {
        return AMOUNT_FORMAT;
    }

    public String getDESCRIPTION_FORMAT() {
        return DESCRIPTION_FORMAT;
    }

    public String getDEBET_OR_CREDIT_FORMAT() {
        return DEBET_OR_CREDIT_FORMAT;
    }

    public ObservableList<FileConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(ObservableList<FileConfig> configs) {
        this.configs = configs;
    }

    // TO DO: Methods to load and save configurations to a file could be added later
}
