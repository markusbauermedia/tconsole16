// a pressable button
//
package tconsole;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A JButton implementation used for buttons in this application.
 */
public class TButton extends JButton implements MouseListener {
    
    /**
     * Constructs a labelled TButton
     * 
     * @param     label   The label text
     */
    public TButton(String label) {
        super(label);
        setBackground(Skin.OBJECT);
        setForeground(Skin.LINE);
        setFont(Skin.DIALOG14);
        addMouseListener(this);
    }
    
    /**
     * Constructs a TButton showing an icon.
     * 
     * @param     icon    The Icon to use
     */
    public TButton(Icon icon) {
        super(icon);
        setBackground(Skin.OBJECT);
        setForeground(Skin.LINE);
        setFont(Skin.DIALOG14);
        addMouseListener(this);
    }

    /**
     * Defines if the button shal be drawn with a frame or not.
     * TButtons are framed by default.
     * 
     * @param     b   False disables the frame
     */
    public void setFramed(boolean b) {
        drawBorder = b;
    }

    /**
     * Controls if the component shall request focus if the mouse
     * enters the component. This feature is enabled by default
     * 
     * @param     flag   True = request focus when the mouse enters
     *                   the component
     */
    public void setFocusOnMouseEnter(boolean flag) {
        focusOnMouseEnter = flag;
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
        if (drawBorder) g2.drawRect(0, 0, w-1, h-1);
        if (getText() != null) {
            g2.setFont(getFont());
            FontMetrics f = g2.getFontMetrics();
            int n = f.stringWidth(getText());
            int m = f.getHeight();
            g2.drawString(getText(), (w-n)/2, (h+m)/2-5);
        }
        Icon icon = getIcon();
        if (icon != null) {
            int x = (w - icon.getIconWidth()) / 2;
            int y = (h - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2, x, y);
        }
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
        if (focusOnMouseEnter) requestFocus();
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
        if (focusOnMouseEnter) TheApp.mainPanel.requestFocus();
        focussed = false;
        repaint();
    }
    
    /** controls if a frame is painted around the button */
    private boolean drawBorder = true;
    
    /** true = in focus, controls the backgound color */
    private boolean focussed = false;

    /** controls if the component shall request focus when the mouse enters */
    private boolean focusOnMouseEnter = true;

}
