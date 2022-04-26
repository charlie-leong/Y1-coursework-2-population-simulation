import java.util.Iterator;
import java.util.List;

/**
 * This class provides a representation of a shark as a predator species
 * It contains the characteristics and methods of a shark, allowing it to move, breed, hunt, and contract disease
 *
 * @author Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */

public class Shark extends Predator{

    /**
     * Create a shark as a newborn (age zero and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the shark will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param gender    the shark's gender, with true representing female and false representing male.
     */
    public Shark(boolean randomAge, Field field, Location location, boolean gender) {
        super(randomAge, field, location, gender);
        setBREEDING_AGE(getBaseValue("sharkBreedingAge"));
        setMAX_AGE(getBaseValue("sharkMaxAge"));
        setBREEDING_PROBABILITY(getStatistic("sharkBreedingProbability"));
        setMAX_LITTER_SIZE(getBaseValue("sharkMaxLitterSize"));
    }

    /**
     * This dictates what each Shark does in every time unit, allowing it to mate, find food, and move
     * This includes the incrementation of its age and level of hunger
     * @param newShark List to return newly born sharks
     */
    @Override
    public void act(List<Animal> newShark) {
        incrementAge();
        incrementHunger();
        checkDisease();
        if(isAlive()) {
            findMate(newShark);
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
     * How sharks behave at night, where their age and hunger levels are incremented, and they can move
     * @param newShark List to return newly born sharks
     */
    public void nightAct(List<Animal> newShark) {
         incrementAge();
        incrementHunger();
        if (isAlive()) {
            Location location = this.getLocation();
            setLocation(location);
        }
    }

    /**
     * How sharks act in cases of cold water
     * @param newShark List to return newly born sharks
     */
    public void coldWatersAct(List<Animal> newShark){
        incrementAge();
        if(isAlive()) {
            Location newLocation = findFood();
            if(newLocation == null) { // No food found
                newLocation = getField().freeAdjacentLocation(getLocation());
            }if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                setDead();
            }
        }
    }

    /**
     * How sharks act in cases of strong currents
     * @param newShark List to return newly born sharks
     */
    public void strongCurrentsAct(List<Animal> newShark){
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            findMate(newShark);
            Location newLocation = findFood();
            if(newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                setDead();
            }
        }
    }
    /**
     * Determines whether a shark is positioned next to a viable mate and produces a random number of offspring
     * @param newShark List to return newly born sharks in the case of successful breeding
     */
    protected void findMate(List<Animal> newShark) {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();

        while (it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Shark) {
                Shark mate = (Shark) animal;
                if (mate.isAlive() && mate.getGender() != this.getGender()) {
                   int births = breed();
                    //int births = rand.nextInt(2) + 1;
                    for (int b = 0; b < births && free.size() > 0; b++) {
                        Location loc = free.remove(0);
                        Shark young = new Shark(false, field, loc, rand.nextBoolean());
                        newShark.add(young);
                    }
                }
            }
            return; // can only breed once per time unit
        }
    }

    /**
     * Looks for prey in adjacent locations to eat the first one found
     * Overrides the typical predator findFood method to include Tuna as a type of prey.
     * @return Where food was found, or null if it wasn't.
     */
    @Override
    protected Location findFood() {
        if(getFoodLevel()<60){
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
                Location where = it.next();
                Object animal = field.getObjectAt(where);
                if(animal instanceof Prey || animal instanceof Tuna) {
                    Animal prey = (Animal) animal;
                    if(prey.isAlive()) {
                        prey.setDead();
                        setFoodLevel(getFoodLevel() + prey.getNutritionalValue());
                        return where;
                    }
                }
            }
        }
        return null;
    }

}
