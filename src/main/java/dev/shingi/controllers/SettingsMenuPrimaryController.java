package dev.shingi.controllers;

import java.io.IOException;

import dev.shingi.gui.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class SettingsMenuPrimaryController {
    FXMLLoader fxmlLoader;
	private static Scene scene;

	@FXML
	private AnchorPane placeholderAnchorPane;
	private Parent locationsEditorScene;
	private Parent fileConfigEditorScene;
	private Parent otherMenuScene;

    @FXML
    private void initialize() {
        
    }

    @FXML
	public void switchToFileConfigEditor(ActionEvent e) throws IOException {
		fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/Settings_Menu_Edit_File_Config.fxml"));
		fileConfigEditorScene = fxmlLoader.load();
//		FileConfigEditorController fileConfigEditorController = fxmlLoader.getController();
		placeholderAnchorPane.getChildren().setAll(fileConfigEditorScene);
	}
	
	@FXML
	public void switchToLocationsEditor(ActionEvent e) throws IOException {
		fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/Settings_Menu_Locations.fxml"));
		locationsEditorScene = fxmlLoader.load();
//		LocationsEditorFilesController locationsEditorFilesController = fxmlLoader.getController();
		placeholderAnchorPane.getChildren().setAll(locationsEditorScene);
	}
	
	@FXML
	public void switchToOtherMenu(ActionEvent e) throws IOException {
		fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/Settings_Menu_Other.fxml"));
		otherMenuScene = fxmlLoader.load();
//		FileConfigEditorController fileConfigEditorController = fxmlLoader.getController();
		placeholderAnchorPane.getChildren().setAll(otherMenuScene);
	}
	
	
	static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
