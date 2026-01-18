package tconsole;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * This is a special version of the channel strip which controls
 * Volume, song selections an start/stop of a Radig DMX MP3 player
 * 
 */
public class AudioChannelStrip extends BasicChannelStrip 
    implements ValueUpdater, ActionListener, 
    MouseWheelListener, KeyListener {
    
    /**
     * Constructs the channel strip
     * 
     */
    public AudioChannelStrip() {
        super();
        label.setText("VOLUME");
        // the song number decrement button
        decButton = new TButton("<");
        decButton.addActionListener(this);
        add(decButton, new Rectangle(0, 510, 34, 27));
        // the sonb number increment button
        incButton = new TButton(">");
        incButton.addActionListener(this);
        add(incButton, new Rectangle(35, 510, 35, 27));
        // the play button
        playButton = new TButton("PLAY");
        playButton.addActionListener(this);
        playButton.setFont(Skin.BOLD14);
        playButton.setBackground(Skin.BLUE);
        add(playButton, new Rectangle(0, 538, 70, 42));
        // the stop button
        stopButton = new TButton("STOP");
        stopButton.addActionListener(this);
        stopButton.setBackground(Skin.RED);
        stopButton.setFont(Skin.BOLD14);
        add(stopButton, new Rectangle(0, 581, 70, 27));
        // the song number
        songDisplay = new T7Segment("SONG");
        songDisplay.setSegments("01");
        add(songDisplay, new Rectangle(0, 609, 70, 70));
        // the fader controls the volume
        fader.setValue(1000);
        fader.setValueUpdater(this);
        // we are processing events from the song display
        songDisplay.addMouseWheelListener(this);
        songDisplay.addKeyListener(this);
    }


    /**
     * Delays program execution for a numver of msecs
     * 
     * @param     msecs  The time to delay
     */
    private void delay(int msecs) {
        try {
            Thread.sleep(msecs);
        } catch (Exception e) {
        }
    }

    /**
     * Plays the selected song
     */
    private void play() {
        if (Config.audioMode == Config.RADIG) {
            DMXTransmit.setPCH(Config.audioPlayStop, 0);
            delay(50);
            int songVal = (255*song)/21 + 6; 
            DMXTransmit.setPCH(Config.audioSongSelect, songVal);
            DMXTransmit.setPCH(Config.audioPlayStop, 255);
        } else {
            MPlayer.play(song);
        }
    }

    /**
     * Plays the song with the given number
     * 
     * @param     snr   The song number to play
     */
    public void play(int snr) {
        song = snr;
        songDisplay.setSegments(String.format("%02d", song));
        play();
    }

    /**
     * Stops playing the actual song.
     */
    public void stop() {
        if (Config.audioMode == Config.RADIG) {
            DMXTransmit.setPCH(Config.audioPlayStop, 0);
        } else {
            MPlayer.stop();
        }
    }

    /**
     * Processes button presses (inc / dec/ start /stop) 
     * 
     * @param     e   The ActionEvent to process
     */
    public void actionPerformed(ActionEvent e) {
        if (incButton == e.getSource()) {
            int maxSong = (Config.audioMode == Config.RADIG) ? 21 : 99;
            if (song < maxSong) song++;
            songDisplay.setSegments(String.format("%02d", song));
        } else if (decButton == e.getSource()) {
            if (song > 1) song--;
            songDisplay.setSegments(String.format("%02d", song));
        } else if (playButton == e.getSource()) {
            play();
        } else if (stopButton == e.getSource()) {
            stop();
        }
    }

    /**
     * Increments / descrements the song number with the mouse wheel
     * when moved over the dong number display
     * 
     * @param     e    The MouseWheelEvent to process
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        song += notches;
        int maxSong = (Config.audioMode == Config.RADIG) ? 21 : 99;
        if (song < 1) song = 1;
        if (song > maxSong) song = maxSong;
        songDisplay.setSegments(String.format("%02d", song));
    }

    /**
     * Called on key press envents. does nothing. 
     * 
     * @param     e   The key event to process.
     */
    public void keyPressed(KeyEvent e) {
    }

    /**
     * Called on key release envents. does nothing. 
     * 
     * @param     e   The key event to process.
     */
    public void keyReleased(KeyEvent e) {
    }
    
    /**
     * Increments/decrements the song number if the operator
     * types '+' or '-'
     * 
     * @param     e   The key event to process
     */
    public void keyTyped(KeyEvent e) {
        int maxSong = (Config.audioMode == Config.RADIG) ? 21 : 99;
        if (e.getKeyChar() == '+') {
            if (song < maxSong) song++;
            songDisplay.setSegments(String.format("%02d", song));
        } else if (e.getKeyChar() == '-') {
            if (song > 1) song--;
            songDisplay.setSegments(String.format("%02d", song));
        } else if ((e.getKeyChar() >= '0')&&(e.getKeyChar() <= '9')) {
            int i = e.getKeyChar() - '0';
            song = ((song*10)%100) + i;
            if (song > maxSong) song=maxSong;
            songDisplay.setSegments(String.format("%02d", song));
        }
    }

    /**
     * Sets the audio volume in percent
     * 
     * @param     v  The audio volume
     */
    public void volume(int v) {

        if (Config.audioMode == Config.RADIG) {
            int volume = (v * 255) / 100;
            DMXTransmit.setPCH(Config.audioVolume, volume);
        } else {
            int volume = v;
            MPlayer.volume(volume);
        }
        fader.setValue(v*10);
    }

    /**
     * Controls the volume when the fader gets moved.
     * 
     * @param     v   The fader position (0..1)
     */
    public void updateValue(float v) {
        if (Config.audioMode == Config.RADIG) {
            int volume = (int) (v * 255);
            DMXTransmit.setPCH(Config.audioVolume, volume);
        } else {
            int volume = (int) (v * 100);
            MPlayer.volume(volume);
        }
    }

    /** The play button */
    protected TButton playButton;
    
    /** The stop button */
    protected TButton stopButton;
    
    /** The increment song number button */
    protected TButton incButton;

    /** The decrement song number button */
    protected TButton decButton;

    /** shows the actual song number */
    protected T7Segment songDisplay;

    /** true = song is playing */
    protected boolean playing = false;

    /** the actual song number */
    protected int song = 1;

}



