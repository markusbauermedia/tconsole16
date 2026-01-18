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
public class FadeInChannelStrip extends ChannelStrip 
    implements MouseListener, MouseWheelListener{
    
    /**
     * constructs the channel strip
     * 
     * @param     c  The channel strip number (1..16)
     */
    public FadeInChannelStrip(int c) {
        super(c);
        // add the fade in time component
        fadeInDisplay = new T7Segment("FADE IN");
        add(fadeInDisplay, new Rectangle(0, 609, 70, 70));
        // listen for mouse and mouse whell events
        fadeInDisplay.addMouseWheelListener(this);
        fadeInDisplay.addMouseListener(this);
        // show the initial fade in time in the component
        showFadeIn();
    }

    /**
     * The operator moved the mouse wheel while the mouse pointer 
     * being over the time display. This increments/descrements the
     * fade in time as the mouse wheel is moved.
     * 
     * @param     e     The MouseWheelEvent to process
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        // get the mouse wheel increment
        int notches = e.getWheelRotation();
        // each notch shifts the fade in time by 0.2 secs
        TheApp.fadeIn += notches * 200;
        // limit to 0..10 secs
        if (TheApp.fadeIn < 0) TheApp.fadeIn = 0;
        if (TheApp.fadeIn > 10000) TheApp.fadeIn = 10000;
        // show the incremented time
        showFadeIn();
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
        if (fadeInDisplay == e.getSource()) {
            TheApp.fadeIn = Config.fadeIn;
            showFadeIn();
        } else {
            super.mouseClicked(e);
        }
    }
    
    /**
     * This internal helper method shows the actual fade time
     * in the display component.
     * 
     */
    private void showFadeIn() {
        fadeInDisplay.setSegments(String.format(Locale.US, "%1.1f", TheApp.fadeIn * 0.001f));
    }
    
    /** The fade time */
    protected T7Segment fadeInDisplay;
}



