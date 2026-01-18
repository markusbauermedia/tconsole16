package tconsole;


/**
 * The ExecutionListener interface is used to tell the UI when
 * a cue has been completely processed.
 * 
 */
public interface ExecutionListener {

    /**
     *  Thells that a cue's execution has completed.
     * 
     */
    public void executionFinished();

}

