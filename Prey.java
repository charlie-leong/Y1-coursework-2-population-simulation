import java.util.List;
import java.util.Random;

/**
 * A simple model of prey species.
 * Prey age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling and Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */
public class Prey extends Animal {
    // Characteristics shared by all prey species (class variables).
    private int BREEDING_AGE ;
    private int MAX_AGE = 20;
    private double BREEDING_PROBABILITY ;
    private int MAX_LITTER_SIZE ;

    // A shared random number generator to control breeding.
    private final Random rand = Randomizer.getRandom();

    private int age;
    private int hungerLevel=0; //the lower the hunger value the better

    //the field that the plants eaten by prey exist on
    protected Field plantField;

    /**
     * Create a new prey animal with age zero (a newborn) or with a random age.
     *
     * @param randomAge If true, the prey will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Prey(boolean randomAge, Field field, Location location, boolean gender, Field plantField) {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        setGender(gender);
        this.plantField = plantField;

    }

    /**
     * Make this prey animal act
     * @param newPrey A list to receive newly born animals.
     */
    public void act(List<Animal> newPrey) {}

    /**
     * Make the animal act at night
     * @param newPrey A list to receive newly born animals.
     */
    public void nightAct(List<Animal> newPrey) {}

    /**
     * Make this animal act in cases of cold water
     * @param newPrey A list to receive newly born animals.
     */
    public void coldWatersAct(List<Animal> newPrey) {}

    /**
     * Make this animal act in cases of strong currents
     * @param newPrey A list to receive newly born animals.
     */
    public void strongCurrentsAct(List<Animal> newPrey) {
        if (isAlive()) {
            Location location = this.getLocation();
            setLocation(location);
        }
    }

    /**
     * Increase the age.
     * This could result in the prey's death.
     */
    protected void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Check whether this prey is to give birth at this step.
     * New births will be added into free adjacent locations.
     * @param newPrey A list to return newly born prey animals.
     */
    protected void giveBirth(List<Animal> newPrey) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Prey young = new Prey(false, field, loc, getGender(), plantField);
            newPrey.add(young);
        }
    }

    /**
     * Generate a number representing the number of births, if breeding is possible
     * @return The number of births.
     */
    protected int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * Allows prey species to find and eat plants from the designated plant field
     * Eating plants decreases the animal's hunger level
     */
    protected void findPlant(){
        Field field = plantField;
        Location where = getLocation();
        Object plant = field.getObjectAt(where);
        if(hungerLevel>=0 && plant instanceof Plant){
            Plant food = (Plant) plant;
            food.incrementEatenCounter();
            hungerLevel--; // decrease the animal's hunger level
        }
    }

    /**
     * A prey animal can breed if it has reached the breeding age.
     * @return true if the prey can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }

    /**
     * Allows the prey's breeding age to be set
     * @param age the preys breeding age
     */
    public void setBREEDING_AGE(int age) {
        this.BREEDING_AGE = age;
    }

    /**
     * Set the prey species' maximum litter size (the maximum number of offspring that can be produced in one turn)
     * @param size maximum litter size
     */
    public void setMAX_LITTER_SIZE(int size){
        this.MAX_LITTER_SIZE = size;
    }

    /**
     * Set the probability of successful breeding between two compatible animals
     * @param probability successful breeding probability
     */
    public void setBREEDING_PROBABILITY(double probability){
        this.BREEDING_PROBABILITY = probability;
    }

    /**
     * Set the maximum age that an animal can live up to, external from environmental factors
     * @param age an animal's maximum age
     */
    public void setMAX_AGE(int age){
        this.MAX_AGE = age;
    }

    /**
     * increments an animal's hunger level, potentially resulting in its death
     */
    protected void incrementHunger(){
        hungerLevel++;
        if(hungerLevel > getNutritionalValue()){
            setDead();
        }
    }

}
