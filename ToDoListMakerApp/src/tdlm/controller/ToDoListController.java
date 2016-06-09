package tdlm.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javax.imageio.ImageIO;
import tdlm.data.DataManager;
import tdlm.gui.Workspace;
import saf.AppTemplate;

import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import properties_manager.PropertiesManager;
import static tdlm.PropertyType.CATEGORY_PROMPT;
import static tdlm.PropertyType.COMPLETED_PROMPT;
import static tdlm.PropertyType.DESCRIPTION_PROMPT;
import static tdlm.PropertyType.ENDDATE_PROMPT;
import static tdlm.PropertyType.STARTDATE_PROMPT;
import tdlm.data.ToDoItem;

/**
 * This class responds to interactions with todo list editing controls.
 * 
 * @author McKillaGorilla
 * @version 1.0
 */
public class ToDoListController {
    AppTemplate app;
    
    public ToDoListController(AppTemplate initApp) {
	app = initApp;
    }
    
    
    public void processAddItem() {
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
        
        
        //need a popup dialogue box with multiple input fields for all of the todoitem's data
        
        //the following line is included so that we can use things from the xml files
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        
        //creates a stage for the dialog
        //Stage myStage = new Stage();
        
        //creates the dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("testing");
        
        //adds ok and cancel buttons
        ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        
        //start creating the boxes/panes for the gui
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        
        TextField category = new TextField();
        //Note: the next two commented lines of code set the prompty text, which is nice but unnecessary
        //category.setPromptText(props.getProperty(CATEGORY_PROMPT));
        TextField description = new TextField();
        //description.setPromptText("Description");

        //HBox HBox1 = new HBox();

        ToDoItem myItem = new ToDoItem();
        
        gridPane.add(new Label(props.getProperty(CATEGORY_PROMPT)), 0, 0);
        gridPane.add(category, 1, 0);
        gridPane.add(new Label(props.getProperty(DESCRIPTION_PROMPT)), 0, 1);
        gridPane.add(description, 1, 1);
        
        DatePicker startDate = new DatePicker();
        DatePicker endDate = new DatePicker();
        startDate.setValue(myItem.getStartDate());
        endDate.setValue(myItem.getEndDate());
        
        gridPane.add(new Label(props.getProperty(STARTDATE_PROMPT)), 0, 2);
        gridPane.add(startDate, 1, 2);

        gridPane.add(new Label(props.getProperty(ENDDATE_PROMPT)), 3, 2);
        gridPane.add(endDate, 4, 2);

        CheckBox completed = new CheckBox();
        gridPane.add(new Label(props.getProperty(COMPLETED_PROMPT)), 0, 3);
        gridPane.add(completed, 1, 3);

        dialog.getDialogPane().setContent(gridPane);
        Optional<Pair<String, String>> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            //save the data to myItem and add it to the list
        }
        
        
        
        
    }
    
    public void processRemoveItem() {
        
    }
    
    public void processMoveUpItem() {
        
    }
    
    public void processMoveDownItem() {
        
    }
    
    public void processEditItem() {
        
    }
}
