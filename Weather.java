import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *Sets the possible weather conditions and retrieves a random weather condition
 * @author Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */
public class Weather {

    Map<Integer, String> weatherDict = new HashMap<>();
    String weather;


    /**
     * Sets the weather condition as the value, alongside a key.
     * @param num The key for the weather dictionary.
     * @param weatherCondition The available types of weather conditions
     */
    public void setWeather(int num, String weatherCondition) {
        weatherDict.put(num, weatherCondition);
    }

    /**
     * Generates a random number and retrieves the weather condition value it is associated with.
     * @return current weather
     */
    public String getWeather() {
        Random rand = new Random();
        int randNum = rand.nextInt(3)+1;
        weather = weatherDict.get(randNum);
        return weather;
    }


}
