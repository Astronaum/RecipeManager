package dp.grp4.models.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class JsonDB {
    private static final String DATABASE_FOLDER="C:\\Users\\yassi\\Downloads\\JsonDB";
    private static final JsonDB INSTANCE=new JsonDB();
    private final List<Collection> collections;
    private record Collection(String name,Class<?> className){}
    private final Map<String, Map<Long,Object>> data;
    private final ObjectMapper jsonIO = new ObjectMapper();
    private JsonDB(){
        this.collections= new ArrayList<>();
        this.collections.add(new Collection("ingredients", Ingredient.class));
        this.collections.add(new Collection("recipes", Recipe.class));
        this.data=new HashMap<>();
        this.jsonIO.enable(SerializationFeature.INDENT_OUTPUT);
        loadData();
    }
    private void loadData(){
        Path path = Paths.get(DATABASE_FOLDER);
        if (!Files.exists(path))
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create the Database directory.");
            }
        collections.forEach(collection->{
            File file = new File(Paths.get(DATABASE_FOLDER,collection.name()+".json").toString());
            try {
                if(file.createNewFile()){
                    this.data.put(collection.name(),new HashMap<>());
                    this.jsonIO.writeValue(file, this.data.get(collection.name()));
                }else
                    this.readCollectionFromDB(collection.name());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static JsonDB getInstance(){
        return INSTANCE;
    }
    private void writeCollectionToDB(String collectionName) {
        File file = new File(Paths.get(DATABASE_FOLDER,collectionName+".json").toString());
        try {
            this.jsonIO.writeValue(file, this.data.get(collectionName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void readCollectionFromDB(String collectionName) {
        File file = new File(Paths.get(DATABASE_FOLDER,collectionName+".json").toString());
        Map<Long, Object> map = null;
        try {
            map = this.jsonIO.readValue(file,new TypeReference<>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.data.put(collectionName,map);
    }
    public Map<Long,Object> getCollection(String collectionName){
        return this.data.get(collectionName);
    }
    public void setCollection(String collectionName,Map<Long,Object> collection) {
        this.data.put(collectionName,collection);
        this.writeCollectionToDB(collectionName);
    }
}
