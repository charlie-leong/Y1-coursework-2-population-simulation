import java.util.Iterator;
import java.util.List;
/**
 * A representation of tuna as a predator that can eat prey species, move independently, and breed
 * This class shows the characteristics of tuna
 *
 * @author Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 *
 */

public class Tuna extends Predator{

    /**
     * Create an instance of Tuna as a newborn (age zero and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the tuna's age and hunger level will be randomised.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param gender    The gender of the tuna
     */
    public Tuna(boolean randomAge, Field field, Location location, boolean gender) {
        super(randomAge, field, location, gender);
        setBREEDING_AGE(getBaseValue("tunaBreedingAge"));
        setMAX_AGE(getBaseValue("tunaMaxAge"));
        setBREEDING_PROBABILITY(getStatistic("tunaBreedingProbability"));
        setMAX_LITTER_SIZE(getBaseValue("tunaMaxLitterSize"));
        setNutritionalValue(getBaseValue("tunaNutritionalValue"));
    }

    /**
     * This dictates what each Tuna does in a time unit, allowing it to mate, find food, and move
     *  This includes the incrementation of its age and level of hunger
     * @param newTuna A list to return newly born tuna
     */
    @Override
    public void act(List<Animal> newTuna) {
        incrementAge();
        incrementHunger();
        checkDisease();
        if(isAlive()) {
            findMate(newTuna);
            Location newLocation = findFood();
            if(newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else { // Overcrowding
                setDead();
            }
        }
    }

    /**
     * What tuna do at night, where their age and hunger levels are not incremented, but they can continue to search for food and mates
     *
     * @param newTuna List to return newly born tuna
     */
    public void nightAct(List<Animal> newTuna) {
        if (isAlive()) {
            findMate(newTuna);
            Location newLocation = findFood();
            if(newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * How the tuna behaves in cases of cold water
     * This is similar to the default act method but the tuna's hunger is not incremented
     *
     * @param newTuna a list to return newly born tuna
     */
    public void coldWatersAct(List<Animal> newTuna) {
        incrementAge();
        checkDisease();
        if(isAlive()) {
            findMate(newTuna);
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * How tuna behaves in cases of strong currents, where it is not affected by disease
     * @param newTuna  A list to return newly born tuna
     */
    public void strongCurrentsAct(List<Animal> newTuna) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            findMate(newTuna);
            Location newLocation = findFood();
            if (newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            if (newLocation != null) {
                setLocation(newLocation);
            } else { // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Determines whether a Tuna viable for mating is in an adjacent location and therefore produces offspring
     * @param newTuna A list to return newly born tuna
     */
    protected void findMate(List<Animal> newTuna) {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();

        while (it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Tuna) {
                Tuna mate = (Tuna) animal;
                if (mate.isAlive() && mate.getGender() != this.getGender()) {
                    int births = breed();
                    for (int b = 0; b < births && free.size() > 0; b++) {
                        Location loc = free.remove(0);
                        Tuna young = new Tuna(false, field, loc, rand.nextBoolean());
                        newTuna.add(young);

                    }
                }
            }
            return; // can only breed once per time unit
        }
    }

}
