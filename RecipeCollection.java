import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecipeCollection implements IRecipeOperations {
 //this class implements the IRecipeOperations interface and stores all recipes in a list

 private List<Recipe> recipes;

 public RecipeCollection() {
  this.recipes = new ArrayList<>();
 }

 @Override
 public void addRecipe(Recipe recipe) {
  //adds a new recipe if it's not null. null = no object in memory or absence of value
  if (recipe != null) {
   recipes.add(recipe);
  }
 }

 @Override
 public List<Recipe> viewAllRecipes() {
  //returns a read-only list
  return Collections.unmodifiableList(recipes);
 }

 @Override
 public List<Recipe> searchRecipes(String searchOption, String userInput) {
  //search by various criteria
  List<Recipe> results = new ArrayList<>();
  if (searchOption == null || userInput == null) return results;

  switch (searchOption.toLowerCase()) {
   case "single ingredient":
    for (Recipe r : recipes) {
        //check each ingredient in the recipe to see if it contains the user's search text
        boolean found = false;
        for (String ing : r.getIngredients()) {
            //if the recipe ingredient contains the search text, mark it as found
            if (ing.contains(userInput.toLowerCase().trim())) {
                results.add(r);
                found = true;
                break;
            }
        }
    }
    break;
   case "recipe name (exact)":
    for (Recipe r : recipes) {
     if (r.getRecipeName().equalsIgnoreCase(userInput)) {
      results.add(r);
     }
    }
    break;
   case "recipe name (partial)":
    for (Recipe r : recipes) {
     if (r.getRecipeName().toLowerCase().contains(userInput.toLowerCase())) {
      results.add(r);
     }
    }
    break;
   case "meal type":
    for (Recipe r : recipes) {
     if (r.getMealType().equalsIgnoreCase(userInput)) {
      results.add(r);
     }
    }
    break;
   default:
    //unrecognized search option
    break;
  }
  return results;
 }

 @Override
 public List<Recipe> searchRecipesMultipleIngredients(List<String> ingredientsList) {
    List<Recipe> results = new ArrayList<>();
    if (ingredientsList == null || ingredientsList.isEmpty()) return results;

    for (Recipe r : recipes) {
        boolean containsAll = true;

        for (String searchTerm : ingredientsList) {
            searchTerm = searchTerm.trim().toLowerCase();
            boolean foundThisTermInRecipe = false;

            //check if this recipe has an ingredient containing the searchTerm
            for (String ing : r.getIngredients()) {
                if (ing.contains(searchTerm)) {
                    foundThisTermInRecipe = true;
                    break;
                }
            }
            //if never found this searchTerm in the recipe’s ingredients, break
            if (!foundThisTermInRecipe) {
                containsAll = false;
                break;
            }
        }
        //if the recipe satisfied all search terms, add it to our results
        if (containsAll) {
            results.add(r);
        }
    }
    return results;
 }

 @Override
 public List<Recipe> sortRecipes(String sortChoice, String... fields) {
  //returns a new list that is sorted, leaving the original list unchanged
  List<Recipe> sortedList = new ArrayList<>(recipes);

  if (sortChoice.equalsIgnoreCase("single") && fields.length > 0) {
   switch (fields[0].toLowerCase()) {
    case "preptime":
     sortedList.sort(Comparator.comparingInt(Recipe::getPrepTime));
     break;
    case "cooktime":
     sortedList.sort(Comparator.comparingInt(Recipe::getCookTime));
     break;
    case "rating":
     sortedList.sort(Comparator.comparingInt(Recipe::getRating).reversed());
     break;
    case "difficulty":
     //order is easy < medium < hard
     sortedList.sort(Comparator.comparing(r -> r.getDifficulty().toLowerCase(), (d1, d2) -> {
      List<String> order = List.of("easy", "medium", "hard");
      return Integer.compare(order.indexOf(d1), order.indexOf(d2));
     }));
     break;
    default:
     break;
   }
  } else if (sortChoice.equalsIgnoreCase("multiple") && fields.length == 2) {
   //sort by two fields in sequence, e.g., rating & cook time
   String firstField = fields[0].toLowerCase();
   String secondField = fields[1].toLowerCase();

   sortedList.sort((r1, r2) -> {
    int primaryResult = compareByField(r1, r2, firstField);
    if (primaryResult != 0) {
     return primaryResult;
    }
    return compareByField(r1, r2, secondField);
   });
  }

  return sortedList;
 }

 private int compareByField(Recipe r1, Recipe r2, String field) {
  //multi-field sorting comparison
  switch (field) {
   case "preptime":
    return Integer.compare(r1.getPrepTime(), r2.getPrepTime());
   case "cooktime":
    return Integer.compare(r1.getCookTime(), r2.getCookTime());
   case "rating":
    //descending
    return Integer.compare(r2.getRating(), r1.getRating());
   case "difficulty":
    List<String> order = List.of("easy", "medium", "hard");
    return Integer.compare(order.indexOf(r1.getDifficulty().toLowerCase()),
                           order.indexOf(r2.getDifficulty().toLowerCase()));
   default:
    return 0;
  }
 }

 @Override
 public void modifyRecipe(String targetRecipeName, String fieldToModify, String newValue) {
  //updates a single field in the matching recipe
  for (Recipe recipe : recipes) { //loops through the list of recipes
   if (recipe.getRecipeName().equalsIgnoreCase(targetRecipeName)) { //checks if recipe name matches
    switch (fieldToModify.toLowerCase()) { //determines which field to update
     case "recipename": //if field is recipe name
      recipe.setRecipeName(newValue); //updates the recipe name
      break;
     case "cuisinetype":
      recipe.setCuisineType(newValue);
      break;
     case "mealtype":
      recipe.setMealType(newValue);
      break;
     case "preparationsteps":
      recipe.setPreparationSteps(newValue);
      break;
     case "notes":
      recipe.setNotes(newValue);
      break;
     case "difficulty":
      recipe.setDifficulty(newValue);
      break;
     case "rating":
      recipe.setRating(Integer.parseInt(newValue));
      break;
     case "preptime":
      recipe.setPrepTime(Integer.parseInt(newValue));
      break;
     case "cooktime":
      recipe.setCookTime(Integer.parseInt(newValue));
      break;
     case "ingredients":
      List<String> newList = List.of(newValue.split(","));
      recipe.setIngredients(newList);
      break;
     default: //if field is not recognized
      break; //do nothing
    }
    return; //only modify the first match
   }
  }
 }

 @Override
 public void modifyEntireRecipe(String targetRecipeName, Recipe newRecipeData) {
  //replaces the entire recipe object with new data
  for (int i = 0; i < recipes.size(); i++) { //loops through list of recipes
   if (recipes.get(i).getRecipeName().equalsIgnoreCase(targetRecipeName)) {
    recipes.set(i, newRecipeData); //replaces old recipe with new one
    return; //stops after replacing first match
   }
  }
 }

  @Override
 public void exportRecipes(String fileName) throws IOException {
  //writes all recipes to a file
  if (recipes.isEmpty()) { //checks if there are no recipes
   try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
    writer.write(""); //clear file by writing an empty string
   }
   System.out.println("No recipes to export, file cleared: " + fileName);
   return;
  }
  try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
   //opens file for writing
   for (Recipe r : recipes) {//loops through all recipes
    writer.write(r.toString());//writes the details to the file
    writer.write("----------------------\n");//adds a separator between the recipes
   }
  }
  System.out.println("Recipes exported successfully to " + fileName);
 }

 @Override
 public void loadRecipes(String fileName) throws IOException {
  //reads recipes from the file and appends them to the internal list
  File file = new File(fileName);
  if (!file.exists()) {
   //if file doesn't exist, informs user and exits
   System.out.println("No autosave file found, starting with an empty collection.");
   return;
  }

  try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
   String line; //opens file and stores each line read from it
   List<String> chunk = new ArrayList<>();
   //we accumulate lines for a single recipe until "----------------------"
   //separator line between recipes

   while ((line = reader.readLine()) != null) { //reads lines until end of file
    if (line.trim().equals("----------------------")) { //checks for separators
     //parse the chunk as a single recipe
     Recipe parsed = parseRecipeChunk(chunk);
     if (parsed != null) { //checks of parsing was successful
      recipes.add(parsed); //adds the parsed recipe to the list
     }
     chunk.clear(); //clears chunk for next recipe
    } else {
     chunk.add(line);
    }
   }
   //if there's any leftover chunk, parse it
   if (!chunk.isEmpty()) {
    Recipe parsed = parseRecipeChunk(chunk);
    if (parsed != null) {
     recipes.add(parsed);
    }
   }
   System.out.println("Loaded recipes from " + fileName);
  }
 }

 private Recipe parseRecipeChunk(List<String> lines) {
  //parses lines of text that match the toString() output of a recipe

  String recipeName = null; //stores each type, e.g. recipename, cuisine, etc
  String cuisine = null;
  String mealType = null;
  List<String> ingList = new ArrayList<>();
  String steps = null;
  int prepT = 0;
  int cookT = 0;
  String diff = null;
  int rat = 0;
  String note = null;

  for (String l : lines) { //loops through all lines
   l = l.trim(); //removes extra spaces
   if (l.startsWith("Recipe Name:")) {
       //checking the start of each line
    recipeName = l.substring("Recipe Name:".length()).trim();
   } else if (l.startsWith("Cuisine:")) {
    cuisine = l.substring("Cuisine:".length()).trim();
   } else if (l.startsWith("Meal Type:")) {
    mealType = l.substring("Meal Type:".length()).trim().toLowerCase();
   } else if (l.startsWith("Ingredients:")) {
    //extract raw ingredient string
    String raw = l.substring("Ingredients:".length()).trim();
    //remove square brackets if present
    raw = raw.replaceAll("\\[|\\]", "");
    String[] splitted = raw.split(","); //splits the ingredients
    for (String s : splitted) { //loops through each ingredient
     ingList.add(s.trim().toLowerCase()); //adds the ingredient to a list
    }
   } else if (l.startsWith("Preparation Steps:")) {
    steps = l.substring("Preparation Steps:".length()).trim();
   } else if (l.startsWith("Prep Time:")) { //check if line starts with prep time
    String tmp = l.substring("Prep Time:".length()).trim(); //extracts the time
    tmp = tmp.replace(" minutes", "").trim(); //removes minutes text
    try {
     prepT = Integer.parseInt(tmp); //converts the string to an int
    } catch (NumberFormatException e) {
     prepT = 0; //sets to 0 if conversion fails
    }
   } else if (l.startsWith("Cook Time:")) {
    String tmp = l.substring("Cook Time:".length()).trim();
    tmp = tmp.replace(" minutes", "").trim();
    try {
     cookT = Integer.parseInt(tmp);
    } catch (NumberFormatException e) {
     cookT = 0; 
    }
   } else if (l.startsWith("Difficulty:")) {
    diff = l.substring("Difficulty:".length()).trim().toLowerCase();
   } else if (l.startsWith("Rating:")) {
    String tmp = l.substring("Rating:".length()).trim();
    try {
     rat = Integer.parseInt(tmp);
    } catch (NumberFormatException e) {
     rat = 0;
    }
   } else if (l.startsWith("Notes:")) {
    note = l.substring("Notes:".length()).trim();
   }
  }

  if (recipeName == null || recipeName.isEmpty()) { //checks if recipe name is missing
   return null; //returns null if the recipe is invalid
  }
  //create and returns a new recipe object with the extracted details
  Recipe loaded = new Recipe(recipeName, cuisine, mealType, ingList, steps,
                             prepT, cookT, diff, rat, note);
  return loaded;
 }
}