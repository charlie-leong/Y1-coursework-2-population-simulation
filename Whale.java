import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Whales are a neutral class that are classified as neither predator nor prey
 * This class shows the shared characteristics of whales
 *
 * @author Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */
public class Whale extends Animal{

    private int diseaseCounter; // number of time units gone without contact with a helper fish
    private final int MAX_AGE = getBaseValue("whaleMaxAge");
    public final int MAX_LITTER = getBaseValue("whaleMaxLitterSize");

    protected static Random rand = Randomizer.getRandom();


    /**
     * Create an instance of Whale as a newborn (age zero and not hungry)
     *
     * @param field The field it exists on
     * @param location The current location in the field
     */
    public Whale(Field field, Location location){
        super(field, location);
        diseaseCounter = 0;
        setAge(0);
    }

    /**
     * This is the default method for what each whale does in a time unit, allowing it to mate and age
     * @param newWhale a list to return newly born whales
     */
    @Override
    public void act(List<Animal> newWhale) {
        incrementAge();
        incrementDisease();
        findMate(newWhale);
        Location newLocation = getField().freeAdjacentLocation(getLocation());
        if(newLocation != null){
            setLocation(newLocation);
        } // if there are no free locations, it will remain in the same position (whales are okay with being stagnant! )

    }

    /**
     * What whales do at night, where they are not susceptible to disease but can still mate
     * @param newWhale a list to return newly born whales
     */
    public void nightAct(List<Animal> newWhale) {
        incrementAge();
        if (isAlive()) {
            findMate(newWhale);
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * How whales behave in cases of cold water
     * Disease and age are incremented and the whale can move
     *
     * @param newWhale A list to return newly born whales
     */
    public void coldWatersAct(List<Animal> newWhale) {
        incrementDisease();
        incrementAge();
        if (isAlive()) {
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null){
                setLocation(newLocation);
            } // if there are no free locations, it will remain in the same position (whales are okay with being stagnant! )
        }
    }

    /**
     * How whales behave in cases of strong currents
     * @param newWhale a list to return newly born whales
     */
    public void strongCurrentsAct(List<Animal> newWhale) {
        incrementDisease();
        if (isAlive()) {
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null){
                setLocation(newLocation);
            } // if there are no free locations, it will remain in the same position (whales are okay with being stagnant! )

        }
    }

    /**
     * Increases the whale's age by one
     * If the whale meets the maximum age limit, it will be marked as dead
     */
    public void incrementAge(){
        if(getAge() <= MAX_AGE){
            setAge(getAge()+1);
        }
        else{
            setDead();
        }
    }

    /**
     * If a helper fish is not in an adjacent cell to the whale, the disease counter will increase by one
     * If a helper fish is present, the counter will reset
     * If the disease counter surpasses the limit, the whale will be marked as diseased
     */
    public void incrementDisease(){
        Field field = getField();
        Iterator<Location> it = field.adjacentLocations(getLocation()).iterator();

        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof HelperFish && ((HelperFish) animal).isAlive()) {
                diseaseCounter = 0; //resets the disease counter
                return;
            }
        }
        diseaseCounter++;
        if(diseaseCounter >= getBaseValue("whaleDiseaseLimit")){
            setIsDiseased(true);
        }
    }

    /**
     * Checks surrounding cells for a viable breeding partner
     * If one is present, a randomised number of whale offspring are spawned
     *
     * @param newWhale A list to return newly born whales
     */
    protected void findMate(List<Animal> newWhale) {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();

        while (it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Whale) {
                Whale mate = (Whale) animal;
                if (mate.isAlive() && mate.getAge()>getBaseValue("whaleBreedingAge")) {
                    int births = rand.nextInt(MAX_LITTER) + 1;
                    for (int b = 0; b < births && free.size() > 0; b++) {
                        Location loc = free.remove(0);
                        Whale young = new Whale(field, loc);
                        newWhale.add(young);

                    }
                }
            }
            return; // can only breed once per time unit
        }
    }

}
