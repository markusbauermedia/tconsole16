package tconsole;

import java.awt.Rectangle;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.JLabel;

/**
 * The MainPanel contains the channel strips which make up
 * the main window UI. TheApp creates one MainPanel on startup
 * and displays it as its only component
 * 
 */
public class MainPanel extends TPanel {

    /**
     * Constructs a MainPanel
     * 
     */
    public MainPanel() {
        
        // we use a fixed position layout, the with calulates
        // as CHANNELS+1 * 71 pixels
        setSize(1207, 680);

        // there are CHANNELS + 1 channel strips, the very last one
        // is the control for the MP3 player
        for (int i = 0; i <= TheApp.CHANNELS; i++) {
            BasicChannelStrip c = null;
            // depending on the column different types of channel
            // strips are created
            switch (i) {
            case 0:  
                c = new BlackChannelStrip(i+1); 
                add(c, new Rectangle(i*71, 0, 71, 680));
                break;
            case 1:  
                c = new FadeInChannelStrip(i+1); 
                add(c, new Rectangle(i*71, 0, 71, 680));
                break;
            case 2:  
                c = new FadeOutChannelStrip(i+1); 
                add(c, new Rectangle(i*71, 0, 71, 680));
                break;
            case 3:  
                c = new LiveChannelStrip(i+1); 
                add(c, new Rectangle(i*71, 0, 71, 680));
                break;
            case 4:  
                c = new CuesChannelStrip(i+1); 
                add(c, new Rectangle(i*71, 0, 71, 680));
                break;
            case TheApp.CHANNELS:
                c = new AudioChannelStrip();
                add(c, new Rectangle(i*71, 0, 71, 680));
                break;
            default: 
                c = new ChannelStrip(i+1);  
                add(c, new Rectangle(i*71, 0, 71, 609));
                break;
            }
        }

        // sceneName / sceneLabel are shown while a scene is edited
        sceneLabel = new JLabel("Scene Name");
        sceneLabel.setFont(Skin.DIALOG12);
        sceneLabel.setForeground(Skin.LINE);
        add(sceneLabel, new Rectangle(380, 623, 90, 24));
        sceneName = new JTextField("NAME");
        sceneName.setFont(Skin.DIALOG14);
        add(sceneName, new Rectangle(380, 640, 90, 24));
        hideSceneName();
    }

    /**
     * Shows the scene name entry field, displays 'name' in it.
     * 
     * @param     name   The scene name to show.
     */
    public void showSceneName(String name) {
        sceneLabel.setVisible(true);
        sceneName.setVisible(true);
        sceneName.setText(name);
    }

    /**
     * Hides the scene name entry field. Reads the edited
     * scene name from the entry field and returns it
     * 
     * @return    The edited scene name
     */
    public String hideSceneName() {
        String name = sceneName.getText();
        sceneLabel.setVisible(false);
        sceneName.setVisible(false);
        return name;
    }

    /**
     * Delivers the ChannelStrip with the given column number.
     * 
     * @param     column   The column number as a String
     * @return             The ChannelStrip or null in case of any error
     */
    public ChannelStrip getChannelStrip(String column) {
        if (column == null) return null;
        for (Component c: getComponents()) {
            if (c instanceof ChannelStrip) {
                ChannelStrip cs = (ChannelStrip) c;
                if (column.equals(cs.getColumn())) return cs;
            }
        }
        return null;
    }

    /**
     * Delivers the (first) BlackChannelStrip in the Panel.
     * 
     * @return   The BlackChannelStrip or null in case of any error
     */
    public BlackChannelStrip getBlackChannelStrip() {
        for (Component c: getComponents()) {
            if (c instanceof BlackChannelStrip) {
                return (BlackChannelStrip) c;
            }
        }
        return null;
    }
    
    /**
     * Delivers the (first) AudioChannelStrip in the Panel.
     * 
     * @return   The AudioChannelStrip or null in case of any error
     */
    public AudioChannelStrip getAudioChannelStrip() {
        for (Component c: getComponents()) {
            if (c instanceof AudioChannelStrip) {
                return (AudioChannelStrip) c;
            }
        }
        return null;
    }
    
    /** the label displayed above the sceneName field */
    private JLabel sceneLabel;

    /** the entry field to edit the scene name */
    private JTextField sceneName;
}

