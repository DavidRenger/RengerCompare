package dev.shingi.controllers;

import java.util.ArrayList;

import dev.shingi.models.FileConfig;
import dev.shingi.models.FileConfigManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class EditFileConfigController {

	private FileConfigManager fileConfigManager;

    @FXML private ListView<FileConfig> fileConfigListView;
    @FXML private Button addFileConfigButton, removeFileConfigButton;

    @FXML private Label nameLabel;

    @FXML private TextField dateTextField;
    @FXML private TextField dateHeaderNameTextField, descriptionHeaderNameTextField;
    @FXML private ToggleButton button1, button2, button3, infoButton;
    @FXML private VBox dynamicContentContainer;
    @FXML private TextField headerRowTextField, rowOfFirstTransactionTextField;
    @FXML private Button saveButton;

    /*
     * Initializes the gui
     */
	public void initialize() {
		// Get the static instance of the file config manager
		fileConfigManager = FileConfigManager.getInstance();

        // Get the observable list for the listview and select the first item
        this.fileConfigListView.setItems(fileConfigManager.getConfigs());
        if (fileConfigManager.getConfigs().size() > 0) {
            this.fileConfigListView.getSelectionModel().select(0);
        }
    }

    @FXML void handleAddFileConfigButton(ActionEvent event) {
        try {
            fileConfigManager.addConfig(new FileConfig("new"));
        } catch (Exception e) {
            // TO DO: add alert that config with this name already exists
            e.printStackTrace();
        }
    }

    @FXML void handleRemoveFileConfigButton(ActionEvent event) {
        this.fileConfigManager.removeConfig(this.fileConfigListView.getSelectionModel().getSelectedItem().getSourceName());
    }

    @FXML void handleButton1Action(ActionEvent event) {
        dynamicContentContainer.getChildren().clear();
        dynamicContentContainer.getChildren().addAll(
            new Label("Text Field 1"),
            new Label("Text Field 2"),
            new Label("Text Field 3")
        );
    }

    @FXML void handleButton2Action(ActionEvent event) {
        dynamicContentContainer.getChildren().clear();
        dynamicContentContainer.getChildren().addAll(
            new TextField("Label 1"),
            new TextField("Label 2"),
            new TextField("Label 3")
        );
    }

    @FXML void handleButton3Action(ActionEvent event) {
        dynamicContentContainer.getChildren().clear();
        dynamicContentContainer.getChildren().addAll(
            new Button("Button 1"),
            new Button("Button 2"),
            new Button("Button 3")
        );
    }

    @FXML void handleInfoButtonAction(ActionEvent event) {
        dynamicContentContainer.getChildren().clear();
        dynamicContentContainer.getChildren().addAll(
            new TextArea(
            "Knop 1: als het bedrag in 1 kolom staat en debet en credit zijn aangegeven als min- en plusgetallen.\n" +
            "Knop 2: als het bedrag in 1 kolom staat en debet of credit is aangegeven in een andere kolom.\n" +
            "Knop 3: als het bedrag opgesplitst is in 2 kolommen, waarvan een de debetbedragen bevat en de ander credit."
            )
        );
    }

    @FXML void handleSaveButton(ActionEvent event) {
        
    }
}