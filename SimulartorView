import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 *
 * @author David J. Barnes and Michael Kölling and Alexandra Encarnacion k21016552 and Charlotte Hoi Yi Leong k21062990
 * @version 2022.03.01
 */
public class SimulatorView extends JFrame {
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private final String WEATHER_PREFIX = "Weather: ";
    private final String TIMEofDAY_PREFIX = "Time of Day: ";
    private JLabel stepLabel, population, infoLabel, weatherLabel, timeOfDayLabel;
    private FieldView fieldView;
    private JButton longSimulation_btn, stepByStep_btn, stop_btn, reset_btn;

    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;

    private boolean running;
    private boolean stop;

    /**
     * Create a view of the given width and height.
     *
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width, Simulator simulator) {
        stats = new FieldStats();
        colors = new LinkedHashMap<>();

        setTitle("PPA assignment 3 - alex and charlie's underwater sensation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        infoLabel = new JLabel("  ", JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        weatherLabel = new JLabel(WEATHER_PREFIX, JLabel.CENTER);
        timeOfDayLabel = new JLabel(TIMEofDAY_PREFIX, JLabel.CENTER);

        longSimulation_btn = new JButton("Long Simulation");
        stepByStep_btn = new JButton("Next Step");
        //customSimulation_btn = new JButton("Customised Amount For simulation");
        stop_btn = new JButton("Stop");
        reset_btn = new JButton("Reset");


        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();

        JPanel infoPane = new JPanel(new BorderLayout());
        infoPane.add(stepLabel, BorderLayout.WEST);
        infoPane.add(infoLabel, BorderLayout.CENTER);
        infoPane.add(weatherLabel, BorderLayout.EAST);
        infoPane.add(timeOfDayLabel, BorderLayout.CENTER);

        JPanel bPane = new JPanel(new FlowLayout());
        JPanel buttonsPane = new JPanel(new GridLayout(4, 1));
        buttonsPane.add(longSimulation_btn);
        buttonsPane.add(stop_btn);
        buttonsPane.add(stepByStep_btn);
        buttonsPane.add(reset_btn);
        bPane.add(buttonsPane);

        contents.add(infoPane, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        contents.add(bPane, BorderLayout.EAST);
        pack();
        setVisible(true);


        // Runs the runLongSimulation when the button is clicked

        longSimulation_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    running = true;
                    new Thread(simulator::runLongSimulation).start();
                    stop = false;
                } else {
                    System.out.println("Wait for current simulation to finish...");
                    stop = true;
                }
            }
        });


        //When the stop button is pressed, it stops the current simulation
        stop_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    new Thread(simulator::stop).start();
                    System.out.println("Stop");
                    stop = true;
                    running = false;
                } else {
                    System.out.println("No simulation to stop");
                }
            }
        });


        //When the next step button is clicked, it calls the simulateOneStep method to go to the next step in the simulation.
        stepByStep_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    new Thread(simulator::simulateOneStep).start();
                    stop = true;
                } else {
                    System.out.println("Cant do next step");
                }
            }
        });


        //When the reset button is clicked, it resets the whole simulation.
        reset_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running && stop) {
                    new Thread(simulator::reset).start();
                    System.out.println("RESET");
                } else {
                    System.out.println("Stop simulation first");
                }
            }
        });
    }

    /**
     * Define a color to be used for a given class of animal.
     *
     * @param animalClass The animal's Class object.
     * @param color       The color to be used for the given class.
     */
    public void setColor(Class animalClass, Color color) {
        colors.put(animalClass, color);
    }


    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass) {
        Color col = colors.get(animalClass);
        if (col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     *
     * @param step  Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(int step, Field field, String weather, boolean day) {
        if (!isVisible()) {
            setVisible(true);
        }

        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();

        weatherLabel.setText(WEATHER_PREFIX + weather);

        if (day) {
            timeOfDayLabel.setText((TIMEofDAY_PREFIX + "AM"));
        } else {
            timeOfDayLabel.setText((TIMEofDAY_PREFIX + "PM"));
        }
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                Object plant = field.getObjectAt(row, col);
                if (animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));

                } else if (plant != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     *
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Provide a graphical view of a rectangular field.
     * This component displays the field.
     */
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint() {
            if (!size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if (xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if (yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                Dimension currentSize = getSize();
                if (size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                } else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
