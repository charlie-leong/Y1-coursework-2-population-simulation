/**
 * Plants are spawned randomly at first but won't move throughout the whole simulation
 * plants can be eaten then grow back
 */

import java.util.List;
import java.util.Random;

/**
 * The base model for all plants in the simulation
 * Allows them to spread and get eaten by animal species.
 *
 * @author Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */
public class Plant {
    private static final Random rand = Randomizer.getRandom();
    private boolean alive;      //true if plant has NOT been eaten, false if it has
    private Field field;        //the plant's field
    private Location location;  // The plant's position in the field.
    private static  double REGROWTH_PROBABILITY;
    private static  int MAX_PLANT_SEEDS;
    private int eatenCounter;

    private PropertiesFile file = new PropertiesFile();

    /**
     * a new plant is created
     *
     * @param field The field it currently occupies
     * @param location The location within the field
     */
    public Plant(Field field, Location location) {
        this.field = field;
        setLocation(location);
        alive = true;
        REGROWTH_PROBABILITY = getStatistic("plantRegrowthProbability");
        MAX_PLANT_SEEDS = (int) getStatistic("plantMaxSeed");
        eatenCounter = 4;
    }

    /**
     * Return the plant's location.
     *
     * @return The plant's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Place the plant at the new location in the given field.
     *
     * @param newLocation The plant's new location.
     */
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Accesses the properties file and returns the probability corresponding to the inputted key
     * @param key the name of the variable corresponding to the value being searched for
     * @return double probability value
     */
    protected double getStatistic(String key){
        return Double.parseDouble(file.accessProperty(key));
    }


    /**
     * Check whether the plant is alive or not.
     * @return true if the plant is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the plant is no longer alive.
     * It is removed from the field.
     */
    public void setDead() {
        alive = false;
        if (location != null){
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the plant's field.
     * @return The plant's field.
     */
    public Field getField() {
        return field;
    }


    /**
     * the plants' main acting method that allows them to redistribute seeds and therefore create new plants
     * @param newPlants list of existing plants for new plants to be added to
     */
    public void regrow(List<Plant> newPlants) {
        if(isAlive()) {
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int seedlings = grow();
            for (int b = 0; b < seedlings && free.size() > 0; b++) {
                // for (int b = 0; b < 4 && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Plant seedling = new Plant(field, loc);
                newPlants.add(seedling);
            }
        }
    }

    /**
     * produces a randomised number under the maximum number of seeds
     * @return number of seeds a plant produces
     */
    private int grow() {
        int seeds = 0;
        if ( rand.nextDouble() <= REGROWTH_PROBABILITY) {
            seeds = rand.nextInt(MAX_PLANT_SEEDS) + 1;
        }
        return seeds;
    }


    /**
     * increments the counter tracking the number of times a plant has been eaten
     * if a plant has been eaten over four times, it will be marked as dead
     */
    public void incrementEatenCounter(){
        eatenCounter++;
        if(eatenCounter > (int)getStatistic("maxTimesEaten")){
            this.setDead();
        }
    }
}
