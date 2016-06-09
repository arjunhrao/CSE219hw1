package tdlm.controller;

import java.io.File;
import java.io.IOException;
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
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

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
        
        //creates a stage for the dialog
        //Stage myStage = new Stage();
        
        //creates the dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("testing");
        
        //adds ok and cancel buttons
        ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        
        //start creating the boxes/panes for the gui
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        
        TextField from = new TextField();
        from.setPromptText("Category");
        TextField to = new TextField();
        to.setPromptText("Description");

        gridPane.add(from, 0, 0);
        gridPane.add(new Label("Description:"), 1, 0);
        gridPane.add(to, 2, 0);

        dialog.getDialogPane().setContent(gridPane);
        dialog.showAndWait();
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
