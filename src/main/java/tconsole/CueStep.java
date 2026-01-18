package tconsole;

/**
 * A CueStep is one step of the sequence of operation
 * to be executed when a cue gets executed.
 * 
 */
public class CueStep {
    
    /**
     * Private default constructor. Prevents the class
     * from being instantiated elsewhere. CueStep objects
     * shall be created only by the static parseXXX() 
     * methods
     *     
     */
    private CueStep() {
    }

    /**
     * Internally used to display parse errors as an 
     * Alert box
     * 
     * @param     msg   The message to be schown in the
     *                  Alert box
     * @return          always null.
     */
    private static CueStep error(String msg) {
        Alert.msg(msg, TheApp.mainPanel);
        return null;
    }

    /**
     * Parses a delay time given as a string in seconds
     * to the delay time as an integer in milliseconds
     * 
     * @param     s    The String to parse
     * @return         The delay time in msecs or -1 in case
     *                 of an error
     */
    private static int parseDelay(String s) {
        try {
            return (int) (Float.parseFloat(s) * 1000.0f);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Checks if 's' is a valid scene number 
     * 
     * @param     s   The string to check.
     * @return        True if 's' is a valid scene number
     */
    private static boolean isValidSceneNumber(String s) {
        try {
            int i = Integer.parseInt(s.trim());
            if (i < 1) return false;
            if (i > TheApp.SCENES) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Executes this cue step
     */
    public void execute() {
        
        // what to execute
        switch(command) {
        
        // fade in a scene
        case SCENE_UP:
            // get the scene channel strip, abort if this fails
            ChannelStrip upStrip = TheApp.mainPanel.getChannelStrip(scene);
            if (upStrip == null) return;
            // fade in the scene. use the delay time stored with the
            // cue step or the global fade in time if the step does not
            // specify a delay time (-1 = unspecified)
            if (delay < 0) {
                upStrip.fadeIn(TheApp.fadeIn);
            } else {
                upStrip.fadeIn(delay);
            }
            break;
        
        // fade out a scene
        case SCENE_DN:
            // get the scene channel strip, abort if this fails
            ChannelStrip dnStrip = TheApp.mainPanel.getChannelStrip(scene);
            if (dnStrip == null) return;
            // fade out the scene. use the delay time stored with the
            // cue step or the global fade out time if the step does not
            // specify a delay time (-1 = unspecified)
            if (delay < 0) {
                dnStrip.fadeOut(TheApp.fadeOut);
            } else {
                dnStrip.fadeOut(delay);
            }
            break;

        // wait for some milliseconds
        case DELAY:
            // perform the wait
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
            }
            break;

        // command black to all scenes
        case BLACK:
            // get the channel strip containing the BLACK button
            BlackChannelStrip blackStrip = TheApp.mainPanel.getBlackChannelStrip();
            if (blackStrip == null) return;
            // execute black on this
            blackStrip.executeBlack();
            break;
        
        // plays an audio file
        case PLAY:
            // get the audio channel strip
            AudioChannelStrip playStrip = TheApp.mainPanel.getAudioChannelStrip();
            if (playStrip == null) return;
            // play the audio file
            playStrip.play(song);
            break;
        
        // stops audio playing
        case STOP:
            // get the audio channel strip
            AudioChannelStrip stopStrip = TheApp.mainPanel.getAudioChannelStrip();
            if (stopStrip == null) return;
            // play the audio file
            stopStrip.stop();
            break;
        
        // sets audio volume
        case VOLUME:
            // get the audio channel strip
            AudioChannelStrip volumeStrip = TheApp.mainPanel.getAudioChannelStrip();
            if (volumeStrip == null) return;
            // play the audio file
            volumeStrip.volume(song);
            break;
        }
    }

    /**
     * Parses a SCENE line from the show.txt file. Creates a CueStep
     * object for this SCENE 
     * 
     * @param     s        The line to parse
     * @param     lineNr   The line number, used for error messages
     * @return             The CueStep created ot null in case of a parse error
     */
    public static CueStep parseScene(String s, int lineNr) {
        // the tokens of the line
        // token[0] = SCENE
        // token[1] = scene number 1..16
        // token[2] = UP or DOWN
        // token[3] = delay time in seconds (optional)
        String tokens[] = s.split(" +");
        // tokens 0..2 are mandatory
        if (tokens.length < 3) return error("SCENE missing scene number or dicrection, show.txt:"+lineNr);
        CueStep c = new CueStep();
        // decode the SCENE_UP/SCENE_DN command
        if ("UP".equals(tokens[2])) c.command = Command.SCENE_UP;
        else if ("DOWN".equals(tokens[2])) c.command = Command.SCENE_DN;
        else return error("SCENE must specify UP or DOWN, show.txt:"+lineNr);
        // the scene number
        c.scene = tokens[1];
        if (!isValidSceneNumber(c.scene)) return error("SCENE with invalid scene number, show.txt:"+lineNr);
        // if a delay time is given, set it. without if the time is set to -1
        // which means to get the actual global fade in/out time at execution
        // time
        if (tokens.length == 4) {
            c.delay = parseDelay(tokens[3]);
            if (c.delay < 0) return error("SCENE with invalid fade time, show.txt:"+lineNr);
        } else {
            c.delay = -1;
        }
        return c;
    }

    /**
     * Parses a DELAY line from the show.txt file. Creates a CueStep
     * object for this DELAY step
     * 
     * @param     s        The line to parse
     * @param     lineNr   The line number, used for error messages
     * @return             The CueStep created ot null in case of a parse error
     */
    public static CueStep parseDelay(String s, int lineNr) {
        // the tokens of the line
        // token[0] = DELAY
        // token[1] = delay time in seconds
        String tokens[] = s.split(" +");
        if (tokens.length != 2) return error("DELAY must be followed by time ind secs, show.txt:"+lineNr);
        CueStep c = new CueStep();
        c.command = Command.DELAY;
        c.delay = parseDelay(tokens[1]);
        if (c.delay < 0) return error("DELAY with invalid time, show.txt:"+lineNr);
        c.scene = "";
        return c;
    }

    /**
     * Parses a BLACK line from the show.txt file. Creates a CueStep
     * object for this BLACK step
     * 
     * @param     s        The line to parse
     * @param     lineNr   The line number, used for error messages
     * @return             The CueStep created ot null in case of a parse error
     */
    public static CueStep parseBlack(String s, int lineNr) {
        // the tokens of the line
        // token[0] = BLACK
        CueStep c = new CueStep();
        String tokens[] = s.split(" +");
        if (tokens.length != 1) return error("BLACK must not be followed by parameters, show.txt:"+lineNr);
        c.command = Command.BLACK;
        c.scene = "";
        c.delay = 0;
        return c;
    }

    /**
     * Parses a PLAY line from the show.txt file. Creates a CueStep
     * object for this PLAY step
     * 
     * @param     s        The line to parse
     * @param     lineNr   The line number, used for error messages
     * @return             The CueStep created ot null in case of a parse error
     */
    public static CueStep parsePlay(String s, int lineNr) {
        // the tokens of the line
        // token[0] = PLAY
        // token[1] = song number
        String tokens[] = s.split(" +");
        if (tokens.length != 2) return error("PLAY must be followed by song number, show.txt:"+lineNr);
        CueStep c = new CueStep();
        c.command = Command.PLAY;
        try {
            c.song = Integer.parseInt(tokens[1]);
        } catch (Exception e) {
            return error("PLAY with invalid song number, show.txt:"+lineNr);
        }
        c.scene = "";
        c.delay = 0;
        return c;
    }

    /**
     * Parses a STOP line from the show.txt file. Creates a CueStep
     * object for this STOP step
     * 
     * @param     s        The line to parse
     * @param     lineNr   The line number, used for error messages
     * @return             The CueStep created ot null in case of a parse error
     */
    public static CueStep parseStop(String s, int lineNr) {
        // the tokens of the line
        // token[0] = STOP
        CueStep c = new CueStep();
        String tokens[] = s.split(" +");
        if (tokens.length != 1) return error("STOP must not be followed by parameters, show.txt:"+lineNr);
        c.command = Command.STOP;
        c.scene = "";
        c.delay = 0;
        return c;
    }

    /**
     * Parses a VOLUME line from the show.txt file. Creates a CueStep
     * object for this VOLUME step
     * 
     * @param     s        The line to parse
     * @param     lineNr   The line number, used for error messages
     * @return             The CueStep created ot null in case of a parse error
     */
    public static CueStep parseVolume(String s, int lineNr) {
        // the tokens of the line
        // token[0] = VOLUME
        // token[1] = song number
        String tokens[] = s.split(" +");
        if (tokens.length != 2) return error("VOLUME must be followed by value 0..100, show.txt:"+lineNr);
        CueStep c = new CueStep();
        c.command = Command.VOLUME;
        try {
            c.song = Integer.parseInt(tokens[1]);
        } catch (Exception e) {
            return error("VOLUME with invalis value, show.txt:"+lineNr);
        }
        c.scene = "";
        c.delay = 0;
        return c;
    }

    /** The type of CueStep */
    private Command command = Command.NOP;
    
    /** The scene number this step modifies */
    private String scene = "";

    /** The delay time specified for this step */
    private int delay = 0;

    /** The song number with PLAY commands */
    private int song = 0;

    /** The known CueStep types */
    private enum Command { NOP, SCENE_UP, SCENE_DN, DELAY, BLACK, PLAY, STOP, VOLUME };

}
