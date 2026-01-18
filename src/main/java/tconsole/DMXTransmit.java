package tconsole;

import javax.swing.SwingUtilities;
import ch.bildspur.artnet.ArtNetClient;


/**
 * DMXTransmit accumulates the scene levels, translates the levels
 * to the physical range in DMX and sends the Artnet/DMX frames.
 * 
 */
public class DMXTransmit extends Thread
{

    /**
     * The private standard constructor prohibits from creating
     * instances of this class. The correct way to create a 
     * DMXTransmit is a call to DMXTransmit.initialize()
     * 
     */
    private DMXTransmit()
    {
        setDaemon(true);
        start();
    }

    /**
     * Initializes the DMXTransmit class. Creates one instance of
     * the class which does the DMX tranmitting in background.
     * 
     */
    public static DMXTransmit initialize() {
        if (theOne == null) theOne = new DMXTransmit();
        return theOne;
    }
    
    /**
     * Sets a logical channel level directly. Used only in live
     * edit mode.
     * 
     * @param     lch   The logical channel number (1..16, the
     *                  channel strip number)
     * @param     v     The value to set (0..1)
     */
    public synchronized static void setLCH(int lch, float v)
    {
        // abort if the channel number is out of range
        if (lch > TheApp.CHANNELS) return;
        lch--;
        if (lch < 0) return;

        // set the value
        if (v < 0.0f) v = 0.0f;
        if (v > 1.0f) v = 1.0f;
        accumulator[lch] = v;
    }

    /**
     * Sets an Artnet / DMX value directly. Used for the control
     * of the MP3 player which bypasses the normal channel fader logic.
     * 
     * @param     pch   The physical DMX channel (1..512)
     * @param     v     The value to set (0..255)
     */
    public synchronized static void setPCH(int pch, int v)
    {
        // abort if the channel number is out of range
        if (pch > SIZE) return;
        pch--;
        if (pch < 0) return;

        // set the value
        dmxdata[pch] = (byte) v;
    }
    
    /**
     * Updates the fader positions in the UI to make the fade in/out of
     * scenes visible for the operator.
     */
    private void updateUI()
    {
        // do not update faders if in edit or live mode
        if (TheApp.edit) return;
        if (TheApp.live) return;

        // do the update MT safe
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    for (int i=1; i<=TheApp.CHANNELS; i++) {
                        ChannelStrip.getChannelFader(i).setValue(accumulator[i-1]);
                    }
                    TheApp.mainPanel.repaint();
                }
            }
        );
    }

    /**
     * Accumulates the actual levels from all scenes. Makes 'accumulator' 
     * contain the max value for every channel found in any of the scenes.
     * 
     * Does nothing if in live edit mode.
     */
    private void accumulate() {
        // skip if in live mode
        if (TheApp.live) return;
        // clear the accumulator
        for (int ch = 0; ch < TheApp.CHANNELS; ch++) accumulator[ch] = 0.0f;
        // process all scenes
        for (int sc = 1; sc <= TheApp.SCENES; sc++) {
            Scene scene = Scene.getScene(sc);
            if (!scene.mustAccumulate()) continue;
            float sv[] = scene.getActualValues();
            // in every scene, if a channel value is higher than the accumulated
            // value, replace it.
            for (int ch = 0; ch < TheApp.CHANNELS; ch++) {
                if (sv[ch] > accumulator[ch]) accumulator[ch] = sv[ch];
            }
        }
    }

    /**
     * Translates the logical channel level (floating point 0..1)
     * To the Artnet/DMX range. This version simply does a linear 
     * conversion. Future versions may include pre-heat and non-linar
     * characteristics.
     * 
     * @param     l   The logical channel level (0..1)
     * @return        The physical channel level (0..255)
     */
    private byte translateLevel(float l) {
        int i =  ((int) (l * 255.0)) & 0xFF;
        return (byte) i;
    }

    /** 
     * The background thread. Sends DMX/Artnet messages at about 30Hz.
     *
     * Accumulates the scene levels in every cycle, translates the logical
     * 0..1 values to the DMX 0..255 range and sends the Artnet frame
     */
    public void run()
    {
        // create and start the Artnet client on startup
        ArtNetClient artnet = new ArtNetClient();
        artnet.start();

        // the endless loop
        for (;;) {
            // accumulate all scenes
            accumulate();
            for (int ch = 0; ch < TheApp.CHANNELS; ch++) {
                // do channel patching
                int pch = Config.dmxPatch[ch]-1;
                // translate the logical level to 0..255
                dmxdata[pch] = translateLevel(accumulator[ch]);
            }
            // send the artnet frame
            artnet.broadcastDmx(0, 0, dmxdata);
            // update the (gray) faders in the the UI
            updateUI();
            // 28msecs sleep for about 30 Artnet messages per second.
            try { sleep(28); } catch (Exception e) {}
        }
    }

    /** the DMX data buffer size, we support the full 512 channel range */
    public static final int SIZE = 512;

    /** the DMX data buffer */
    private static byte dmxdata[] = new byte[SIZE];

    /** accumulates the actual levels from all scenes */
    private static float accumulator[] = new float[TheApp.CHANNELS];

    /** the one and only instance of DMXTransmit */
    private static DMXTransmit theOne = null;

}
