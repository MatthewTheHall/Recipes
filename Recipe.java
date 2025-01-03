import java.util.List;

public class Recipe {
 //this class stores all details about a single recipe and provides get and set for each field

 private String recipeName;
 private String cuisineType;
 private String mealType;
 private List<String> ingredients;
 private String preparationSteps;
 private int prepTime;
 private int cookTime;
 private String difficulty;  
 private int rating;        
 private String notes;

 //default constructor
 public Recipe() {
 }

 //overloaded constructor for creating a recipe in one line
 public Recipe(String recipeName, String cuisineType, String mealType, List<String> ingredients,
               String preparationSteps, int prepTime, int cookTime, String difficulty,
               int rating, String notes) {
  this.recipeName = recipeName;
  this.cuisineType = cuisineType;
  this.mealType = mealType;
  this.ingredients = ingredients;
  this.preparationSteps = preparationSteps;
  this.prepTime = prepTime;
  this.cookTime = cookTime;
  this.difficulty = difficulty;
  this.rating = rating;
  this.notes = notes;
 }

 //get and set for each field
 public String getRecipeName() {
  return recipeName;
 }

 public void setRecipeName(String recipeName) {
  this.recipeName = recipeName;
 }

 public String getCuisineType() {
  return cuisineType;
 }

 public void setCuisineType(String cuisineType) {
  this.cuisineType = cuisineType;
 }

 public String getMealType() {
  return mealType;
 }

 public void setMealType(String mealType) {
  this.mealType = mealType;
 }

 public List<String> getIngredients() {
  return ingredients;
 }

 public void setIngredients(List<String> ingredients) {
  this.ingredients = ingredients;
 }

 public String getPreparationSteps() {
  return preparationSteps;
 }

 public void setPreparationSteps(String preparationSteps) {
  this.preparationSteps = preparationSteps;
 }

 public int getPrepTime() {
  return prepTime;
 }

 public void setPrepTime(int prepTime) {
  this.prepTime = prepTime;
 }

 public int getCookTime() {
  return cookTime;
 }

 public void setCookTime(int cookTime) {
  this.cookTime = cookTime;
 }

 public String getDifficulty() {
  return difficulty;
 }

 public void setDifficulty(String difficulty) {
  this.difficulty = difficulty;
 }

 public int getRating() {
  return rating;
 }

 public void setRating(int rating) {
  this.rating = rating;
 }

 public String getNotes() {
  return notes;
 }

 public void setNotes(String notes) {
  this.notes = notes;
 }

 //method for printing & exporting recipe details
 @Override
 public String toString() {
  return "Recipe Name: " + recipeName + "\n"
         + "Cuisine: " + cuisineType + "\n"
         + "Meal Type: " + mealType + "\n"
         + "Ingredients: " + ingredients + "\n"
         + "Preparation Steps: " + preparationSteps + "\n"
         + "Prep Time: " + prepTime + " minutes\n"
         + "Cook Time: " + cookTime + " minutes\n"
         + "Difficulty: " + difficulty + "\n"
         + "Rating: " + rating + "\n"
         + "Notes: " + notes + "\n";
 }
}
