package tconsole;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;


/**
 * The class Scene contains the fader positions for one scene
 * together with methods to manage fade in/out and scene editing
 * 
 * There are TheApp.CHANNELS Scene objects in the software one for 
 * each scene button below the fader in the main screen
 */
public class Scene {

    /**
     * Constructs a Scene object. The scene has all levels set to 0,
     * ist not showing an the fade time is set to 0.
     *
     * @param n   The scene number (1..16)
     */
    public Scene(int n) {
        values = new float[TheApp.CHANNELS];
        edit = false; 
        showing = false;
        started = 0;
        fadeTime = 0;
        
        if (n < 1) n = 1;
        if (n > TheApp.SCENES) n = TheApp.SCENES;
        
        sceneNumber = n;
        name = "";
        scenes[n-1] = this;
    }

    /**
     * Sets the edit mode for the scene. In edit mode the scene always
     * shows the programmed values as actual values in getValues()
     * 
     * @param     flag   True enables edit mode
     */
    public void setEdit(boolean flag) {
        synchronized(LOCK) {
            edit = flag;
        }
    }

    /**
     * Sets one of the programmed values in the scene. Calls with
     * out-of-range 'channel'values are ignored, 'value' gets limited
     * to 0..1 inside the method
     * 
     * @param     channel   The channel number (1..16) to set.
     * @param     value     The value to set for this channel (0..1)
     */
    public void setValue(int channel, float value) {
        synchronized(LOCK) {
            if (channel < 1) return;
            if (channel > TheApp.CHANNELS) return;
            if (value < 0.0f) value = 0.0f;
            if (value > 1.0f) value = 1.0f;
            values[channel-1] = value;
        }
    }
    
    /**
     * Gets the programmed value of a particular channel in the scene.
     * 
     * @param     channel   The channel (1..16)
     * @return              The programmed channel value
     */
    public float getValue(int channel) {
        synchronized(LOCK) {
            if (channel < 1) channel = 1;
            if (channel > TheApp.CHANNELS) channel = TheApp.CHANNELS;
            return values[channel-1];
        }
    }

    /**
     * Gets the actual channel values for this scene, This respects the
     * actual fade-on/out and edit settings for the scene.
     * 
     * @return  A float array with the actual channel levels of this scene
     */
    public float[] getActualValues() {
        synchronized(LOCK) {
            if (edit) return values;
            float retval[] = new float[TheApp.CHANNELS];
            if (showing) {
                if (fadeTime == 0) return values;
                long dt = System.currentTimeMillis() - started;
                if (dt > fadeTime) dt = fadeTime;
                float a = ((float)dt) / ((float)fadeTime);
                for (int i = 0; i < values.length; i++) retval[i] = values[i] * a;
                return retval;
            } else {
                if (fadeTime == 0) return retval;
                long dt = System.currentTimeMillis() - started;
                if (dt > fadeTime) dt = fadeTime;
                float a = ((float)(fadeTime-dt)) / ((float)fadeTime);
                for (int i = 0; i < values.length; i++) retval[i] = values[i] * a;
                return retval;
            }
        }
    }

    /**
     * Tells the scene to fade in with the given fade in time 
     * 
     * @param     t   The fade in time (msecs)
     */
    public void fadeIn(int t) {
        synchronized(LOCK) {
            fadeTime = t;
            started = System.currentTimeMillis();
            showing = true;
        }
    }

    /**
     * Tells the scene to fade out with the given fade out time 
     * 
     * @param     t   The fade out time (msecs)
     */
    public void fadeOut(int t) {
        synchronized(LOCK) {
            fadeTime = t;
            started = System.currentTimeMillis();
            showing = false;
        }
    }

    /**
     * Checks if this scene must be accumulated for the final 
     * channel output. This is if the scene is showing or if the
     * fade out i still running.
     * 
     * @return    true if the scene must be accumulated.
     */
    public boolean mustAccumulate() {
        // showing scenes must be accuulated
        if (showing) return true;
        // scenes in edit mode must be accumulated
        if (edit) return true;
        // scene is not showing, check if the fade out ist still running
        return System.currentTimeMillis() < (started + fadeTime);
    }

    /**
     * Returns a string with the channel levels, separated by
     * comma characters, Used to write the scene settings in a file
     * 
     * @return    The channel levels as a string
     */
    public String toString() {
        synchronized (LOCK) {
            StringBuffer b = new StringBuffer(name);
            for (int i = 0; i < values.length; i++) {
                b.append(";");
                int v = (int) (values[i] * 1000.0f);
                b.append(v);
            }
            return b.toString();
        }
    }

    /**
     * Parses a string created with 'toString()', restores the
     * channel levels from this. Used when reading the stored 
     * scene from disk
     * 
     * @param     s    The string to parse.
     */
    public void fromString(String s) {
        try {
            String words[] = s.split("\\;");
            name = words[0];
            for (int i = 0; i < values.length; i++) {
                int v = Integer.parseInt(words[i+1]);
                values[i] = v / 1000.0f;
            }
        } catch (Exception e) {
        }
    }

    /**
     * Sets the name of the scene
     * 
     * @param     n   The new scene name
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * Delivers the scene's name
     * 
     * @return     The name
     */
    public String getName() {
        return name;
    }

    /**
     * Delivers a reference to a scene given by its number.
     *
     * @param   n The scene number (0..16)
     * @return    A reference to the requested scene object
     */
    public static Scene getScene(int n) {
        if (n < 1) n = 1;
        if (n > TheApp.SCENES) n = TheApp.SCENES;
        return scenes[n-1];
    }

    /**
     * Loads all scenes from the scenes.txt file
     * 
     */
    public static void load() {
        try {
            int sn = 0;
            BufferedReader in = new BufferedReader(new FileReader("scenes.txt"));
            for (;;) {
                String line = in.readLine();
                if (line == null) break;
                if (sn >= TheApp.SCENES) break;
                scenes[sn++].fromString(line);
            }
            in.close();
        } catch (Exception e) {
            // ignore that
        }
    }

    /**
     * Saves all scenes to the scenes.txt file 
     * 
     */
    public static void save() {
        try {
            PrintWriter out = new PrintWriter("scenes.txt");
            for (int sn = 0; sn < TheApp.SCENES; sn++) {
                out.println(scenes[sn].toString());
            }
            out.close();
        } catch (Exception e) {
            // ignore that
        }
    }

    /** the scene number (1..16) */
    private int sceneNumber;

    /** the scene name, displayed on the scene button */
    private String name;

    /** true = the sceen is showing */
    private boolean showing;

    /** true = the scene is in edit mode */
    private boolean edit;

    /** the time when a fade-in/out has been initiated */
    private long started;

    /** the actual fade time to be used (msecs) */
    private long fadeTime;

    /** the programmed fader values for the scene (0..1) */
    private float values[];

    /**
     * Operations on scenes may happen in different task contexts
     * (UI, DMX transmitter etc.) Therefore all operations on scenes
     * are synchronized / MT-protected by this static object.
     */
    private static final Object LOCK = new Object();

    /**
     * A list of all scenes which exist in the program
     */
    private static Scene scenes[] = new Scene[TheApp.CHANNELS];

}
