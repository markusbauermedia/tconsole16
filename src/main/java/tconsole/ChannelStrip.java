package tconsole;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 * This class defined the standard channel strip. Beside the label and the fader
 * inherited from the BasicChannelStrip, the ChannelStrip containd an 'scene' button
 * to launch a stored scene (cue) and an 'edit' button used to edit the fader settings
 * of this particular scene.
 * 
 * If not in edit-mode, all ChanneStrip faders are read only and show the state of the
 * actual output.
 * 
 */
public class ChannelStrip extends BasicChannelStrip 
    implements ValueUpdater, ActionListener, Runnable {
    
    /**
     * constructs the channel strip  
     * 
     * @param     c   The channel / column number (1..16)
     */
    public ChannelStrip(int c) {
        
        // be sure that 'c' is valid
        super();
        if (c < 1) c = 1;
        if (c > TheApp.CHANNELS) c = TheApp.CHANNELS;
        column = c;

        // create a scene for this channel strip 
        scene = new Scene(c);

        // set the label
        label.setText(Config.dmxName[c-1]);

        // add the 'edit' button below the fader
        editButton = new TButton("EDIT");
        editButton.addActionListener(this);
        add(editButton, new Rectangle(0, 510, 70, 27));
        
        // add the 'scene' button below the 'edit' button
        sceneButton = new SceneButton(column, scene);
        sceneButton.setFont(Skin.SANS);
        sceneButton.addActionListener(this);
        add(sceneButton, new Rectangle(0, 538, 70, 70));
        
        // subscribe for fader position updates, disable the fader by default
        fader.setValueUpdater(this);
        fader.setEnabled(false);

        // remember this component in the 'strips' list
        strips[c-1] = this;
    }

    /** 
     * sets all faders to the position which represent the programmed
     * values for this scene. This is done delayed in a separate thread
     * to avoid conflicts with the DMXTransmit background thread which
     * may still try to update the faders to the output positions
     */
    private void setFadersToProgrammed() {
        new Thread(this).start();
    }

    /**
     * Background thread started by setFadersToProgrammed()
     *
     * Sets all faders to their programmed positions in this scene
     * after a short delay time
     * 
     */
    public void run() {
        // wait 100msecs to be sure that the DMXTransmit thread
        // has recognized the change to edit mode
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        // set all faders to their position programmed in this scene
        // do this MT safe
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    for (int ch=1; ch <= TheApp.CHANNELS; ch++) {
                        strips[ch-1].fader.setValue(scene.getValue(ch));
                    }
                    TheApp.mainPanel.repaint();
                }
            }
        ); 
    }

    /**
     * Called if the operator activated one of the buttons in this
     * ChannelStrip
     * 
     * @param     e    The ActionEvent to process
     */
    public void actionPerformed(ActionEvent e) {
        
        // edit button pressed?
        if (editButton == e.getSource()) {
            // toggle edit mode
            if (editMode) {
                // switch edit mode off.
                editMode = false;
                editButton.setBackground(Skin.OBJECT);
                editButton.setForeground(Skin.LINE);
                scene.setEdit(false);
                scene.setName(TheApp.mainPanel.hideSceneName());
                editedScene = null;
                TheApp.edit = false;
                enableFaders();
                Scene.save();
            } else {
                if (editedScene != null) editedScene.setName(TheApp.mainPanel.hideSceneName());
                editedScene = scene;
                TheApp.edit = true;
                enableFaders();
                for (ChannelStrip s: strips) {
                    if (s == this) {
                        s.editMode = true;
                        s.editButton.setBackground(Skin.RED);
                        s.editButton.setForeground(Color.white);
                        s.scene.setEdit(true);
                        setFadersToProgrammed();
                        TheApp.mainPanel.showSceneName(s.scene.getName());
                    } else {
                        s.editMode = false;
                        s.editButton.setBackground(Skin.OBJECT);
                        s.editButton.setForeground(Skin.LINE);
                        s.scene.setEdit(false);
                    }
                }
            }
        // scene button pressed ?
        } else if (sceneButton == e.getSource()) {
            // toggle scenet show
            if (sceneShows) {
                sceneShows = false;
                sceneButton.setBackground(Skin.OBJECT);
                scene.fadeOut(TheApp.fadeOut);
            } else {
                sceneShows = true;
                sceneButton.setBackground(Skin.GREEN);
                scene.fadeIn(TheApp.fadeIn);
            }
        }
    }

    /**
     * Called if the operator shanged the fader position 
     * 
     * @param     v   The fader position (0..1)
     */
    public void updateValue(float v) {
        if (TheApp.edit) {
            if (editedScene == null) return;
            editedScene.setValue(column, v);
        } else if (TheApp.live) {
            DMXTransmit.setLCH(column, v);
        }
    }

    /**
     * Immediately switched the scene of this ChannelStrip black
     * 
     */
    public void setBlack() {
        sceneShows = false;
        sceneButton.setBackground(Skin.OBJECT);
        scene.fadeOut(0);
    }
    
    /**
     * Delivers the number on the scene button as a string 
     * 
     * @return    The number on the scene button on a string.
     */
    public String getColumn() {
        return ""+column;
    }

    /**
     * Fades in the scene as if triggered by mouse click in the UI.
     * Does this MT safe and uses the fadeTime passed to the call
     * instead of the standard fade time.
     * 
     * @param     fadeTime    The fade time to use (msecs)
     */
    public void fadeIn(int fadeTime) {
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    sceneShows = true;
                    scene.fadeIn(TheApp.fadeIn);
                    sceneButton.setBackground(Skin.GREEN);
                    repaint();
                }
            }
        ); 
    }

    /**
     * Fades out the scene as if triggered by mouse click in the UI.
     * Does this MT safe and uses the fadeTime passed to the call
     * instead of the standard fade time.
     * 
     * @param     fadeTime    The fade time to use (msecs)
     */
    public void fadeOut(int fadeTime) {
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    sceneShows = false;
                    scene.fadeOut(fadeTime);
                    sceneButton.setBackground(Skin.OBJECT);
                    repaint();
                }
            }
        ); 
    }

    /**
     * Enables / disables all channel faders according to the global
     * edit / live edit mode
     */
    public static void enableFaders() {
        boolean enable = TheApp.edit || TheApp.live;
        for (ChannelStrip c: strips) c.fader.setEnabled(enable);
    }

    /**
     * Delivers a reference to the fader object of a particular
     * ChannelStrip. 
     * 
     * @param     ch   The channel strip number (1..16)
     * @return         The fader used by this strip
     */
    public static TFader getChannelFader(int ch) {
        if (ch < 1) ch = 1;
        if (ch > TheApp.CHANNELS) ch = TheApp.CHANNELS;
        return strips[ch-1].fader;
    }

    /** the 'scene' button */
    protected TButton sceneButton;

    /** the 'edit' button */
    protected TButton editButton;
    
    /** the column number of the ChannelStrip */
    protected int column;

    /** true = this scene gets actually edited */
    protected boolean editMode = false;

    /** true = this scene is actually showing */
    protected boolean sceneShows = false;

    /** the scene linked to the scene button in this channel strip */
    protected Scene scene;

    /** a static list of all ChannelStrips */
    protected static ChannelStrip strips[] = new ChannelStrip[TheApp.CHANNELS];

    /** the scene which is actually edited */
    protected static Scene editedScene = null;
}

