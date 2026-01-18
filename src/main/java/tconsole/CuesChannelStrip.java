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
 * 'cues' button at the bottom for showing the CUES window.
 * 
 */
public class CuesChannelStrip extends ChannelStrip {
    
    
    /**
     * Constructs the channel strip 
     * 
     * @param     c   The channel strip number (1..16)
     */
    public CuesChannelStrip(int c) {
        super(c);

        // add the button
        cuesButton = new TButton("CUES");
        cuesButton.setFont(Skin.SANS);
        add(cuesButton, new Rectangle(0, 609, 70, 70));
        // listen for button presses
        cuesButton.addActionListener(this);
    }

    /**
     * Shows / hides the CUES window mode when the operator hit the button
     * 
     * @param     e   The action event to process.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // was the cuesShowing mode button pressed?
        if (cuesButton == e.getSource()) {
            CueWindow w = new CueWindow(cuesButton);
            w.setVisible(true);
        } else {
            // delegate to the base class method
            super.actionPerformed(e);
        }
    }

    /** the cuesShowing mode button */
    protected TButton cuesButton;


}



