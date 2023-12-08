package dev.shingi.gui;

import java.io.IOException;

import dev.shingi.services.ResourceManager;
import dev.shingi.services.SaveData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	public static SaveData save = new SaveData();
    public static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML());
        scene.getMnemonics();
        stage.setScene(scene);
        
        Image icon = new Image("/Logo (zonder kroon) photoshop.png");
        stage.getIcons().add(icon);
        stage.setTitle("RengerCompare");
        
        stage.show();
    }

    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/PrimaryScene.fxml"));
        return fxmlLoader.load();
    }
    public static void main(String[] args) {
        launch();
    }

    public static boolean saveData() {
		try {
			ResourceManager.save(Main.save, "data");
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}
}




















/*
 * In the context of a JavaFX application, the .gui package would typically contain classes that directly manage or manipulate the GUI components but aren't necessarily controllers in the MVC sense. However, given your project's structure where FXML files manage the layout and controllers handle the logic, it might not be immediately obvious what could go into a .gui package. Here are some scenarios where it could be useful:

Custom Components: If you decide to create any custom JavaFX components that can be reused across different parts of your application, these would go in the .gui package.

Example: CustomTableView.java, DragAndDropPane.java
Dialogs and Popups: If you have complex logic behind creating or showing dialogs and popups, these classes could go here.

Example: ConfirmationDialog.java, ErrorPopup.java
GUI Utilities: Any utility classes that facilitate common GUI operations, such as loading images, formatting text fields, etc.

Example: ImageLoader.java, TextFieldFormatter.java
GUI Constants: A class (or classes) that hold constants used throughout the GUI, like standard dimensions, colors, or style classes.

Example: GUIConstants.java
GUI Initializers: Classes that perform initial setup for the GUI, like setting a theme, initializing a stage, etc.

Example: AppInitializer.java
 */