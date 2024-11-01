package dp.grp4.models.entities;

import java.time.LocalDate;

public class Ingredient {
    private long id;
    private String name;
    private int quantity;
    private Unit unit;
    private LocalDate expirationDate;
    enum Unit{
        KG,g,L
    }
}
