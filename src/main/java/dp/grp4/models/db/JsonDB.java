package dp.grp4.models.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dp.grp4.exceptions.DBException;
import dp.grp4.exceptions.ExceptionHandler;

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
    private static final String DATABASE_FOLDER= Configuration.DATABASE_FOLDER;
    private static final String METADATA_FILENAME= Configuration.METADATA_FILENAME;
    private static final String FILE_EXTENSION= Configuration.FILE_EXTENSION;
    private static final JsonDB INSTANCE=new JsonDB();
    public record Metadata(String collectionName,long maxId){}
    private List<Metadata> metadataList;
    private final Map<String, Map<Long,Object>> data;
    private final JsonIO jsonIO = new JsonIO();

    public static class JsonIO extends ObjectMapper{
        public JsonIO(){
            super();
            this.enable(SerializationFeature.INDENT_OUTPUT);
            this.registerModule(new JavaTimeModule());
        }
    }

    public static JsonDB getInstance(){
        return INSTANCE;
    }
    private JsonDB(){
        this.metadataList=new ArrayList<>();
        this.data=new HashMap<>();
        for(int i=0;i<Configuration.COLLECTIONS.size();i++){
            Class<?> clazz=Configuration.COLLECTIONS.get(i);
            this.data.put(getNameFromClazz(clazz),new HashMap<>());
            this.metadataList.add(new Metadata(getNameFromClazz(clazz),0L));
        }
        this.loadData();
    }
    private String getNameFromClazz(Class<?> clazz){
        return clazz.getSimpleName().toLowerCase();
    }
    private void loadData() {
        ExceptionHandler.context(()->{
            Path path = Paths.get(DATABASE_FOLDER);
            if (!Files.exists(path))
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    throw new DBException("Failed to create the Database directory.");
                }
            List<String> collections=new ArrayList<>(this.data.keySet());
            for(int i = collections.size() - 1; i >= 0; i--){
                String collectionName=collections.get(i);
                File file = new File(Paths.get(DATABASE_FOLDER,collectionName+FILE_EXTENSION).toString());
                try {
                    if(file.createNewFile()){
                        this.jsonIO.writeValue(file, this.data.get(collectionName));
                    }else
                        this.readCollectionFromDB(collectionName);
                } catch (Exception e) {
                    throw new DBException(e.getMessage());
                }
            }
            File metadataFile=new File(Paths.get(DATABASE_FOLDER,METADATA_FILENAME).toString());
            try {
                if(metadataFile.createNewFile()){
                    this.jsonIO.writeValue(metadataFile, this.metadataList);
                }else{
                    this.metadataList= this.jsonIO.readValue(metadataFile, new TypeReference<>() {});
                }
            } catch (Exception e) {
                throw new DBException(e.getMessage());
            }
        });
    }

    public long getNextId(Class<?> collectionClass) throws DBException {
        String collectionName=getNameFromClazz(collectionClass);
        long nextId=0;
        for(int i = this.metadataList.size() - 1; i >= 0; i--){
            Metadata m=this.metadataList.get(i);
            if(m.collectionName.equals(collectionName)){
                nextId=m.maxId+1;
                this.metadataList.set(i,new Metadata(collectionName,nextId));
            }
        }
        if(nextId==0) throw new DBException("Collection name not Found");
        return nextId;
    }
    private void writeMetaDataToDB() {
        ExceptionHandler.context(()-> {
            File metadataFile = new File(Paths.get(DATABASE_FOLDER, METADATA_FILENAME).toString());
            try {
                this.jsonIO.writeValue(metadataFile, this.metadataList);
            } catch (IOException e) {
                throw new DBException(e.getMessage());
            }
        });
    }
    private void writeCollectionToDB(String collectionName)  {
        ExceptionHandler.context(()->{
            File file = new File(Paths.get(DATABASE_FOLDER,collectionName+FILE_EXTENSION).toString());
            try {
                this.jsonIO.writeValue(file, this.data.get(collectionName));
            } catch (IOException e) {
                throw new DBException(e.getMessage());
            }
            this.writeMetaDataToDB();
        });
    }
    private void readCollectionFromDB(String collectionName) {
        ExceptionHandler.context(()-> {
            File file = new File(Paths.get(DATABASE_FOLDER, collectionName+FILE_EXTENSION).toString());
            Map<Long, Object> map;
            try {
                Class<?> clazz = Configuration.COLLECTIONS.stream()
                        .filter(c -> getNameFromClazz(c).equals(collectionName))
                        .findFirst()
                        .orElseThrow(() -> new DBException("Class not found for collection: " + collectionName));
                map = this.jsonIO.readValue(file, this.jsonIO.getTypeFactory().constructMapType(
                        Map.class, Long.class, clazz));
            } catch (IOException e) {
                throw new DBException(e.getMessage());
            }
            this.data.put(collectionName, map);
        });
    }
    public Map<Long,Object> getCollection(Class<?> collectionClass){
        return this.data.get(getNameFromClazz(collectionClass));
    }
    public void setCollection(Class<?> collectionClass,Map<Long,Object> collection) {
        this.data.put(getNameFromClazz(collectionClass),collection);
        this.writeCollectionToDB(getNameFromClazz(collectionClass));
    }

}
