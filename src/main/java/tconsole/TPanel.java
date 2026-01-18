package tconsole;

import java.awt.*;
import javax.swing.*;


/**
 * TPanel is the version of JPanel commonly used in this 
 * application. TPanel usually has no LayoutManager set,
 * Components in this panel are places explicitly by
 * callin setBounds()
 */
public class TPanel extends JPanel {

    /**
     * Contstructs a TPanel with nor LayoutManager set
     */
    public TPanel() {
        super(null, true);
        setForeground(Skin.LINE);
        setBackground(Skin.BACKGROUND);
    }

    /**
     * Constructs a TPanel with the given LayoutManager
     * 
     * @param     l   The LayoutManager to use.
     */
    public TPanel(LayoutManager l) {
        super(l, true);
        setForeground(Skin.LINE);
        setBackground(Color.black);
    }

    /**
     * Used to add paint operations at the end of the paintComponent()
     * method.
     * 
     * @param     g    The graphics context to use for painting.
     */
    public void paintBuffered(Graphics g) {
    }

    /**
     * Overrides paintComponent to enable antialiasing and to add a call
     * to paintBuffered at the end.
     * 
     * @param     g    The graphics context to use for painting.
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        g.setColor(getForeground());
        paintBuffered(g);
    }

    /**
     * Overrides the paintBorder method to make it paint a simple
     * rectangle around the component if the 'framed' flöag ist set
     * 
     * @param     g    The graphics context to use for painting.
     */
    @Override
    public void paintBorder(Graphics g) {
        if (framed) {
            g.setColor(Skin.LINE);
            g.drawRect(0,0,getSize().width-1,getSize().height-1);
        }
    }

    /** empty definition, may be overriden by subclasses */
    public void close() {
    }

    /**
     * Adds a component to this panel and sets the size and position of
     * the component
     * 
     * @param     c        The component to add
     * @param     bounds   The size and position of the component, relative
     *                     to this panel
     */
    public void add(Component c, Rectangle bounds) {
        add(c);
        c.setBounds(bounds);
    }

    /**
     * Delivers the preferred size of this panel. If the panel
     * has a LayoutManager set, the size is derived from this.
     * With explicit component placing the actual size of the panel
     * is returned
     * 
     * @return    The preferred size of this panel.
     */
    @Override
    public Dimension getPreferredSize() {
        LayoutManager l = getLayout();
        if (l != null) return super.getPreferredSize();
        else return getSize();
    }

    /**
     * Sets the framed flag for this panel. With framed=true
     * a simple rectangle is drawn around the panel.
     * 
     * @param     f   True enables drawing the frame rectangle
     */
    public void setFramed(boolean f) {
        framed = f;
        repaint();
    }

    /** True enables drawing the frame rectangle */
    protected boolean framed = false;

}
