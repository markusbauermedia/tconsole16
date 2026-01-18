package tconsole;

/**
 * The class Config defines all configuration parameters of the program
 * as static variables.
 * 
 */
public class Config {

    /**
     * Initializes the confog parameters from 'tconsole.properties'
     * 
     */
    public static void initialize() {
        ExtProperties p = new ExtProperties("tconsole.properties");
        fadeIn = p.getInteger("scene.default.fade.in", 1200);
        fadeOut = p.getInteger("scene.default.fade.out", 3000);
        String am = p.getString("audio.mode", "MPLAYER");
        audioMode = "RADIG".equals(am) ? RADIG : MPLAYER;
        mplayerCLI = p.getString("audio.mplayer.cli", "mplayer -quiet -idle -slave");
        audioSongSelect = p.getInteger("audio.radig.select", 50);
        audioPlayStop = p.getInteger("audio.radig.play", 51);
        audioVolume = p.getInteger("audio.radig.volume", 52);
        for (int i=1; i<=TheApp.CHANNELS; i++) {
            String key = "fader."+String.format("%02d", i)+".label";
            dmxName[i-1] = p.getString(key, "CH-"+String.format("%02d", i));
        }
        for (int i=1; i<=TheApp.CHANNELS; i++) {
            String key = "fader."+String.format("%02d", i)+".patch";
            dmxPatch[i-1] = p.getInteger(key, i);
        }
    }

    /** Constant for audioMode */
    public static final int MPLAYER = 0;
    
    /** Constant for audioMode */
    public static final int RADIG = 1;

    /** The default fade in time (msecs) */
    public static int fadeIn = 1200;

    /** The default fade out time (msecs) */
    public static int fadeOut = 3000;

    /** The audio player to be used. one of MPLAYER, RADIG */
    public static int audioMode = MPLAYER;

    /** The DMX channel for song select in RADIG audioMode */
    public static int audioSongSelect = 50;
    
    /** The DMX channel for play / stop in RADIG audioMode */
    public static int audioPlayStop = 51;

    /** The DMX channel for audio volume in RADIG audioMode */
    public static int audioVolume = 52;

    /** The command line to start mplayer */
    public static String mplayerCLI = "mplayer -idle -quiet -slave";

    /** DMX channel patiching */
    public static int dmxPatch[] = {
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        16
    };

    /** DMX channel / fader names */
    public static String dmxName[] = {
        "CH-01",
        "CH-02",
        "CH-03",
        "CH-04",
        "CH-05",
        "CH-06",
        "CH-07",
        "CH-08",
        "CH-09",
        "CH-10",
        "CH-11",
        "CH-12",
        "CH-13",
        "CH-14",
        "CH-15",
        "CH-16"
    };

}
