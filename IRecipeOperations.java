import java.io.IOException;
import java.util.List;

public interface IRecipeOperations {
 //this interface defines the contract for all recipe operations

 void addRecipe(Recipe recipe);
 List<Recipe> viewAllRecipes();
 List<Recipe> searchRecipes(String searchOption, String userInput);
 List<Recipe> searchRecipesMultipleIngredients(List<String> ingredientsList);
 List<Recipe> sortRecipes(String sortChoice, String... fields);
 void modifyRecipe(String targetRecipeName, String fieldToModify, String newValue);
 void modifyEntireRecipe(String targetRecipeName, Recipe newRecipeData);
 void exportRecipes(String fileName) throws IOException;
 void loadRecipes(String fileName) throws IOException;
}
