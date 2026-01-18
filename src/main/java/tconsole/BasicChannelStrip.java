package tconsole;

import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * This is the base variant of channel strips, providing only a label
 * and a fader as the only components which are commion to all channel
 * strip variants.
 *
 * The BasicChannelStrip implements the MouseListener interface although
 * it does not make use of it. Subclasses may overwrite single methods of
 * the interface without the need of definig them all.
 */
public class BasicChannelStrip extends TPanel 
    implements MouseListener {
    
    /**
     * constructs the channel strip
     * 
     */
    public BasicChannelStrip() {
        label = new JLabel("LABEL", SwingConstants.CENTER);
        label.setFont(Skin.DIALOG14);
        add(label, new Rectangle(0, 0, 71, 36));
        fader = new TFader();
        fader.addMouseListener(this);
        add(fader, new Rectangle(0, 36, 71, 466));
    }

    /**
     * The mouse was clicked 
     * 
     * @param     e   The mouse event to process
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * The mouse was pressed.
     * 
     * @param     e   The mouse event to process
     */
    public void mousePressed(MouseEvent e) {
    }
    
    /**
     * The mouse was released.
     * 
     * @param     e   The mouse event to process.
     */
    public void mouseReleased(MouseEvent e) {
    }
    
    /**
     * The mouse entered a component.
     * 
     * @param     e  The mouse event to process
     */
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     * The mouse exited a component.
     * 
     * @param     e   The mouse event to process
     */
    public void mouseExited(MouseEvent e) {
    }
    
    /** the fader in this component */
    protected TFader fader;

    /** the label at the top to the channel strip */
    protected JLabel label;
}


