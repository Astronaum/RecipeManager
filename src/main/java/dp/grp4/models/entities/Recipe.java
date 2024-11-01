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
    private List<Long> ingredientsIds;
    private List<String> instructionsList;
    private int note; // x/10
    private String comment;
    private boolean favourite;
    public enum Category{
        APPETIZER, MAIN, DESSERT
    }
    public enum Difficulty{
        EASY, MEDIUM, HARD
    }
    public enum Criteria{
        HALAL,VEGETARIAN
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public int getPreparationTime() {
        return preparationTime;
    }
    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }
    public int getCookingTime() {
        return cookingTime;
    }
    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    public Set<Criteria> getCriteriaSet() {
        return criteriaSet;
    }
    public void setCriteriaSet(Set<Criteria> criteriaSet) {
        this.criteriaSet = criteriaSet;
    }
    public List<Long> getIngredientsIds() {
        return ingredientsIds;
    }
    public void setIngredientsIds(List<Long> ingredientsIds) {
        this.ingredientsIds = ingredientsIds;
    }
    public List<String> getInstructionsList() {
        return instructionsList;
    }
    public void setInstructionsList(List<String> instructionsList) {
        this.instructionsList = instructionsList;
    }
    public int getNote() {
        return note;
    }
    public void setNote(int note) {
        this.note = note;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public boolean isFavourite() {
        return favourite;
    }
    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
    public Recipe(Recipe.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.category = builder.category;
        this.preparationTime = builder.preparationTime;
        this.cookingTime = builder.cookingTime;
        this.difficulty = builder.difficulty;
        this.criteriaSet = builder.criteriaSet;
        this.ingredientsIds = builder.ingredientsIds;
        this.instructionsList = builder.instructionsList;
        this.note = builder.note;
        this.comment = builder.comment;
        this.favourite = builder.favourite;
    }
    public static Recipe.Builder builder(){
        return new Recipe.Builder();
    }
    public static class Builder{
        private long id;
        private String name;
        private Category category;
        private int preparationTime; // minutes
        private int cookingTime; // minutes
        private Difficulty difficulty;
        private Set<Criteria> criteriaSet;
        private List<Long> ingredientsIds;
        private List<String> instructionsList;
        private int note; // x/10
        private String comment;
        private boolean favourite;

        public Recipe.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Recipe.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Recipe.Builder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Recipe.Builder setPreparationTime(int preparationTime) {
            this.preparationTime = preparationTime;
            return this;
        }

        public Recipe.Builder setCookingTime(int cookingTime) {
            this.cookingTime = cookingTime;
            return this;
        }

        public Recipe.Builder setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Recipe.Builder setCriteriaSet(Set<Criteria> criteriaSet) {
            this.criteriaSet = criteriaSet;
            return this;
        }

        public Recipe.Builder setIngredientsIds(List<Long> ingredientsIds) {
            this.ingredientsIds = ingredientsIds;
            return this;
        }

        public Recipe.Builder setInstructionsList(List<String> instructionsList) {
            this.instructionsList = instructionsList;
            return this;
        }

        public Recipe.Builder setNote(int note) {
            this.note = note;
            return this;
        }

        public Recipe.Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Recipe.Builder setFavourite(boolean favourite) {
            this.favourite = favourite;
            return this;
        }
        public Recipe build(){
            return new Recipe(this);
        }
    }
}
