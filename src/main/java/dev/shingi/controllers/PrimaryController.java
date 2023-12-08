package dev.shingi.controllers;

import dev.shingi.gui.Main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class PrimaryController {
    
	FXMLLoader fxmlLoader;
	private static Scene scene;

	@FXML
	private AnchorPane placeholderAnchorPane;
	private Parent compareCrossPointsScene;
	private Parent compareTransactionFilesScene;
	private Parent fileConfigScene;

    @FXML
    private void initialize() {
        
    }

    @FXML
	public void switchToCompareTransactionFiles(ActionEvent e) throws IOException {
		fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/CompareTransactionFiles.fxml"));
		compareTransactionFilesScene = fxmlLoader.load();
//		CompareTransactionFilesController compareTransactionFilesController = fxmlLoader.getController();
		placeholderAnchorPane.getChildren().setAll(compareTransactionFilesScene);
	}
	
	@FXML
	public void switchToCompareCrossPoints(ActionEvent e) throws IOException {
		fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/CompareCrossPointsFiles.fxml"));
		compareCrossPointsScene = fxmlLoader.load();
//		CompareCrossPointsFilesController compareCrossPointsFilesController = fxmlLoader.getController();
		placeholderAnchorPane.getChildren().setAll(compareCrossPointsScene);
	}
	
	@FXML
	public void switchToBankMenu(ActionEvent e) throws IOException {
		fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/FileConfigEditor copy.fxml"));
		fileConfigScene = fxmlLoader.load();
//		FileConfigEditorController fileConfigEditorController = fxmlLoader.getController();
		placeholderAnchorPane.getChildren().setAll(fileConfigScene);
	}
	
	
	static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
