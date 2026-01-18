package tconsole;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The CueDisplay is part of the CueWindow, it shows the title
 * and the description of the cue to be executed next.
 */
public class CueDisplay extends JComponent {
    
    /**
     * Constructs a CueDisplay component.
     */
    public CueDisplay() {
        setBackground(Skin.BACKGROUND);
        setForeground(Skin.LINE);
        setFont(Skin.DIALOG12);
        cue = null;
        topLine = "loading show.txt";
    }

    /**
     * Sets the cue object to be shown, The component
     * gets totle and description from it.
     * 
     * @param     c   The cue to display
     */
    public void setCue(Cue c) {
        cue = c;
        repaint();
    }

    /**
     * Sets the top line text in the component this is either
     * 'next cue:" or 'executing ...'. the latter is displayed
     * red while active.
     * 
     * @param     tl    The top line text to show.
     */
    public void setTopLine(String tl) {
        topLine = tl;
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

        // draw the background
        g2.setColor(getBackground());
        g2.fillRect(0, 0, w-1, h-1);

        // set the color for the top line text and draw this
        if ("executing ...".equals(topLine)) {
            g2.setColor(Color.red);
        } else {
            g2.setColor(getForeground());
        }
        g2.setFont(getFont());
        g2.drawString(topLine, 4, 20);
        
        // draw the cue title and description
        g2.setFont(Skin.SANS);
        g2.setColor(Skin.GREEN);
        if (cue != null) {
            g2.drawString(cue.getTitle(), 4, 50);
            g2.setColor(getForeground());
            g2.setFont(getFont());
            for (int i=0; i<cue.getDescriptionCount(); i++) {
                g2.drawString(cue.getDescription(i), 4, 70+i*16);
            }
        } else {
            g2.drawString("empty cue list.", 4, 50);
        }
    }

    /** the cue to display */
    private Cue cue;

    /** the top line text to show */
    private String topLine;

}

