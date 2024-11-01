package dp.grp4.models.entities;

import java.util.List;
import java.util.Set;

public class Recipe {
    private long id;
    private String name;
    private Category category;
    private int preparationTime; // minutes
    private int cookingTime; // minutes
    private Difficulty difficulty;
    private Set<Criteria> criteriaSet;
    private List<Ingredient> ingredientsList;
    private List<String> instructionsList;
    private int note; // x/10
    private String comment;
    private boolean favourite;
    enum Category{
        APPETIZER, MAIN, DESSERT
    }
    enum Difficulty{
        EASY, MEDIUM, HARD
    }
    enum Criteria{
        HALAL,VEGETARIAN
    }
}
