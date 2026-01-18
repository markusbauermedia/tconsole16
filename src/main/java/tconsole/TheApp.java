package tconsole;

import java.util.*;
import java.io.*;
import java.awt.*;

import java.awt.event.*;
import javax.swing.*;
import org.json.*;


/**
 * TheApp defines the application main window and a static main()
 * method to start the application.
 *
 */
public class TheApp extends JFrame {

    /**
     * Constructs and shows a TConsole window.
     *
     */
    public TheApp() {
        Config.initialize();
        fadeIn = Config.fadeIn;
        fadeOut = Config.fadeOut;
        String wd = new File(".").getAbsolutePath();
        setTitle("TConsole16 "+Version.version+"- "+wd);
        mainPanel = new MainPanel();
        add(mainPanel);
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        DMXTransmit.initialize();
        Scene.load();
        if (Config.audioMode == Config.MPLAYER) MPlayer.initialize();
    }

    /**
     * The application main method. Starts the application, creates a
     * TConsole main window and populates this with the components / panels
     * building the application UI
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
        new TheApp();
    }

    /**
     * The number of channels / faders provided by the
     * application. Affects the faders shown in the upper
     * application main window.
     */
    public static final int CHANNELS = 16;
    
    /**
     * The number of scenes provided by the
     * application. This equals to the number of channels.
     * Future versions of the software may support different
     * numbers for CHANNELS and SCENES
     */
    public static final int SCENES = CHANNELS;

    /**
     * A Properties object containing the fader definitions
     * and some global settings of the application. Defined
     * as public static to be easily accessed from other
     * classes.
     */
    public static Properties properties = new Properties();

    /** The fade in time (msecs) actually used. */
    public static int fadeIn = 1000;

    /** The fade out time actually used. */
    public static int fadeOut = 3000;

    /** true = the application is in live edit mode */
    public static boolean live = false;

    /** true = one scene is in edit mode */
    public static boolean edit = false;

    /** a global reference to the main panel */
    public static MainPanel mainPanel;
}

