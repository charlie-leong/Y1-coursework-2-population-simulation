import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * This class provides a representation of helper fish as a prey species
 * It contains the characteristics of helper fish
 * @author Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */

public class HelperFish extends Prey{

    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a helper fish instance as a newborn (age zero and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the fish will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param gender    The gender of the helper fish
     */
    public HelperFish(boolean randomAge, Field field, Location location, boolean gender, Field plantField) {
        super(randomAge, field, location, gender, plantField);
        setBREEDING_AGE(getBaseValue("helperBreedingAge"));
        setMAX_AGE(getBaseValue("helperMaxAge"));
        setBREEDING_PROBABILITY(getStatistic("helperBreedingProbability"));
        setMAX_LITTER_SIZE(getBaseValue("helperMaxLitterSize"));
        setNutritionalValue(getBaseValue("helperNutritionalValue"));

    }

    /**
     * This dictates what each helper fish does in a time unit during the day
     * It allows it to mate, find food, and move
     *
     * @param newHelperFish A list to return newly born helper fish
     */
    @Override
    public void act(List<Animal> newHelperFish) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            findPlant();
            findMate(newHelperFish); // can mate and move to new location in same move
            Location newLocation = getField().freeAdjacentLocation(getLocation());

            if(newLocation != null) { // See if it was possible to move.
                setLocation(newLocation);
            } else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * What helper fish do at times designated nighttime
     * @param newHelperFish A list to return newly born helper fish
     */
    public void nightAct(List<Animal> newHelperFish) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            findPlant();
            findMate(newHelperFish);
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                setDead();
            }
        }
    }

    /**
     * How helper fish behave in cases of cold water
     * This is similar to its default act method, but its hunger levers stay the same and the fish does not breed
     * @param newPrey A list to return newly born helper fish
     */
    public void coldWatersAct(List<Animal> newPrey) {
        incrementAge();
        if (isAlive()) {
            findPlant();
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                setDead();
            }
        }
    }

    /**
     * Determines if there is a viable mating partner in an adjacent location
     * If so, a randomised number of new helper fish are born
     *
     * @param newHelperFish A list to return newly born helper fish
     */
    protected void findMate(List<Animal> newHelperFish) {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();

        while (it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof HelperFish) {
                HelperFish mate = (HelperFish) animal;
                if (mate.isAlive() && mate.getGender() != this.getGender()) {
                    int births = breed();
                    for (int b = 0; b < births && free.size() > 0; b++) {
                        Location loc = free.remove(0);
                        HelperFish young = new HelperFish(false, field, loc, rand.nextBoolean(), plantField);
                        newHelperFish.add(young);
                    }
                }
            }
            return; // can only breed once per time unit
        }
    }


}
