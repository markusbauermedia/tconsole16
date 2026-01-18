package tconsole;

import java.util.Vector;

/**
 * A Cue contains the data of one cue in the cue list.
 * Each cue consists of a title, a (multiline) description
 * and a sequence of CueStep commands which are executed
 * one after each other when cue gets started.
 * 
 */
public class Cue implements Runnable {

    /**
     * Constructs a Cue object. Sets the title and initializes
     * / clears the description line and cue step list.
     * 
     * @param     t   The title of the queue
     */
    public Cue(String t) {
        title = t;
        description = new Vector<String>();
        steps = new Vector<CueStep>();
        listener = null;
    }

    /**
     * Adds a description line to the cue
     * 
     * @param     line   The description to add
     */
    public void addDescription(String line) {
        description.add(line);
    }

    /**
     * Executes the sequence of cue steps contained
     * in this cue. This happens detached in a background
     * thread, hence the UI is not blocked during wait
     * states in the cue step sequence.
     *
     * The execution has finished, an ExecutionListener
     * gets informed about this fact.
     * 
     * @param     l    The ExecutionListener to inform when
     *                 the cue step sequence jas been completely
     *                 processed.
     */
    public void execute(ExecutionListener l) {
        listener = l;
        new Thread(this).start();
    }

    /**
     * Delivers the cue title
     * 
     * @return    The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Delivers a description line stored with this cue.
     * 
     * @param     i    The index (o based) of the line
     *                 to return
     * @return         The requested description line or
     *                 null if no description line with this
     *                 index exists,
     */
    public String getDescription(int i) {
        try {
            return description.elementAt(i);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Delivers the number of description lines this cue contains.
     * 
     * @return      The number of description lines.
     */
    public int getDescriptionCount() {
        return description.size();
    }

    /**
     * Adds a cue step to thus cue.
     * 
     * @param     cs   The cue step to add.
     */
    public void addStep(CueStep cs) {
        steps.add(cs);
    }

    /**
     * Delays 'delayTime' milliseconds. Used internally.
     * 
     * @param     delayTime   The time to wait.
     */
    private void delay(long delayTime) {
        try {
            Thread.sleep(delayTime);
        } catch (Exception e) {}
    
    }

    /**
     * The background thread procedure which gets invoked when
     * 'execute()' is called. Calls execute() for all cue steps
     * and finally waits 200 msecs before the execution listener
     * gets informed.
     *
     * The delay shall avoid any mouse click bouncing, executing the
     * next cue is blocked in the UI until the first states that it's
     * ready.
     * 
     */
    public void run() {
        for (CueStep step: steps) step.execute();
        delay(200);
        if (listener != null) listener.executionFinished();
    }

    /** The execution listener to be informed when execution has completed. */
    private ExecutionListener listener;

    /** The title of the cue. Displayed in bold green letters in the UI */
    private String title;

    /** The description of the cue. The lines are displayed below the title */
    private Vector<String> description;

    /** The cue steps to be executed */
    private Vector<CueStep> steps;

}

