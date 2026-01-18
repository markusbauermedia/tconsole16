package tconsole;

import java.util.Vector;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * The CueList contains the 'show.txt' in compiled form. When constructed,
 * it reads an parses the 'show.txt' file. Any parse errors are signalled as 
 * Alerts.
 *
 * The CueList provides methods to navigate in the the cue list, but does no
 * cue execution.
 * 
 */
public class CueList {

    /**
     * Contructs a CueList, loads and parses the 'show.txt' file
     * 
     */
    public CueList() {
        clear();
        load();
    }

    /**
     * Clears the CueList. 
     */
    private void clear() {
        cueList = new Vector<Cue>();
        upcoming = -1;
        marker1 = -1;
        marker2 = -1;
        marker3 = -1;
        marker4 = -1;
    }

    /**
     * Loads the CueList from the 'show.txt' file.
     */
    public void load() {
        Vector<Cue> cl = new Vector<Cue>();
        try {
            BufferedReader in = new BufferedReader(new FileReader("show.txt"));
            int lineNo = 0;
            boolean expectDescription = false;
            for (;;) {
                String line = in.readLine();
                if (line == null) break;
                lineNo++;
                int commentStart = line.indexOf("#");
                if (commentStart == 0) {
                    line = "";
                } else if (commentStart > 0) {
                    line = line.substring(0, commentStart);
                }
                line = line.trim();
                if (expectDescription) {
                    if (line.length() == 0) {
                        expectDescription = false;;
                    } else {
                        cl.lastElement().addDescription(line);
                    }
                } else if (line.length() > 0) {
                    StringTokenizer st = new StringTokenizer(line);
                    String token = st.nextToken();
                    switch (token) {
                    case "CUE":
                        line = line.substring(3).trim();
                        cl.add(new Cue(line));
                        expectDescription = true;
                        break;
                    case "MARKER":
                        if (!st.hasMoreTokens()) {
                            Alert.msg("missing number 1..4 after MARKER in show.txt:"+lineNo, TheApp.mainPanel);
                            return;
                        }
                        switch (st.nextToken()) {
                        case "1":
                            marker1 = cl.size();
                            break;
                        case "2":
                            marker2 = cl.size();
                            break;
                        case "3":
                            marker3 = cl.size();
                            break;
                        case "4":
                            marker4 = cl.size();
                            break;
                        default:
                            Alert.msg("MARKER index must be 1..4 in show.txt:"+lineNo, TheApp.mainPanel);
                            return;
                        }
                        break;
                    case "SCENE":
                        CueStep cScene = CueStep.parseScene(line, lineNo);
                        if (cScene == null) return;
                        if (cl.size() == 0) return;
                        cl.lastElement().addStep(cScene);
                        break;
                    case "PLAY":
                        CueStep cPlay = CueStep.parsePlay(line, lineNo);
                        if (cPlay == null) return;
                        if (cl.size() == 0) return;
                        cl.lastElement().addStep(cPlay);
                        break;
                    case "STOP":
                        CueStep cStop = CueStep.parseStop(line, lineNo);
                        if (cStop == null) return;
                        if (cl.size() == 0) return;
                        cl.lastElement().addStep(cStop);
                        break;
                    case "VOLUME":
                        CueStep cVolume = CueStep.parseVolume(line, lineNo);
                        if (cVolume == null) return;
                        if (cl.size() == 0) return;
                        cl.lastElement().addStep(cVolume);
                        break;
                    case "DELAY":
                        CueStep cDelay = CueStep.parseDelay(line, lineNo);
                        if (cDelay == null) return;
                        if (cl.size() == 0) return;
                        cl.lastElement().addStep(cDelay);
                        break;
                    case "BLACK":
                        CueStep cBlack = CueStep.parseBlack(line, lineNo);
                        if (cBlack == null) return;
                        if (cl.size() == 0) return;
                        cl.lastElement().addStep(cBlack);
                        break;
                    default:
                        Alert.msg("invalid token '"+token+"' in show.txt:"+lineNo, TheApp.mainPanel);
                        return;
                    }
                }
            }
            in.close();
        } catch (Exception e) {
        }
        cueList = cl;
        if (cueList.size() > 0) {
            upcoming = 0;
        } else {
            upcoming = -1;
        }
    }
    
    /**
     * Gest the Cue to be executed next
     * 
     * @return    The Cue to be executed next or null if there
     *            is no such Cue.
     */
    public Cue getUpcoming() {
        try {
            return cueList.elementAt(upcoming);
        } catch (Exception failed) {
            return null;
        }
    }

    /** Goes one Cue back */
    public void prev() {
        if (upcoming < 0) return;
        if (cueList.size() == 0) return;
        if (--upcoming < 0) upcoming = 0;
    }

    /** Goes one Cue forward */
    public void next() {
        if (upcoming < 0) return;
        if (cueList.size() == 0) return;
        if (++upcoming >= cueList.size()) upcoming = cueList.size()-1;
    }

    /** Jumps to marker 1 if this is defined */
    public void gotoMarker1() {
        if (marker1 < 0) return;
        upcoming = marker1;
    }
    
    /** Jumps to marker 2 if this is defined */
    public void gotoMarker2() {
        if (marker2 < 0) return;
        upcoming = marker2;
    }
    
    /** Jumps to marker 3 if this is defined */
    public void gotoMarker3() {
        if (marker3 < 0) return;
        upcoming = marker3;
    }
    
    /** Jumps to marker 4 if this is defined */
    public void gotoMarker4() {
        if (marker4 < 0) return;
        upcoming = marker4;
    }

    /** The compiled list of cues */
    private Vector<Cue> cueList;

    /** The index of the next cue to execute in cueList (-1 = unset) */
    private int upcoming;

    /** The index of the marker 1 in cueList (-1 = unset) */
    private int marker1;

    /** The index of the marker 2 in cueList (-1 = unset) */
    private int marker2;
    
    /** The index of the marker 3 in cueList (-1 = unset) */
    private int marker3;

    /** The index of the marker 4 in cueList (-1 = unset) */
    private int marker4;

}
