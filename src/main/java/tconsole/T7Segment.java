// a pressable button
//
package tconsole;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A labelled 7-segment display used in this application. It is typically used
 * as a 71x71 fiels in a channel strip. It displays a number with a 7-segment font
 * and a small label above this.
 */
public class T7Segment extends JComponent implements MouseListener {
    
    /**
     * Constructs a T7Segment component.
     * 
     * @param     label   A label to be displayed in the upper part of the
     *                    component which describes the meaning of 7-segment
     *                    numbers below.
     */
    public T7Segment(String label) {
        this.label = label;
        setBackground(Skin.OBJECT);
        setForeground(Skin.LINE);
        setFont(Skin.DIALOG12);
        addMouseListener(this);
    }

    /**
     * Set the string to be displayed using the 7-segment font. Only digits
     * and the decimal point are allowed for this.
     * 
     * @param     s   Te text to display
     */
    public void setSegments(String s) {
        segments = s;
        repaint();
    }

    /**
     * Paints the component
     * 
     * @param     g   The graphic context to use for painting
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getBounds().width;
        int h = getBounds().height;

        Color bg = getBackground();
        if (isEnabled() && focussed) bg = bg.brighter();
        g2.setColor(bg);
        g2.fillRect(0, 0, w-1, h-1);

        if (isEnabled()) {
            g2.setColor(getForeground());
        } else {
            g2.setColor(Skin.DISABLED);
        }
        g2.drawRect(0, 0, w-1, h-1);
        
        g2.setFont(getFont());
        FontMetrics f = g2.getFontMetrics();
        int n = f.stringWidth(label);
        g2.drawString(label, (w-n)/2, 15);
        
        if (isEnabled()) {
            g2.setColor(Skin.GREEN);
        } else {
            g2.setColor(Skin.DISABLED);
        }
        g2.setFont(Skin.SEG7);
        f = g2.getFontMetrics();
        n = f.stringWidth(segments);
        g2.drawString(segments, (w-n)/2, 50);
    }

    /**
     * A mouse click happened on the component. Does nothing
     * 
     * @param     e  The MouseEvent to process.
     */
    public void mouseClicked(MouseEvent e)
    {
    }

    /**
     * The mouse was pressed on the component. Does nothing
     * 
     * @param     e  The MouseEvent to process.
     */
    public void mousePressed(MouseEvent e)
    {
    }
    
    /**
     * The mouse was released on the component. Does nothing
     * 
     * @param     e  The MouseEvent to process.
     */
    public void mouseReleased(MouseEvent e)
    {
    }
    
    /**
     * The mouse pointer entered the component. Requests input focus.
     * 
     * @param     e  The MouseEvent to process.
     */
    public void mouseEntered(MouseEvent e)
    {
        requestFocus();
        focussed = true;
        repaint();
    }
    
    /**
     * The mouse pointer exited the component. Releases input focus.
     * 
     * @param     e  The MouseEvent to process.
     */
    public void mouseExited(MouseEvent e)
    {
        getParent().requestFocus();
        focussed = false;
        repaint();
    }
    
    /** true if in focus, controls the background color */
    private boolean focussed = false;
    
    /** the label to be displayed above the 7-segment number */
    private String label = "";

    /** the text to display as 7-segment number */
    private String segments = "00";

}
