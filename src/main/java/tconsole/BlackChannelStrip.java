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
 * This is a special version of the channel strip containing the
 * 'black' button at the bottom for blacking out all lights immediately
 * 
 */
public class BlackChannelStrip extends ChannelStrip {
    
    /**
     * constructs the channel strip
     * 
     * @param     c  The channel strip number (1..16)
     */
    public BlackChannelStrip(int c) {
        super(c);
        // add the button
        blackButton = new TButton("BLCK");
        blackButton.setBackground(Skin.RED);
        blackButton.setFont(Skin.SANS);
        add(blackButton, new Rectangle(0, 609, 70, 70));
        // listen for button presses
        blackButton.addActionListener(this);
    }

    /**
     * Performs the blackout when the operator hit the button
     * 
     * @param     e   The action event to process.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // comes the event from the 'black' button?
        if (blackButton == e.getSource()) {
            // perform the blackout
            executeBlack();
        } else {
            // delegate to the base class method
            super.actionPerformed(e);
        }
    }

    /**
     * Executes 'black# got all channel strips
     */
    public void executeBlack() {
        for (ChannelStrip c: strips) c.setBlack();
    }

    /** the 'black' button */
    protected TButton blackButton;
}



