package tdlm.controller;

import java.awt.MouseInfo;
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
import static tdlm.PropertyType.ADD_ITEM_HEADING;
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
    DataManager myManager;
    
    public ToDoListController(AppTemplate initApp) {
	app = initApp;
        
    }
    
    public DataManager getDataManager() { return myManager;}
    
    public void changesMade() {
        //changes the value of saved in the AppFileController to show that the list
        //has been edited and has not been saved so that exiting will make sure to ask before just exiting.
        app.getGUI().getFileController().setSaved(false);
    }
    public void processNameChange() {
        
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        //workspace.setNameTextField("aaaaaaaaaah");
        //change name upon typing in - actually might not need this because I can do it when I save or load
        myManager=(DataManager)app.getDataComponent();
        //update name data
        myManager.setNameString(workspace.getNameTextField().getText());
        
        workspace.getNameTextField().selectEnd();
        
        //enable save button
        app.getGUI().getSaveButton().setDisable(false);
        changesMade();
	workspace.reloadWorkspace();

    }
    public void processOwnerChange() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        
        myManager=(DataManager)app.getDataComponent();
        
        myManager.setOwnerString(workspace.getOwnerTextField().getText());
        /**
         * if (workspace.getOwnerTextField().getText() == null)
            myManager.setOwner("");
        else
            myManager.setOwner(workspace.getNameTextField().getText());
            * */
        
        
        //enable save button
        app.getGUI().getSaveButton().setDisable(false);
        changesMade();
	workspace.reloadWorkspace();

    }
    
    public void processAddItem() {
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
        myManager=(DataManager)app.getDataComponent();
        
        //need a popup dialogue box with multiple input fields for all of the todoitem's data
        
        //the following line is included so that we can use things from the xml files
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        
        //creates a stage for the dialog
        //Stage myStage = new Stage();
        
        //creates the dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(props.getProperty(ADD_ITEM_HEADING));
        
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

        gridPane.add(new Label(props.getProperty(ENDDATE_PROMPT)), 0, 3);
        gridPane.add(endDate, 1, 3);

        CheckBox completed = new CheckBox();
        //do i need to make sure that it can't be in the indeterminate state?
        
        gridPane.add(new Label(props.getProperty(COMPLETED_PROMPT)), 0, 4);
        gridPane.add(completed, 1, 4);

        dialog.getDialogPane().setContent(gridPane);
        Optional<ButtonType> result = dialog.showAndWait();
        
        if (result.isPresent() && result.get() == okButtonType) {
            //set and save the data to myItem and add it to the arraylist in the datamanager obj myManager
            myItem.setCategory(category.getText());
            myItem.setDescription(description.getText());
            myItem.setStartDate(startDate.getValue());
            myItem.setEndDate(endDate.getValue());
            myItem.setCompleted(completed.isSelected());
            myManager.addItem(myItem);
            
            //enable the save button
            app.getGUI().getSaveButton().setDisable(false);
            
            changesMade();
            //update the workspace / table
            workspace.reloadWorkspace();
            //useless line of code: app.getWorkspaceComponent().getWorkspace().getChildren().clear();
        }
        
        
    }
    
    public void processRemoveItem(Boolean selected, ToDoItem item) {
        if (selected) {
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace();
            
            myManager=(DataManager)app.getDataComponent();
            
            //remove the item
            myManager.getItems().remove(item);
            //enable save
            app.getGUI().getSaveButton().setDisable(false);
            changesMade();
            workspace.reloadWorkspace();
        }
                    
    }
    
    public void processMoveUpItem(Boolean selected, ToDoItem item, int indexOfSelection) {
        //if statement confirms that something that isn't the first element, of index 0, is selected
        if (selected && indexOfSelection != 0) {
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace();
            
            myManager=(DataManager)app.getDataComponent();
            
            //remove the item before
            ToDoItem temp = myManager.getItems().get(indexOfSelection-1);
            myManager.getItems().remove(indexOfSelection-1);
            //add it back after
            myManager.getItems().add(indexOfSelection, temp);
            //enable save
            app.getGUI().getSaveButton().setDisable(false);
            changesMade();
            workspace.reloadWorkspace();
        }
    }
    
    public void processMoveDownItem(Boolean selected, ToDoItem item, int indexOfSelection) {
        //confirms that something is selected
        if (selected) {
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace();
            
            myManager=(DataManager)app.getDataComponent();
            
            //remove the item before
            ToDoItem temp = myManager.getItems().get(indexOfSelection+1);
            myManager.getItems().remove(indexOfSelection+1);
            //add it back after
            myManager.getItems().add(indexOfSelection, temp);
            
            //enable save
            app.getGUI().getSaveButton().setDisable(false);
            changesMade();
            workspace.reloadWorkspace();
        }
    }
    
    public void processEditItem(ToDoItem it) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        
        myManager=(DataManager)app.getDataComponent();
        
        
        int x = myManager.getItems().indexOf(it);
        
        //Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
        
        
        //need a popup dialogue box with multiple input fields for all of the todoitem's data
        
        //the following line is included so that we can use things from the xml files
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        
        //creates a stage for the dialog
        //Stage myStage = new Stage();
        
        //creates the dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(props.getProperty(ADD_ITEM_HEADING));
        
        //adds ok and cancel buttons
        ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        
        //start creating the boxes/panes for the gui
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        
        TextField category = new TextField();
        //IMPORTANT: this actually puts the relevant information inside
        category.setText(it.getCategory());
        //Note: the next two commented lines of code set the prompty text, which is nice but unnecessary
        //category.setPromptText(props.getProperty(CATEGORY_PROMPT));
        TextField description = new TextField();
        //IMPORTANT: this actually puts the relevant information inside
        description.setText(it.getDescription());
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
        //IMPORTANT: this actually puts the relevant information inside
        startDate.setValue(it.getStartDate());
        //IMPORTANT: this actually puts the relevant information inside
        endDate.setValue(it.getEndDate());
        
        gridPane.add(new Label(props.getProperty(STARTDATE_PROMPT)), 0, 2);
        gridPane.add(startDate, 1, 2);

        gridPane.add(new Label(props.getProperty(ENDDATE_PROMPT)), 0, 3);
        gridPane.add(endDate, 1, 3);

        CheckBox completed = new CheckBox();
        //IMPORTANT: this actually puts the relevant information inside
        completed.setSelected(it.getCompleted());
        //do i need to make sure that it can't be in the indeterminate state?
        
        gridPane.add(new Label(props.getProperty(COMPLETED_PROMPT)), 0, 4);
        gridPane.add(completed, 1, 4);

        dialog.getDialogPane().setContent(gridPane);
        Optional<ButtonType> result = dialog.showAndWait();
        
        
        if (result.isPresent() && result.get() == okButtonType) {
            //set and save the data to myItem and add it to the arraylist in the datamanager obj myManager
            myManager.getItems().get(x).setCategory(category.getText());
            myManager.getItems().get(x).setDescription(description.getText());
            myManager.getItems().get(x).setStartDate(startDate.getValue());
            myManager.getItems().get(x).setEndDate(endDate.getValue());
            myManager.getItems().get(x).setCompleted(completed.isSelected());
            
            
            //enable the save button
            app.getGUI().getSaveButton().setDisable(false);
            
            //update the workspace / table / savestate
            changesMade();
            workspace.reloadWorkspace();
            //useless line of code: app.getWorkspaceComponent().getWorkspace().getChildren().clear();
        }
    }
}
