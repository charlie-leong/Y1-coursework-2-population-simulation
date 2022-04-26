import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
/**
 * A small ecosystem simulator containing five species and plants displayed on a rectangular screen
 * Spawns all new animals and plants on the field of area
 *
 * @author David J. Barnes and Michael Kölling and Alenxandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */
public class Simulator {
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;

    public PropertiesFile file;

    private List<Animal> animals;   // List of animals in the field.
    private List<Plant> plants;     //list of plants in the field

    private Field field;
    private Field plantLocation;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView animalView;
    private SimulatorView plantView;

    private Weather weather = new Weather();
    private String currentWeather;

    private boolean exit;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    /**
     * Create a simulation field with the given size.
     *
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        plants = new ArrayList<>();
        field = new Field(depth, width);
        plantLocation = new Field(depth, width);

        file = new PropertiesFile();
        // Create a view of the state of each location in the field.
        plantView = new SimulatorView(depth, width, Simulator.this);
        plantView.setColor(Plant.class, Color.GREEN);

        animalView = new SimulatorView(depth, width,Simulator.this );
        animalView.setColor(HelperFish.class, Color.YELLOW);
        animalView.setColor(Shark.class, Color.BLACK);
        animalView.setColor(Tuna.class, Color.RED);
        animalView.setColor(SmallFish.class, Color.CYAN);
        animalView.setColor(Whale.class, Color.BLUE);

        // Setup a valid starting point.
        reset();

    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (2000 steps).
     */
    public void runLongSimulation() {
        simulate(2000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     *
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && animalView.isViable(field); step++) {
            simulateOneStep();
            delay(40);   // uncomment this to run more slowly
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each animal
     */
    public void simulateOneStep() {
        if (!exit) {
            step++;

            // Provide space for newborn animals.
            List<Animal> newAnimals = new ArrayList<>();
            List<Plant> newPlants = new ArrayList<>();
            boolean day = checkTimeOfDay();
            String currentWeather = getCurrentWeather();

            for (Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
                Animal animal = it.next();

                if (day) {
                    animal.act(newAnimals);
                }
                else if(currentWeather.equals("Warm waters")) { //act normal
                        animal.nightAct(newAnimals);
                } else if (currentWeather.equals("Cold waters")) { //hunger or health decreases
                        animal.coldWatersAct(newAnimals);
                } else if (currentWeather.equals("Strong currents")) { //all animals stay in the same location
                        animal.strongCurrentsAct(newAnimals);
                }

                if (!animal.isAlive()) {
                    it.remove();
                }
            }
            for (Iterator<Plant> itPlant = plants.iterator(); itPlant.hasNext(); ) {
                Plant plant = itPlant.next();
                // plants only grow during the day
                if (day) {
                    plant.regrow(newPlants);
                }

                if (!plant.isAlive()) {
                    itPlant.remove();
                }
            }

            // Add the newly born animals and plants
            animals.addAll(newAnimals);
            plants.addAll(newPlants);

            animalView.showStatus(step, field, currentWeather, day);
            plantView.showStatus(step, plantLocation, currentWeather, day);
        }
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        exit = false;
        animals.clear();
        plants.clear();
        populate();
        setWeatherDict();
        String currentWeather = getCurrentWeather();
        // Show the starting state in the view.
        boolean day = checkTimeOfDay();
        animalView.showStatus(step, field, currentWeather, day);
        plantView.showStatus(step, plantLocation, currentWeather, day);
    }


    /**
     * stops the simulator
     */
    public void stop() {
        exit = true;
    }


    /**
     * Randomly populate the field with different species.
     */
    private void populate() {
        Random rand = Randomizer.getRandom();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= getStatistic("SHARK_CREATION_PROBABILITY")) {
                    Location location = new Location(row, col);
                    Predator shark = new Shark(true, field, location, rand.nextBoolean()); // add gender randomizer
                    animals.add(shark);
                }
                else if(rand.nextDouble() <= getStatistic("TUNA_CREATION_PROBABILITY")) {
                    Location location = new Location(row, col);
                    Predator tuna = new Tuna(true, field, location, rand.nextBoolean());
                    animals.add(tuna);
                }
                else if(rand.nextDouble() <= getStatistic("HELPER_FISH_CREATION_PROBABILITY")) {
                    Location location = new Location(row, col);
                    Prey helperFish = new HelperFish(true, field, location, rand.nextBoolean(), plantLocation);
                    animals.add(helperFish);
                }
                else if(rand.nextDouble() <= getStatistic("SMALL_FISH_CREATION_PROBABILITY")){
                    Location location = new Location(row,col);
                    Prey smallFish = new SmallFish(true, field, location, rand.nextBoolean(), plantLocation);
                    animals.add(smallFish);
                }
                else if(rand.nextDouble() <= getStatistic("WHALE_CREATION_PROBABILITY")){
                    Location location = new Location(row, col);
                    Animal whale = new Whale(field, location);
                    animals.add(whale);
                }
                if (rand.nextDouble() <= getStatistic("PLANT_CREATION_PROBABILITY")) {
                    Location location = new Location(row, col);
                    Plant plant = new Plant(plantLocation, location);
                    plants.add(plant);
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Pause for a given time.
     *
     * @param millisec The time to pause for, in milliseconds
     */
    private void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException ie) {
            // wake up
        }
    }

    /**
     * Sets the possible weather conditions
     */
    private void setWeatherDict() {
        weather.setWeather(1, "Cold waters");
        weather.setWeather(2, "Warm waters");
        weather.setWeather(3, "Strong currents");
    }

    /**
     * For every 8 steps, the simulation changes the current weather condition
     * @return currentWeather.
     */
    private String getCurrentWeather() {
        if (step % 8 == 0) {
            currentWeather = weather.getWeather();
            return currentWeather;
        }
        return currentWeather;
    }

    /**
     * Checks the current time of day
     * Animal will act differently depending on the time of day
     *
     * @return true if it is day time, false if it is night
     */
    private boolean checkTimeOfDay() {
        return (step % 4 <= 1);
    }

    /**
     * retrieves the probability value corresponding to the key from the population base statistic property file
     * @param key name of the probability being searched for
     * @return corresponding double probability value
     */
    public double getStatistic(String key){
        return Double.parseDouble(file.accessProperty(key));
    }


}





