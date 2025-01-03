import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RecipeApp {
 //this class contains the main method, automatically loads from recipesautosave.txt, and handles user interaction

 public static void main(String[] args) {
  Scanner scanner = new Scanner(System.in); //read user input
  IRecipeOperations recipeCollection = new RecipeCollection(); //manage recipe operation

  //automatically load from recipesautosave.txt if it exists
  try {
   recipeCollection.loadRecipes("recipesautosave.txt");
  } catch (IOException e) {
    //error message if recipes can't be loaded at startup
   System.out.println("Error loading recipes on startup: " + e.getMessage());
  }

  String userCommand = ""; //stores users command
  while (!userCommand.equalsIgnoreCase("exit")) {
   //display menu options to the user
   System.out.println("\nSelect an option:");
   System.out.println("1. Add Recipe");
   System.out.println("2. View All Recipes");
   System.out.println("3. Search Recipes");
   System.out.println("4. Sort Recipes");
   System.out.println("5. Modify Recipe");
   System.out.println("Type 'exit' to quit");
   System.out.print("> ");

   userCommand = scanner.nextLine().trim(); //reads users input and removes extra spaces

   switch (userCommand.toLowerCase()) {
    case "1":
    case "add recipe": //user can choose to type 1 or add recipe
     addRecipeFlow(recipeCollection, scanner);
     break;
    case "2":
    case "view all recipes":
     viewAllFlow(recipeCollection);
     break;
    case "3":
    case "search recipes":
     searchRecipeFlow(recipeCollection, scanner);
     break;
    case "4":
    case "sort recipes":
     sortRecipeFlow(recipeCollection, scanner);
     break;
    case "5":
    case "modify recipe":
     modifyRecipeFlow(recipeCollection, scanner);
     break;
    case "exit":
     System.out.println("Goodbye!");
     break;
    default:
     System.out.println("Invalid command. Please try again.");
   }
  }
  scanner.close(); //closes scanner after loop ends
 }

 private static void addRecipeFlow(IRecipeOperations recipeCollection, Scanner scanner) {
  //collects user input for each field, performs validation, then adds a new recipe

  System.out.println("Enter recipe name:");
  String recipeName = scanner.nextLine();

  System.out.println("Enter cuisine type (Italian, French, Japanese, American, etc.):");
  String cuisineType = scanner.nextLine();

  System.out.println("Enter meal type (breakfast, lunch, dinner, snack):");
  String mealType = scanner.nextLine().trim().toLowerCase();
  while (!mealType.equals("breakfast") && !mealType.equals("lunch")
         && !mealType.equals("dinner") && !mealType.equals("snack")) { //checks if meal type is valid
   System.out.println("Invalid meal type. please enter 'breakfast', 'lunch', 'dinner', or 'snack':");
   mealType = scanner.nextLine().trim().toLowerCase(); //asks for input again if invalid
  }

  System.out.println("Enter ingredients (comma separated):"); //asking user for ingredients
  String ingString = scanner.nextLine();
  List<String> ingredients = new ArrayList<>();
  for (String ing : ingString.split(",")) {
   ingredients.add(ing.trim().toLowerCase()); //splits, then trims, and adds each ingredient to list
  }

  System.out.println("Enter preparation steps:");//asks for preparation steps
  String preparationSteps = scanner.nextLine(); //reads the steps

  System.out.println("Enter preparation time in minutes:");
  int prepTime;
  while (true) { //repeats until provided a valid number
   try {
    prepTime = Integer.parseInt(scanner.nextLine().trim());
    if (prepTime < 0) { //checks if negative, also ensures it's a positive number instead of text
     throw new NumberFormatException("Prep time cannot be negative.");
    }
    break;
   } catch (NumberFormatException e) {
    System.out.println("Invalid input. please enter a non-negative integer for preparation time:");
   }
  }

  System.out.println("Enter cooking time in minutes:");
  int cookTime;
  while (true) {
   try {
    cookTime = Integer.parseInt(scanner.nextLine().trim());
    if (cookTime < 0) {
     throw new NumberFormatException("Cook time cannot be negative.");
    }
    break;
   } catch (NumberFormatException e) {
    System.out.println("Invalid input. please enter a non-negative integer for cooking time:");
   }
  }

  System.out.println("Enter difficulty (easy, medium, hard):");//asks for difficulty level
  String difficulty = scanner.nextLine().trim().toLowerCase();//reads input
  while (!difficulty.equals("easy") && !difficulty.equals("medium") && !difficulty.equals("hard")) { //validates input
   System.out.println("Invalid difficulty. please enter 'easy', 'medium', or 'hard':");
   difficulty = scanner.nextLine().trim().toLowerCase();//asks for input again if invalid
  }

  System.out.println("Enter personal rating (1 to 5):");
  int rating;
  while (true) { //repeats until valid number is provided
   try {
    rating = Integer.parseInt(scanner.nextLine().trim());
    if (rating < 1 || rating > 5) { //checks if rating is between 1 and 5
     throw new NumberFormatException("Rating must be between 1 and 5.");
    }
    break; //breaks loop if invalid input
   } catch (NumberFormatException e) { //handles invalid input
    System.out.println("Invalid rating. please enter a number between 1 and 5:");
   }
  }

  System.out.println("Enter notes (for modifications or tips):");
  String notes = scanner.nextLine();
  
  //creates new recipe with provided details
  Recipe newRecipe = new Recipe(recipeName, cuisineType, mealType, ingredients,
                                preparationSteps, prepTime, cookTime, difficulty,
                                rating, notes);

  recipeCollection.addRecipe(newRecipe);//adds recipe to the collection

  //autosave to recipesautosave.txt right after adding
  try {
   recipeCollection.exportRecipes("recipesautosave.txt");
  } catch (IOException e) {
   System.out.println("Warning: failed to autosave recipe: " + e.getMessage());
  }

  System.out.println("Recipe added successfully!");
 }

 private static void viewAllFlow(IRecipeOperations recipeCollection) {
  //displays all recipes if available
  List<Recipe> allRecipes = recipeCollection.viewAllRecipes(); //retrieves the list
  if (allRecipes.isEmpty()) { //checks if empty list
   System.out.println("No recipes available."); //informs if no recipes available
  } else {
   for (Recipe r : allRecipes) { //loops through and displays each recipe
    System.out.println(r);
    System.out.println("----------------------"); //visual separator between recipes for readability
   }
  }
 }

 private static void searchRecipeFlow(IRecipeOperations recipeCollection, Scanner scanner) {
  //prompts user for search type and prints matching results
  System.out.println("Search by:"); //search options
  System.out.println("1. Single ingredient");
  System.out.println("2. Multiple ingredients");
  System.out.println("3. Recipe name (exact)");
  System.out.println("4. Recipe name (partial)");
  System.out.println("5. Meal type");
  System.out.print("> ");

  String option = scanner.nextLine().trim().toLowerCase();
  List<Recipe> results = new ArrayList<>(); //creates a list to store results

  switch (option) { //determines which search type to use based on input
   case "1":
   case "single ingredient":
    System.out.print("Enter ingredient: ");
    String singleIng = scanner.nextLine();
    results = recipeCollection.searchRecipes("single ingredient", singleIng);
    break;
   case "2":
   case "multiple ingredients":
    System.out.print("Enter ingredients (comma separated): ");
    String multiIng = scanner.nextLine();
    List<String> ingList = Arrays.asList(multiIng.split(","));
    results = recipeCollection.searchRecipesMultipleIngredients(ingList);
    break;
   case "3":
   case "recipe name (exact)":
    System.out.print("Enter exact recipe name: ");
    String exactName = scanner.nextLine();
    results = recipeCollection.searchRecipes("recipe name (exact)", exactName);
    break;
   case "4":
   case "recipe name (partial)":
    System.out.print("Enter partial name: ");
    String partialName = scanner.nextLine();
    results = recipeCollection.searchRecipes("recipe name (partial)", partialName);
    break;
   case "5":
   case "meal type":
    System.out.print("Enter meal type: ");
    String meal = scanner.nextLine();
    results = recipeCollection.searchRecipes("meal type", meal);
    break;
   default:
    System.out.println("Invalid search option.");
    return;
  }

  if (results.isEmpty()) { //checks if search returned no results
   System.out.println("No matches found.");
  } else {
   System.out.println("Search results:");
   for (Recipe r : results) { //loops through and displays each matching result
    System.out.println(r); //prints details
    System.out.println("----------------------"); //visual separator
   }
  }
 }

 private static void sortRecipeFlow(IRecipeOperations recipeCollection, Scanner scanner) {
  //prompts user for sorting option and prints sorted results
  System.out.println("Sort by:"); 
  System.out.println("1. Single field");
  System.out.println("2. Multiple fields");
  System.out.print("> ");

  String sortType = scanner.nextLine().trim().toLowerCase(); //read user choice
  List<Recipe> sortedResults; //store sorted results

  switch (sortType) { //determine which sorting method to use from user input
   case "1":
   case "single field":
    System.out.println("Sort by (prepTime, cookTime, rating, difficulty): ");
    String field = scanner.nextLine().trim();
    sortedResults = recipeCollection.sortRecipes("single", field);
    break;
   case "2":
   case "multiple fields":
    System.out.println("Enter first field (prepTime, cookTime, rating, difficulty): ");
    String firstField = scanner.nextLine().trim();
    System.out.println("Enter second field (prepTime, cookTime, rating, difficulty): ");
    String secondField = scanner.nextLine().trim();
    sortedResults = recipeCollection.sortRecipes("multiple", firstField, secondField);
    break;
   default:
    System.out.println("Invalid sort choice.");
    return;
  }

  if (sortedResults != null && !sortedResults.isEmpty()) { //checks if search returned any results
   for (Recipe r : sortedResults) { //loops and prints each sorted recipe
    System.out.println(r);
    System.out.println("----------------------");
   }
  } else {
   System.out.println("No recipes to sort or invalid fields.");
  }
 }

 private static void modifyRecipeFlow(IRecipeOperations recipeCollection, Scanner scanner) {
  //allows user to modify either a single field or the entire recipe
  System.out.print("Enter the recipe name to modify: ");
  String target = scanner.nextLine(); //read recipe name

  System.out.println("Modify: (recipeName, cuisineType, mealType, ingredients, preparationSteps,"
                     + " prepTime, cookTime, difficulty, rating, notes, or entire recipe)");
  String field = scanner.nextLine(); //reads the field to modify

  if (field.equalsIgnoreCase("entire recipe")) {
   //asks user re-enter all fields
   System.out.println("Enter new recipe name:");
   String recipeName = scanner.nextLine();

   System.out.println("Enter new cuisine type:");
   String cuisine = scanner.nextLine();

   System.out.println("Enter new meal type (breakfast, lunch, dinner, snack):");
   String meal = scanner.nextLine().trim().toLowerCase();
   while (!meal.equals("breakfast") && !meal.equals("lunch") //checking for valid input
          && !meal.equals("dinner") && !meal.equals("snack")) {
    System.out.println("Invalid meal type. please enter 'breakfast', 'lunch', 'dinner', or 'snack':");
    meal = scanner.nextLine().trim().toLowerCase();
   }

   System.out.println("Enter new ingredients (comma separated):");
   String ingString = scanner.nextLine(); //read ingredients
   List<String> ingredients = new ArrayList<>(); //add to list
   for (String ing : ingString.split(",")) {
    ingredients.add(ing.trim().toLowerCase());
   }

   System.out.println("Enter new preparation steps:");
   String steps = scanner.nextLine();

   System.out.println("Enter new prep time:");
   int prepT;
   while (true) { //loop to make sure prep time is a valid input, not negative number or word
    try {
     prepT = Integer.parseInt(scanner.nextLine().trim());
     if (prepT < 0) {
      throw new NumberFormatException("Prep time cannot be negative.");
     }
     break;
    } catch (NumberFormatException e) {
     System.out.println("Invalid input. please enter a non-negative integer for prep time:");
    }
   }

   System.out.println("Enter new cook time:");
   int cookT;
   while (true) { //loop for cook time to ensure not a negative number or word
    try {
     cookT = Integer.parseInt(scanner.nextLine().trim());
     if (cookT < 0) {
      throw new NumberFormatException("Cook time cannot be negative.");
     }
     break;
    } catch (NumberFormatException e) {
     System.out.println("Invalid input. please enter a non-negative integer for cook time:");
    }
   }

   System.out.println("Enter new difficulty (easy, medium, hard):");
   String diff = scanner.nextLine().trim().toLowerCase(); 
   //ensuring difficulty is either easy, medium, or hard input only.
   while (!diff.equals("easy") && !diff.equals("medium") && !diff.equals("hard")) {
    System.out.println("Invalid difficulty. please enter 'easy', 'medium', or 'hard':");
    diff = scanner.nextLine().trim().toLowerCase();
   }

   System.out.println("Enter new rating (1 to 5):");
   int rat;
   while (true) { //loop to ensure rating is between 1-5
    try {
     rat = Integer.parseInt(scanner.nextLine().trim());
     if (rat < 1 || rat > 5) {
      throw new NumberFormatException("Rating must be between 1 and 5.");
     }
     break;
    } catch (NumberFormatException e) {
     System.out.println("Invalid rating. please enter a number between 1 and 5:");
    }
   }

   System.out.println("Enter new notes:");
   String note = scanner.nextLine();

   Recipe updatedRecipe = new Recipe(recipeName, cuisine, meal, ingredients, steps,
                                     prepT, cookT, diff, rat, note);
   recipeCollection.modifyEntireRecipe(target, updatedRecipe);
   System.out.println("Recipe updated successfully!");

   //autosave after modifying
   try {
    recipeCollection.exportRecipes("recipesautosave.txt");
   } catch (IOException e) {
    System.out.println("Warning: failed to autosave modified recipe: " + e.getMessage());
   }

  } else {
   //user modifies just one field
   System.out.println("Enter the new value:");
   String newValue = scanner.nextLine(); //reads new value
   recipeCollection.modifyRecipe(target, field, newValue); //updates specified field
   System.out.println("Recipe updated successfully!");

   //autosave after modifying
   try {
    recipeCollection.exportRecipes("recipesautosave.txt"); //saves recipes to file
   } catch (IOException e) { //handles errors during saving
    System.out.println("Warning: failed to autosave modified recipe: " + e.getMessage());
   }
  }
 }
}
