package tconsole;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * TSliderUI  defines the special user interface for the level
 * sliders. The sliders use an image for the slider thumb.
 */
public class TSliderUI extends BasicSliderUI {
    
    /**
     * Constructs a TSliderUI object
     * 
     * @param     s   The slider for which this UI shall apply
     */
    public TSliderUI(JSlider s) {
        super(s);
        timg = Skin.SLIDER.getImage();
        dtimg = Skin.DSLIDER.getImage();
    }

    /**
     * Delivers the thumb size. Overrides the base class method to 
     * set the thumb image size as tumb size
     * 
     * @return    The size of the thumb image
     */
    @Override
    protected Dimension getThumbSize() {
        return new Dimension(timg.getWidth(slider), timg.getHeight(slider));
    }

    /**
     * Paints the slider thumb. Uses the image instead of the standard
     * slider thumb
     * 
     * @param     g   The graphics context to use
     */
    @Override
    public void paintThumb(Graphics g) {
        Rectangle t = thumbRect;
        if (slider.isEnabled()) g.drawImage(timg, t.x, t.y, slider);
        else g.drawImage(dtimg, t.x, t.y, slider);
    }

    /**
     * Overrides the paintFocus method to disable the focus rectangle
     * normally painted for a slider
     * 
     * @param     g   The graphics context to use.
     */
    @Override
    public void paintFocus(Graphics g) {
    }

    /**
     * Delivers the width of the slider
     * 
     * @return    The width of the slider
     */
    public int getSliderWidth() {
        return timg.getWidth(slider);
    }

    /** the thumb image (enabled state) */
    private Image timg;

    /** the thumb imafe (disabled state) */
    private Image dtimg;

}

