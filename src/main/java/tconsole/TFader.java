package tconsole;

import java.awt.Dimension;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


/**
 * TFader is the JSlider implementation used for the channel faders in
 * this application.
 *
 */
public class TFader extends JSlider 
    implements MouseWheelListener, ChangeListener {
    
    /**
     * Constructs a TFader object.
     * 
     * @param     v   The ValueUpdater to use
     */
    public TFader() {
        super(JSlider.VERTICAL, 0, 1000, 0);
        vud = null;
        setForeground(Skin.LINE);
        setBackground(Skin.BACKGROUND);
        tui = new TSliderUI(this);
        setUI(tui);
        addMouseWheelListener(this);
        addChangeListener(this);
        setEnabled(true);
    }

    /**
     * Sets the value updater used by this fader. The value updater
     * gets informed when the operator changed the fader position.
     * 
     * @param     v   The value updater
     */
    public void setValueUpdater(ValueUpdater v) {
        vud = v;
    }

    /**
     * Called by the framework when the operator changed the slider
     * position.
     * 
     * @param     e   The event to process
     */
    public void stateChanged(ChangeEvent e) {
        if (vud != null) vud.updateValue(getValue()*0.001f);
    }

    /**
     * Updates the fader position from a 0..1 floating point 
     * value. 
     * 
     * @param     value   The value to set.
     */
    public void setValue(float value) {
        int newValue = (int) (value * 1000.0f);
        if (newValue < 0) newValue = 0;
        if (newValue > 1000) newValue = 1000;
        setValue(newValue);
    }

    /**
     * Called by the framework when the mouse wheel gets rotated
     * 
     * @param     e   The event to process
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (!isEnabled()) return;
        int h = getSize().height;
        int d = h / 1000;
        if (d < 1) d = 1;
        int notches = e.getWheelRotation() * 20;
        setValue(getValue() + d*notches);
    }

    /**
     * Delivers the preferred size of this slider object.
     * 
     * @return    The preferred size
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(tui.getSliderWidth()+10, 400);
    }

    /** The slider UI to use */
    private TSliderUI tui;

    /** The ValueUpdate to be informed when the slider position changes */
    private ValueUpdater vud = null;

}

