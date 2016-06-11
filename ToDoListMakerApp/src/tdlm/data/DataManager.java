package tdlm.data;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import tdlm.gui.Workspace;
import saf.components.AppDataComponent;
import saf.AppTemplate;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna and coauthor Arjun Rao
 * @version 1.0
 */
public class DataManager implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES
    
    // NAME OF THE TODO LIST
    StringProperty name;
    String nameString;
    // LIST OWNER
    StringProperty owner;
    String ownerString;
    
    // THESE ARE THE ITEMS IN THE TODO LIST
    ObservableList<ToDoItem> items;
    
    
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;
        List list = new ArrayList();
        items = FXCollections.observableList(list);
        name = new SimpleStringProperty();
        owner = new SimpleStringProperty();
        nameString = "";
        ownerString = "";
    }
    
    public ObservableList<ToDoItem> getItems() {
	return items;
    }
    
    public String getName() {
        return nameString;
        /**
         * if (name == null)
            return "";
        else
            return name.get();
            * */
        
    }
    
    public String getOwner() {
        return ownerString;
        /**
         * if (owner == null)
            return "";
        else
            return owner.get();
            * */
    }
    
    public void setNameString(String s) {
        //name = new SimpleStringProperty();
        //name.setValue(s);
        nameString = s;
    }
    public void setOwnerString(String s) {
        //owner = new SimpleStringProperty();
        //owner.setValue(s);
        ownerString = s;
    }

    public void addItem(ToDoItem item) {
        items.add(item);
    }



    /**
     * 
     */
    @Override
    public void reset() {
        //clear items - remove all todoitems in it
        items.clear();
        
        //not sure if this is right
        name.setValue("");
        owner.setValue("");
        
        nameString = "";
        ownerString = "";
        
    }
}
