import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 *
 * @author David J. Barnes and Michael Kölling and Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */
public abstract class Animal {
    private boolean isAlive; // Whether the animal is alive or not.
    private Field field; // The field an animal exists on
    private Location location; // The animal's position in the field.
    private boolean gender;  // The animal's gender, true is female, false is male
    private int age; //The animal's age
    private int nutritionalValue; // The nutritional value an animal provides when eaten
    private boolean isDiseased; // Whether an animal is diseased

    private final Random rand = new Random();
    protected PropertiesFile file = new PropertiesFile();

    /**
     * Create a new animal at location in field.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location) {
        isAlive = true;
        this.field = field;
        setLocation(location);
        setIsDiseased(false);
    }

    /**
     * Make this animal act
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Make this animal act during the night, in warm waters.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void nightAct(List<Animal> newAnimals);

    /**
     * Make this animal act during the night in cold waters.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void coldWatersAct(List<Animal> newAnimals);

    /**
     * Make this animal act during the night in strong currents.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void strongCurrentsAct(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return isAlive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead() {
        isAlive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField() {
        return field;
    }

    /**
     * Return the animal's gender
     * @return The animal's gender
     */
    protected boolean getGender(){
        return gender;
    }

    /**
     * Set the animal's gender
     * @param gender The animal's assigned gender
     */
    protected void setGender(boolean gender){
        this.gender = gender;
    }

    /**
     * Set the animal's age
     * @param age The value the animal's age will be set as
     */
    protected void setAge(int age){
        this.age = age;
    }

    /**
     * Return the animal's age
     * @return the animal's age
     */
    protected int getAge(){
        return age;
    }

    /**
     * Return the animal's nutritional value
     * @return the animal's nutritional value
     */
    protected int getNutritionalValue(){
        return nutritionalValue;
    }

    /**
     * Set the animal's nutritional value
     * @param value the animal's nutritional value
     */
    protected void setNutritionalValue(int value){
        nutritionalValue = value;
    }

    /**
     * Set whether the animal is diseased
     * @param disease whether the animal is diseased
     */
    protected void setIsDiseased(boolean disease){
        isDiseased = disease;
    }

    /**
     * return whether the animal is diseased
     * @return whether the animal is diseased
     */
    protected boolean getIsDiseased(){
        return this.isDiseased;
    }

    /**
     * determines whether an animal dies if it is diseased based on randomised proobability
     */
    protected void checkDiseaseDeath(){
        if(isDiseased && rand.nextDouble() < getStatistic("diseaseProbability")){
            this.setDead();
        }
    }

    /**
     * Retrieves probability values from the property file based on the key
     * @param key Key of the variable to be found
     * @return The key's corresponding probability value
     */
    protected double getStatistic(String key){
        return Double.parseDouble(file.accessProperty(key));
    }

    /**
     * Retrieves the integer value from the property file based on the key
     * @param key key of the variable to be found
     * @return The integer value corresponding to the key
     */
    protected int getBaseValue(String key){
        return Integer.parseInt(file.accessProperty(key));
    }

}
