package tdlm.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import tdlm.data.DataManager;
import tdlm.data.ToDoItem;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class FileManager implements AppFileComponent {
    // FOR JSON SAVING AND LOADING
    static final String JSON_CATEGORY = "category";
    static final String JSON_DESCRIPTION = "description";
    static final String JSON_START_DATE = "start_date";
    static final String JSON_END_DATE = "end_date";
    static final String JSON_COMPLETED = "completed";
    
    static final String JSON_NAME = "name";
    static final String JSON_OWNER = "owner";
    static final String JSON_ITEMS = "items";
    
    /**
     * This method is for saving user work.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	DataManager dataManager = (DataManager)data;
	
	// FIRST THE LIST NAME AND OWNER
        String name;
        String owner;
        
        if (dataManager.getName() == null)
            name = "";
        else
            name = dataManager.getName();
        
        if (dataManager.getOwner() == null)
            owner = "";
        else
            owner = dataManager.getOwner();
        
	// NOW BUILD THE JSON ARRAY FOR THE LIST
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	ObservableList<ToDoItem> items = dataManager.getItems();
	for (ToDoItem item : items) {	    
	    JsonObject itemJson = Json.createObjectBuilder()
		    .add(JSON_CATEGORY, item.getCategory())
		    .add(JSON_DESCRIPTION, item.getDescription())
		    .add(JSON_START_DATE, item.getStartDate().toString())
		    .add(JSON_END_DATE, item.getEndDate().toString())
		    .add(JSON_COMPLETED, item.getCompleted()).build();
	    arrayBuilder.add(itemJson);
	}
	JsonArray itemsArray = arrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_NAME, name)
                .add(JSON_OWNER, owner)
		.add(JSON_ITEMS, itemsArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	DataManager dataManager = (DataManager)data;
	dataManager.reset();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
	
	// LOAD LIST NAME AND OWNER
	String name = json.getString(JSON_NAME);
	String owner = json.getString(JSON_OWNER);
        
        //TA showed me that I forgot to add these two lines of code, which was messing up the name/owner
        //data loading extensively. Now, however, it is fixed I believe.
        dataManager.setNameString(name);
	dataManager.setOwnerString(owner);
        
	// AND NOW LOAD ALL THE ITEMS
	JsonArray jsonItemArray = json.getJsonArray(JSON_ITEMS);
	for (int i = 0; i < jsonItemArray.size(); i++) {
	    JsonObject jsonItem = jsonItemArray.getJsonObject(i);
	    ToDoItem item = loadItem(jsonItem);
	    dataManager.addItem(item);
	}
    }
    
    public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    public ToDoItem loadItem(JsonObject jsonItem) {
	// GET THE DATA
	String category = jsonItem.getString(JSON_CATEGORY);
	String description = jsonItem.getString(JSON_DESCRIPTION);
        String startDateText = jsonItem.getString(JSON_START_DATE);
        LocalDate startDate = LocalDate.parse(startDateText, DateTimeFormatter.ISO_DATE);
        String endDateText = jsonItem.getString(JSON_END_DATE);
        LocalDate endDate = LocalDate.parse(endDateText, DateTimeFormatter.ISO_DATE);
        boolean completed = jsonItem.getBoolean(JSON_COMPLETED);
        
	// THEN USE THE DATA TO BUILD AN ITEM
        ToDoItem item = new ToDoItem(category, description, startDate, endDate, completed);
        
	// ALL DONE, RETURN IT
	return item;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /**
     * This method would be used to export data to another format,
     * which we're not doing in this assignment.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {

    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        
    }
}
