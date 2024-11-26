package dp.grp4.models.db;

import dp.grp4.models.entities.Ingredient;
import dp.grp4.models.entities.Recipe;

import java.util.List;

public class Configuration {
    public static final String DATABASE_FOLDER="C:\\Users\\yassi\\Desktop\\JsonDB";
    public static final String METADATA_FILENAME= "metadata.json";
    public static final String FILE_EXTENSION= ".json";
    public static final List<Class<?>> COLLECTIONS=List.of(Ingredient.class,Recipe.class);

}
