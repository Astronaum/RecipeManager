package dp.grp4.models.entities;

import java.time.LocalDate;

public class Ingredient {
    private long id;
    private String name;
    private int quantity;
    private Unit unit;
    private LocalDate expirationDate;
    public enum Unit{
        KG,g,L
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
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Unit getUnit() {
        return unit;
    }
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
    public Ingredient(Ingredient.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.unit = builder.unit;
        this.expirationDate = builder.expirationDate;
    }
    public static Ingredient.Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private long id;
        private String name;
        private int quantity;
        private Unit unit;
        private LocalDate expirationDate;
        public Ingredient.Builder setId(long id) {
            this.id = id;
            return this;
        }
        public Ingredient.Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Ingredient.Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }
        public Ingredient.Builder setUnit(Unit unit) {
            this.unit = unit;
            return this;
        }
        public Ingredient.Builder setExpirationDate(LocalDate expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }
        public Ingredient build(){
            return new Ingredient(this);
        }
    }
}
