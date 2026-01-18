package tconsole;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * This is a special version of the channel strip containing a
 * 'live' button at the bottom for switching to live mode (controls
 * all dimmers directly, not via scenes)
 * 
 */
public class LiveChannelStrip extends ChannelStrip {
    
    
    /**
     * Constructs the channel strip 
     * 
     * @param     c   The channel strip number (1..16)
     */
    public LiveChannelStrip(int c) {
        super(c);

        // add the button
        liveButton = new TButton("LIVE");
        liveButton.setBackground(Skin.BLUE);
        liveButton.setFont(Skin.SANS);
        add(liveButton, new Rectangle(0, 609, 70, 70));
        // listen for button presses
        liveButton.addActionListener(this);
    }

    /**
     * Switches to live mode when the operator hit the button
     * 
     * @param     e   The action event to process.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // was the live mode button pressed?
        if (liveButton == e.getSource()) {
            // toggle live mode
            if (live) {
                live = false;
                liveButton.setBackground(Skin.BLUE);
            } else {
                live = true;
                liveButton.setBackground(Skin.GREEN);
            }
            TheApp.live = live;
            enableFaders();
        } else {
            // delegate to the base class method
            super.actionPerformed(e);
        }
    }

    /** the live mode button */
    protected TButton liveButton;

    /** true = live mode is avtive */
    protected boolean live = false;

}



