package tconsole;

import java.awt.*;
import javax.swing.*;

/**
 * A SceneButton is part of the channel strip. It shows/hides
 * a stored scene.
 */
public class SceneButton extends TButton {

    /**
     * Contructs a SceneButton
     * 
     * @param     column   The column/scene number (1..16)
     * @param     s        The scene associated with this button
     */
    public SceneButton(int column, Scene s) {
        super(""+column);
        scene = s;
    }

    /**
     * Paints the component
     * 
     * @param     g   The graphic context to use for painting
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getBounds().width;
        int h = getBounds().height;
        if (isEnabled()) {
            g2.setColor(getForeground());
        } else {
            g2.setColor(Skin.DISABLED);
        }
        g2.setFont(Skin.DIALOG12);
        FontMetrics f = g2.getFontMetrics();
        String name = scene.getName();
        int n = f.stringWidth(name);
        g2.drawString(name, (w-n)/2, 15);
    }

    /** The scene associated with this button */
    private Scene scene;
}

