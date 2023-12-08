package dev.shingi.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import dev.shingi.models.AmountInfo;
import dev.shingi.models.FileConfig;
import dev.shingi.models.FileConfigManager;
import dev.shingi.utils.DateUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class FileConfigEditorController {
    // Configuration option setup
	@FXML private HBox contentHbox;
	@FXML private Button newConfigButton, saveFileConfigButton;
	@FXML private ComboBox<FileConfig> selectConfigComboBox;
	@FXML private Label selectOrDropPreviewFileLabel;

	// Basic fileConfig information
	@FXML private TextField sourceNameField;

	// Date formats
	@FXML private Label dateFormatPreview;
	@FXML private ComboBox<String> dateFormatComboBox;

	// Debet and credit formats
	@FXML private ToggleGroup amountFormatGroup;
	@FXML private RadioButton inAmountRadioButton, inSeparateColumnRadioButton, inSeparateColumnsRadioButton;
	@FXML private TextField debetCreditHeaderNameField, creditFormatField, debetFormatField;

	// Basic read information
	@FXML private TextField firstTransactionRowField, headerRowField;

	// Header names
	@FXML private TextField dateHeaderNameTextField, amountHeaderNameTextField, descriptionHeaderNameTextField;
	
	// FileConfig data
	private FileConfigManager fileConfigManager;
	private FileConfig lastSelectedConfig;

	// Program state: false = creating a new configuration; true = editing an existing configuration
	private boolean editingExistingConfig;
	
    /*
     * Initializes the gui
     */
	public void initialize() {
		// Get the static instance of the file config manager
		fileConfigManager = FileConfigManager.getInstance();

		// Initialize program state
		editingExistingConfig = false;

		// Configuration menu is disables until user chooses to create a new configuration or opens an existing configuration.
		contentHbox.setDisable(true);

		// Initialize all combo boxes with preset data if available
		initializeComboBoxes();

		// Initialize toggle groups
		initializeToggleGroups();
	}

	private void initializeComboBoxes() {
		// Initialize config selection combobox
		selectConfigComboBox.setItems(fileConfigManager.getConfigs());

		// Initialize date format combo box and set items
		dateFormatComboBox.setItems(DateUtils.getDateFormatPresets());
	}

	private void initializeToggleGroups() {
		// Initialize amount format toggle group
		amountFormatGroup = new ToggleGroup();

		// Add toggles to the amount format toggle group
		inAmountRadioButton.setToggleGroup(amountFormatGroup); inSeparateColumnRadioButton.setToggleGroup(amountFormatGroup); inSeparateColumnsRadioButton.setToggleGroup(amountFormatGroup);
	}
	
    /*
     * 
     */
	@FXML
	public void handleSaveFileConfigButton(ActionEvent event) throws Exception {
		if (fileConfigManager.getConfigs().contains(fileConfigManager.getConfig(sourceNameField.getText()))) {
			// TO DO: Implement alert!
			System.out.println("FileConfig with this name already exists.");
		} else if (allConfigItemsValid()) {
			System.out.println("All items are valid.");
			if (editingExistingConfig) {
				updateExistingConfig();
			} else {
				createNewConfig();
			}
		}
	}

	@FXML
	private void handleNewConfigButton(ActionEvent event) {
		// Set the program state to 0: creating a new file configuration setup.
		editingExistingConfig = false;

		// Enable the contentHbox (where the user enters data). This is disabled by default to force the user to choose whether to create a new configuration or edit an existing configuration.
		if (contentHbox.isDisabled()) { 
			contentHbox.setDisable(false);
		} else {
			// Clear the selection box current selection and clear the data fields.
			selectConfigComboBox.getSelectionModel().clearSelection();
			clearFields();
		}
	}

	/*
    * Loads all existing file configs into the configuration selection comboBox
    * Adds listener to the configuration selection comboBox
    */
    @FXML
    private void handleSelectFileConfigComboBox(ActionEvent event) {
		
		// Set the program state to 1: editing an existing file configuration.
		editingExistingConfig = true;
        populateItemsWithFileConfigData();
        System.out.println("FileConfig selected: " + selectConfigComboBox.getSelectionModel().getSelectedItem().toString());
    }

	/*
	 * Handle debet credit radio button group
	 */
	@FXML
    private void handleInAmountRadioButton(ActionEvent event) {
		// amountFormatGroup.selectToggle(inAmountRadioButton);

		// Set text fields to empty and disable them.
		debetCreditHeaderNameField.setText("");
		debetCreditHeaderNameField.setDisable(true);
		debetFormatField.setText("");
		creditFormatField.setText("");
		creditFormatField.setDisable(true);
		debetFormatField.setDisable(true);
    }
	@FXML
    private void handleInSeparateColumnRadioButton(ActionEvent event) {
		// amountFormatGroup.selectToggle(inSeparateColumnRadioButton);

		// Disable the credit and debet format text fields if they are enabled
		if (!creditFormatField.isDisabled() && !debetFormatField.isDisabled()) {
			creditFormatField.setDisable(true);
			debetFormatField.setDisable(true);
		}

		// Enable the header name field
		debetCreditHeaderNameField.setDisable(false);
    }
	@FXML
    private void handleInSeparateColumnsRadioButton(ActionEvent event) {
		// amountFormatGroup.selectToggle(inSeparateColumnsRadioButton);

		// Disable the header name field if it is enabled
		if (!debetCreditHeaderNameField.isDisabled()) {
			debetCreditHeaderNameField.setDisable(true);
		}

		// Enable the credit and debet format text fields
		creditFormatField.setDisable(false);
		debetFormatField.setDisable(false);
    }

	/*
     * Poplates the configuration menu items with the data from the selected fileConfig
     */
	private void populateItemsWithFileConfigData() {
		sourceNameField.setText(selectConfigComboBox.getSelectionModel().getSelectedItem().getSourceName());

		// Set the date format combo box
		String dateFormat = selectConfigComboBox.getSelectionModel().getSelectedItem().getDateFormat().toPattern();
		if (DateUtils.getDateFormatPresets().contains(dateFormat)) {
			dateFormatComboBox.getSelectionModel().clearAndSelect(DateUtils.getDateFormatPresets().indexOf(dateFormat));
		} else {
			dateFormatComboBox.getSelectionModel().clearSelection();
			dateFormatComboBox.getEditor().setText(dateFormat);
		}

		// Populate debet/credit information
		if (selectConfigComboBox.getSelectionModel().getSelectedItem().getAmountFormat().isInAmount()) {
			amountFormatGroup.selectToggle(inAmountRadioButton);
			amountHeaderNameTextField.setText(selectConfigComboBox.getSelectionModel().getSelectedItem().getAmountFormat().getAmountHeaderName());
		} else if (selectConfigComboBox.getSelectionModel().getSelectedItem().getAmountFormat().isInSeparateColumn()) {
			amountFormatGroup.selectToggle(inSeparateColumnRadioButton);
			debetCreditHeaderNameField.setText(selectConfigComboBox.getSelectionModel().getSelectedItem().getAmountFormat().getDebetCreditHeaderName());
		} else {
			amountFormatGroup.selectToggle(inSeparateColumnsRadioButton);
			debetFormatField.setText(selectConfigComboBox.getSelectionModel().getSelectedItem().getAmountFormat().getDebetFormat());
			creditFormatField.setText(selectConfigComboBox.getSelectionModel().getSelectedItem().getAmountFormat().getCreditFormat());
		}
		
		// Populate basic read information
		headerRowField.setText(Integer.toString(selectConfigComboBox.getSelectionModel().getSelectedItem().getHeaderRowNumber()));
		firstTransactionRowField.setText(Integer.toString(selectConfigComboBox.getSelectionModel().getSelectedItem().getFirstTransactionRowNumber()));

		// Populate basic header name fields
		dateHeaderNameTextField.setText(selectConfigComboBox.getSelectionModel().getSelectedItem().getDateHeaderName());
		descriptionHeaderNameTextField.setText(selectConfigComboBox.getSelectionModel().getSelectedItem().getDescriptionHeaderName());
	}

	/*
	 * Clears all data fields
	 */
    private void clearFields() {
		sourceNameField.setText("");
		dateFormatComboBox.getSelectionModel().clearSelection();
		amountFormatGroup.selectToggle(inAmountRadioButton);
		headerRowField.setText("");
		firstTransactionRowField.setText("");
		dateHeaderNameTextField.setText("");
		amountHeaderNameTextField.setText("");
		descriptionHeaderNameTextField.setText("");
	}

    /*
     * Updates the date preview according to the selected toggle or the entry in the custom date format field
     */
	@FXML
	private void updateDatePreview(ActionEvent event) {
		String pattern = dateFormatComboBox.getSelectionModel().getSelectedItem().toString();
		try {
			dateFormatPreview.setText(DateUtils.previewDateFromPattern(pattern));
		} catch (IllegalArgumentException e) {
			dateFormatPreview.setText("Invalid format");
		}
	}
	
	private void updateExistingConfig() {

	}

	private void createNewConfig() {
		// Create new fileconfig
		try {
			AmountInfo amountInfo = createAmountInfo();

			FileConfig fileConfig = new FileConfig(sourceNameField.getText(), dateFormatComboBox.getEditor().getText(), amountInfo, Integer.parseInt(headerRowField.getText()), 
			Integer.parseInt(firstTransactionRowField.getText()), dateHeaderNameTextField.getText(), amountHeaderNameTextField.getText(), descriptionHeaderNameTextField.getText());
			System.out.println("New file configuration created: " + fileConfig.getSourceName());

			// Save the FileConfig object to a file, database, etc.
			fileConfigManager.getConfigs().add(fileConfig);
			selectConfigComboBox.getSelectionModel().select(fileConfig);
			// TO DO: ResourceManager.save(fileConfigManager, "data");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AmountInfo createAmountInfo() {
		AmountInfo amountInfo = new AmountInfo();
		
		if (amountFormatGroup.getSelectedToggle() == inAmountRadioButton) {
			amountInfo.setInAmount(true);
			amountInfo.setAmountHeaderName(amountHeaderNameTextField.getText());
			return amountInfo;
		} else if (amountFormatGroup.getSelectedToggle() == inSeparateColumnRadioButton) {
			amountInfo.setInSeparateColumn(true);
			amountInfo.setDebetCreditHeaderName(debetCreditHeaderNameField.getText());
		} else if (amountFormatGroup.getSelectedToggle() == inSeparateColumnsRadioButton) {
			amountInfo.setInSeparateColumns(true);
			amountInfo.setCreditFormat(creditFormatField.getText());
			amountInfo.setDebetFormat(debetFormatField.getText());
		}
		return amountInfo;
	}

	/*
	 * Validation logic
	 */
	public boolean allConfigItemsValid() {
		if (contentHbox.isDisabled()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Nog geen configuratie geopend");
			alert.setHeaderText("Start of selecteer eerst een configuratie.");
			alert.showAndWait();
			return false;
		} else {
			// Initialize a hashmap that keeps track of valid and invalid items
			LinkedHashMap<String, Boolean> configItemsValidity = new LinkedHashMap<>();
			ArrayList<String> invalidItems = new ArrayList<>();
			int count = 0;
			
			configItemsValidity.put("Naam bron", validateFileConfigNameField());
			configItemsValidity.put("Datum format", validateDateFormat());
			configItemsValidity.put("Debet en Credit representatie", validateDebetCreditHeaderField());
			configItemsValidity.put("Rij met headers", validateHeaderInformation());
			configItemsValidity.put("Rij van eerste transactie", validateFirstTransactionRowField());
			
			for (Entry<String, Boolean> configItem: configItemsValidity.entrySet()) {
				if (!configItem.getValue()) {
					invalidItems.add(configItem.getKey());
					count++;
				}
			}
			
			if (count > 0) {
				StringBuilder stringBuilder = new StringBuilder();
				for (String item : invalidItems) {
					stringBuilder.append(item).append(", ");
				}
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Gegevens incompleet");
				alert.setHeaderText("Vul de volgende gegevens aan: " + stringBuilder.toString());
				alert.showAndWait();
				return false;
			}
		}
			
		return true;
	}

	public boolean validateFileConfigNameField() {
		return !sourceNameField.getText().isEmpty() || sourceNameField.getText() != null;
	}
	public boolean validateDateFormat() {
		return !dateFormatPreview.getText().equals("Invalid format");
	}
	public boolean validateCreditFormat() {
		return !creditFormatField.getText().isEmpty() || creditFormatField.getText() != null;
	}
	public boolean validateDebetFormat() {
		return !debetFormatField.getText().isEmpty() || debetFormatField.getText() != null;
	}
	public boolean validateDebetCreditHeaderField() {
		return !debetCreditHeaderNameField.getText().isEmpty() || debetCreditHeaderNameField.getText() != null;
	}
	public boolean validateHeaderInformation() {
		// if any of the comboboxes are empty, return false, else return true.
		if (dateHeaderNameTextField.getText() == null || dateHeaderNameTextField.getText().isEmpty()) {
			return false;
		}
		if (amountHeaderNameTextField.getText() == null || amountHeaderNameTextField.getText().isEmpty()) {
			return false;
		}
		if (descriptionHeaderNameTextField.getText() == null || amountHeaderNameTextField.getText().isEmpty()) {
			return false;
		}
		
		return true;
	}
	public boolean validateFirstTransactionRowField() {
		// Collect and validate fileConfig name
		if (firstTransactionRowField.getText() == null || firstTransactionRowField.getText().isEmpty()) {
			// Show error message or set the field to an error state
			return false;
		} else {
			try {
				Integer.parseInt(firstTransactionRowField.getText());
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}
}