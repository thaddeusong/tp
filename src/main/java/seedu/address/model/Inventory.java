package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.ingredient.*;
import seedu.address.model.ingredient.exceptions.IngredientNotFoundException;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class Inventory implements ReadOnlyInventory {

    private final UniqueIngredientList ingredients;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        ingredients = new UniqueIngredientList();
    }

    public Inventory() {}

    /**
     * Creates an Inventory using the Ingredients in the {@code toBeCopied}
     */
    public Inventory(ReadOnlyInventory toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the ingredient list with {@code ingredients}.
     * {@code ingredients} must not contain duplicate ingredients.
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients.setIngredients(ingredients);
    }

    /**
     * Resets the existing data of this {@code Inventory} with {@code newData}.
     */
    public void resetData(ReadOnlyInventory newData) {
        requireNonNull(newData);
        setIngredients(newData.getIngredientList());
    }

    //// ingredient-level operations

    /**
     * Returns true if a ingredient with the same identity as {@code ingredient} exists in the address book.
     */
    public boolean hasIngredient(Name ingredientName) {
        requireNonNull(ingredientName);

        for (Ingredient ingredient : ingredients) {
            if(ingredient.getName().equals(ingredientName)) {
                return true;
            }
        }
        return false;
    }

    public Quantity getQuantityOf(Name ingredientName) {
        requireNonNull(ingredientName);
        for (Ingredient ingredient : ingredients) {
            if(ingredient.getName().equals(ingredientName)) {
                return ingredient.getQuantity();
            }
        }
        //If ingredient can't be found, then return 0 quantity
        return new Quantity(0, Unit.GRAM);
    }

    /**
     * Adds a ingredient to the address book.
     * The ingredient must not already exist in the address book.
     */
    public void addIngredient(Ingredient p) {
        ingredients.add(p);
    }

    /**
     * Replaces the given ingredient {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The ingredient identity of {@code editedPerson} must not be the same as another existing ingredient in the address book.
     */
    public void useIngredient(Name ingredientName, Quantity quantity) {
        requireAllNonNull(ingredientName, quantity);
        for (Ingredient ingredient : ingredients) {
            if(ingredient.getName().equals(ingredientName)) {
                ingredient.use(quantity);
                return;
            }
        }
        throw new IngredientNotFoundException();
    }

    /**
     * Removes {@code key} from this {@code Inventory}.
     * {@code key} must exist in the address book.
     */
    public void removeIngredient(Name ingredientName) {
        for (Ingredient ingredient : ingredients) {
            if(ingredient.getName().equals(ingredientName)) {
                removeIngredient(ingredientName);
                break;
            }
        }
        throw new IngredientNotFoundException();
    }

    public void clear() {
        ingredients.clear();
    }
    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", ingredients)
                .toString();
    }

    @Override
    public ObservableList<Ingredient> getIngredientList() {
        return ingredients.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Inventory)) {
            return false;
        }

        Inventory otherInventory = (Inventory) other;
        return ingredients.equals(otherInventory.ingredients);
    }

    @Override
    public int hashCode() {
        return ingredients.hashCode();
    }

}