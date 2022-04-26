import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A simple model for predator species to inherit from.
 * Predator species age, breed, and hunt for prey
 *
 * @author David J. Barnes and Michael Kölling and Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */
public class Predator extends Animal {
    // class variables
    private  int BREEDING_AGE;
    private int MAX_AGE;
    private double BREEDING_PROBABILITY;
    private int MAX_LITTER_SIZE;
    private static int STARTING_FOOD_LEVEL;

    // A shared random number generator to control breeding.
    protected static Random rand = Randomizer.getRandom();

    // instance fields
    private int age;
    private int foodLevel;

    /**
     * Create a predator as a newborn (age zero and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the predator will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Predator(boolean randomAge, Field field, Location location, boolean gender) {
        super(field, location);
        STARTING_FOOD_LEVEL = getBaseValue("startingFoodLevel");
        if(randomAge) {
            setAge(rand.nextInt(20)+1);
            foodLevel = rand.nextInt(STARTING_FOOD_LEVEL);
        }
        else{
            age = 0;
            foodLevel = STARTING_FOOD_LEVEL;
        }
        setGender(gender);
    }

    /**
     * Increase the age of the predator, potentially resulting in its death
     */
    protected void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this predator more hungry, potentially resulting in its death
     */
    protected void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for prey adjacent to the current location.
     * Only the first live instance of a prey species is eaten.
     * @return The location of prey, or null if no prey was present.
     */
    protected Location findFood(){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Prey) {
                Animal prey = (Prey) animal;
                if(prey.isAlive()) {
                    prey.setDead();
                    foodLevel += prey.getNutritionalValue();
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check areas adjacent to the predator for a diseased animal
     * If there is a diseased animal nearby, the predator will become diseased as well
     * If an animal is diseased, there is a probability of it dying.
     */
    protected void checkDisease(){
        if(isAlive()) {
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());

            for (Location where : adjacent) {
                Object animal = field.getObjectAt(where);
                if (animal instanceof Animal && ((Animal) animal).getIsDiseased()) {
                    this.setIsDiseased(true);
                }
            }
            checkDiseaseDeath();
        }
    }


    /**
     * Generate a number representing the number of births, if it can breed.
     * @return The number of births
     */
    protected int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }


    /**
     * A predator can breed if it has reached the breeding age.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }

    /**
     * @return The food level of the predator
     */
    public int getFoodLevel() {
        return foodLevel;
    }

    /**
     * Updates the animal's food level and consequent number of steps it can go without food
     * @param newFoodLevel updated food level
     */
    public void setFoodLevel(int newFoodLevel){
        foodLevel = newFoodLevel;
    }

    /**
     * Make this animal act
     * @param newAnimals A list to receive newly born animals.
     */
    @Override
    public void act(List<Animal> newAnimals) {}

    /**
     * Make this animal act at night
     * @param newAnimals A list to receive newly born animals.
     */
    @Override
    public void nightAct(List<Animal> newAnimals) {}

    /**
     * Make this animal act in cases of cold water
     * @param newAnimals A list to receive newly born animals.
     */
    public void coldWatersAct(List<Animal> newAnimals) {}

    /**
     * Make this animal act in cases of strong currents
     * @param newAnimals A list to receive newly born animals.
     */
    public void strongCurrentsAct(List<Animal> newAnimals) {}

    /**
     * Sets the age at which a predator can begin breeding
     * @param age breeding age
     */
    public void setBREEDING_AGE(int age) {
        this.BREEDING_AGE = age;
    }

    /**
     * Sets the maximum number of offspring it can produce while breeding every round
     * @param size maximum litter size
     */
    public void setMAX_LITTER_SIZE(int size){
        this.MAX_LITTER_SIZE = size;
    }

    /**
     * Sets the probability of successful reproduction when a male and female of a species meet
     * @param probability probability of breeding
     */
    public void setBREEDING_PROBABILITY(double probability){
        this.BREEDING_PROBABILITY = probability;
    }

    /**
     * Sets the maximum age a predator can live, regardless of external factors
     * @param age maximum age
     */
    public void setMAX_AGE(int age){
        this.MAX_AGE = age;
    }
}
