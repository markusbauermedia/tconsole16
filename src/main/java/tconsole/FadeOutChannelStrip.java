package tconsole;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.util.Locale;

/**
 * This is a special version of the channel strip containing a
 * control for the fade in time at the bottom
 * 
 */
public class FadeOutChannelStrip extends ChannelStrip 
    implements MouseListener, MouseWheelListener {
    
    /**
     * constructs the channel strip
     * 
     * @param     c  The channel strip number (1..16)
     */
    public FadeOutChannelStrip(int c) {
        super(c);
        // add the fade out time component
        fadeOutDisplay = new T7Segment("FADE OUT");
        add(fadeOutDisplay, new Rectangle(0, 609, 70, 70));
        // listen for mouse and mouse whell events
        fadeOutDisplay.addMouseWheelListener(this);
        fadeOutDisplay.addMouseListener(this);
        // show the initial fade in time in the component
        showFadeOut();
    }

    /**
     * The operator moved the mouse wheel while the mouse pointer 
     * being over the time display. This increments/descrements the
     * fade out time as the mouse wheel is moved.
     * 
     * @param     e     The MouseWheelEvent to process
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        // get the mouse wheel increment
        int notches = e.getWheelRotation();
        // each notch shifts the fade out time by 0.2 secs
        TheApp.fadeOut += notches * 200;
        // limit to 0..10 secs
        if (TheApp.fadeOut < 0) TheApp.fadeOut = 0;
        if (TheApp.fadeOut > 10000) TheApp.fadeOut = 10000;
        // show the incremented time
        showFadeOut();
    }

    /**
     * The operator clicked to the fade in display component.
     * This resets the fade time to its default value
     * 
     * @param     e     The MouseEvent to process
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (fadeOutDisplay == e.getSource()) {
            TheApp.fadeOut = Config.fadeOut;
            showFadeOut();
        } else {
            super.mouseClicked(e);
        }
    }

    /**
     * This internal helper method shows the actual fade time
     * in the display component.
     * 
     */
    private void showFadeOut() {
        fadeOutDisplay.setSegments(String.format(Locale.US, "%1.1f", TheApp.fadeOut * 0.001f));
    }
    
    /** The fade time */
    protected T7Segment fadeOutDisplay;
}



