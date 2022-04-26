import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class accesses the text file containing the population base statistics and allows values to be retrieved from their key.
 *
 * @author Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */

public class PropertiesFile {

    /**
     * Accesses the property file labelled "population base statistics" and returns the value matching the inputted key
     *
     * @param name name of variable being searched for
     * @return property corresponding to the name
     */
    public String accessProperty(String name){
        //create a new input stream to access the file containing the base packing list
        try (InputStream input = new FileInputStream(getDirectory("population base statistics"))) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(name);
        }catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * access the directory of a file name so the file path doesn't need to be hard coded
     * connecting the system directory to the file name to create the full file path
     * @param fileName name of the file being searched for
     * @return the searched file's path directory
     */
    public String getDirectory(String fileName){
       String directory = new File(fileName).getAbsolutePath();
        return directory;
    }


}

