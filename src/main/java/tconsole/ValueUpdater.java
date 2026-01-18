package tconsole;

/**
 * The interface ValueUpdater is used to update fader positions
 * from the automatic updates made by the Levels object.
 */
public interface ValueUpdater {
    
    /** updates the value */
    public void updateValue(float v);

}

