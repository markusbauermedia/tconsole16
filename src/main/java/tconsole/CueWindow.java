package tconsole;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.Rectangle;

/**
 * The CueWindow is shown when the operator clicks the 'CUES' button.
 * It shows the actual cue from the 'show.txt' file along with buttons
 * to navigate in the show and to run the actual cue
 * 
 */
public class CueWindow extends JFrame 
    implements ActionListener, ExecutionListener, KeyListener {

    /**
     * Constructs and shows the CueWindow.
     * 
     * @param     b   The button which launched the CueWindow.
     *                This button gest disabled while the window
     *                is vissible
     */
    public CueWindow(TButton b) {
    
        // remember the button which launched the window
        // ans disable it temporarely
        cueButton = b;
        cueButton.setEnabled(false);

        // create a new CueList from the 'show.txt' file
        cueList = new CueList();

        // some basic window stuff, keep the window always on top
        setTitle("TConsole16 Cue List");
        setResizable(false);
        setAlwaysOnTop(true);

        // the panel contains all components of the window
        panel = new TPanel();
        panel.setSize(400, 250);

        // 'display' shows title and description of the cue which
        // will be run when 'go' is activated. display will get and
        // keep keyboard focus to be the source of keyboard events
        // while the window ist showing.
        display = new CueDisplay();
        panel.add(display, new Rectangle(0,0,399,189));
        Cue cue = cueList.getUpcoming();
        display.setCue(cue);
        display.setTopLine((cue!=null) ? "next cue:": "");
        display.addKeyListener(this);
        display.requestFocus();

        // the go button runs the actual cue and advances to the next
        // one after execution of this had completed.
        go = new TButton("GO");
        go.setFont(Skin.SANS);
        go.setForeground(Skin.GREEN);
        go.addActionListener(this);
        go.setFocusOnMouseEnter(false);
        panel.add(go, new Rectangle(0,190,399,33));

        // the '<' button goes one step back in the cue list 
        // without executing the cue
        TButton prev = new TButton("<");
        prev.addActionListener(this);
        prev.setFocusOnMouseEnter(false);
        panel.add(prev, new Rectangle(0,225,39,24));
        
        // the marker1 button jumps to marker 1 in the list
        // if such a marker is defined in 'show.txt'
        TButton marker1 = new TButton("M1");
        marker1.addActionListener(this);
        marker1.setFocusOnMouseEnter(false);
        panel.add(marker1, new Rectangle(40,225,79,24));

        // the marker2 button jumps to marker 2 in the list
        // if such a marker is defined in 'show.txt'
        TButton marker2 = new TButton("M2");
        marker2.addActionListener(this);
        marker2.setFocusOnMouseEnter(false);
        panel.add(marker2, new Rectangle(120,225,79,24));
        
        // the marker3 button jumps to marker 3 in the list
        // if such a marker is defined in 'show.txt'
        TButton marker3 = new TButton("M3");
        marker3.addActionListener(this);
        marker3.setFocusOnMouseEnter(false);
        panel.add(marker3, new Rectangle(200,225,79,24));
        
        // the marker4 button jumps to marker 4 in the list
        // if such a marker is defined in 'show.txt'
        TButton marker4 = new TButton("M4");
        marker4.addActionListener(this);
        marker4.setFocusOnMouseEnter(false);
        panel.add(marker4, new Rectangle(280,225,79,24));
        
        // the '>' button goes one step forward in the cue list 
        // without executing the cue
        TButton next = new TButton(">");
        next.addActionListener(this);
        next.setFocusOnMouseEnter(false);
        panel.add(next, new Rectangle(360,225,39,24));

        add(panel);
        pack();
        setLocationRelativeTo(null);
    
    }

    /**
     * The callback method invoked by the cue list after 
     * executing a cue. It advances the cue pointer and
     * shows the next cue in the display
     *
     * Also re-enables the go button which was disabled
     * when cie execution started.
     */
    public void executionFinished() {
        cueList.next();
        // do the update MT safe
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    display.setTopLine("next cue:");
                    display.setCue(cueList.getUpcoming());
                    go.setEnabled(true);
                    display.setEnabled(true);
                }
            }
        );
    }


    /**
     * Processes all button clicks to the window. 
     * 
     * @param     e   The action event to process
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
        case "GO":
            Cue c = cueList.getUpcoming();
            display.setTopLine("executing ...");
            go.setEnabled(false);
            display.setEnabled(false);
            if (c != null) c.execute(this);
            break;
        case "<":
            cueList.prev();
            display.setCue(cueList.getUpcoming());
            break;
        case "M1":
            cueList.gotoMarker1();
            display.setCue(cueList.getUpcoming());
            break;
        case "M2":
            cueList.gotoMarker2();
            display.setCue(cueList.getUpcoming());
            break;
        case "M3":
            cueList.gotoMarker3();
            display.setCue(cueList.getUpcoming());
            break;
        case "M4":
            cueList.gotoMarker4();
            display.setCue(cueList.getUpcoming());
            break;
        case ">":
            cueList.next();
            display.setCue(cueList.getUpcoming());
            break;
        }
        display.requestFocus();
    }
    
    /**
     * Hooks on the WINDOW_CLOSING event, re-enables
     * the button which launched the CueWindow
     * 
     * @param     e   The WindowEvent to process
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if(e.getID() == WindowEvent.WINDOW_CLOSING) {
            cueButton.setEnabled(true);
        }
    }

    /**
     * Processes key press events. does nothing.
     * 
     * @param     e  The KeyEvent to process
     */
    public void keyPressed(KeyEvent e) {
    }

    /**
     * Processes key typed events. Does the same
     * like actionPerformed, but for keyboard operation.
     * 
     * @param     e  The KeyEvent to process
     */
    public void keyTyped(KeyEvent e) {
        System.out.println("'"+e.getKeyChar()+"'");
        switch (e.getKeyChar()) {
        case ' ':
            Cue c = cueList.getUpcoming();
            display.setTopLine("executing ...");
            go.setEnabled(false);
            display.setEnabled(false);
            if (c != null) c.execute(this);
            break;
        case '<':
            cueList.prev();
            display.setCue(cueList.getUpcoming());
            break;
        case '1':
            cueList.gotoMarker1();
            display.setCue(cueList.getUpcoming());
            break;
        case '2':
            cueList.gotoMarker2();
            display.setCue(cueList.getUpcoming());
            break;
        case '3':
            cueList.gotoMarker3();
            display.setCue(cueList.getUpcoming());
            break;
        case '4':
            cueList.gotoMarker4();
            display.setCue(cueList.getUpcoming());
            break;
        case '>':
            cueList.next();
            display.setCue(cueList.getUpcoming());
            break;
        }
        display.requestFocus();
    }
    
    /**
     * Processes key release events. does nothing.
     * 
     * @param     e  The KeyEvent to process
     */
    public void keyReleased(KeyEvent e) {
    }

    /** The button which launched this window */
    private TButton cueButton;
    
    /** The GO button */
    private TButton go;
    
    /** The panel containing all components of this window */
    private TPanel panel;
    
    /** The display for ciúe title and description */
    private CueDisplay display;
    
    /** The CueList operated by this window */
    private CueList cueList;

}
