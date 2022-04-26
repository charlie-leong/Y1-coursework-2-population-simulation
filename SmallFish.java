import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A class containing the shared characteristics and methods of small fish
 * They are capable of breeding, finding food, and moving
 *
 * @author Alenxandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */

public class SmallFish extends Prey{
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a small fish as a newborn (age zero and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the fish will have a randomised age and hunger level.
     * @param field     The field currently occupied.
     * @param location  Its location within the field.
     * @param gender    Its gender, with true representing female and false representing male
     */
    public SmallFish(boolean randomAge, Field field, Location location, boolean gender, Field plantField) {
        super(randomAge, field, location, gender, plantField);
        setBREEDING_AGE(getBaseValue("smallBreedingAge"));
        setMAX_AGE(getBaseValue("smallMaxAge"));
        setBREEDING_PROBABILITY(getStatistic("smallBreedingProbability"));
        setMAX_LITTER_SIZE(getBaseValue("smallMaxLitterSize"));
        setNutritionalValue(getBaseValue("smallNutritionalValue"));
    }

    /**
     * This is the default actions that a small fish takes during the day, bar other weather conditions.
     * @param newSmallFish A list to return newly born fish.
     */
    @Override
    public void act(List<Animal> newSmallFish) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            findPlant();
            giveBirth(newSmallFish);
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) { // See if it was possible to move.
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * What the small fish do at times designated nighttime
     * @param newSmallFish a list of newly born small fish
     */
    public void nightAct(List<Animal> newSmallFish) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            findPlant();
            giveBirth(newSmallFish);
            //findMate(newSmallFish);
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if (newLocation != null) { // See if it was possible to move.
                setLocation(newLocation);
            } else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * How small fish act in cases of cold water
     * The fish's age is incremented, and they find food
     *
     * @param newPrey a list to return newly born fish
     */
    public void coldWatersAct(List<Animal> newPrey) {
        incrementAge();
        if (isAlive()) {
            findPlant();
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) { // See if it was possible to move.
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Checks adjacent cells for viable breeding partners
     * If so, a randomised number of offspring will be produced
     *
     * @param newPrey A list to return newly born small fish.
     */
    protected void giveBirth(List<Animal> newPrey) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            SmallFish young = new SmallFish(false, field, loc, rand.nextBoolean(), plantField);
            newPrey.add(young);
        }
    }


}
