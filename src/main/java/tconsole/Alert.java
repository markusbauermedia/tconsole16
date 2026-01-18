
package tconsole;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;



/**
 * An Alert is a small window showing a message or question on the screen. The
 * use of a generic Dialog class in inconvenient here, because displayind the
 * message cannot be done by a single, simple method call.
 *
 * The class Alert exports a couble of static methods which allow to display
 * message boxes in a simple way. Before using these methods the first time,
 * the class mus be initialized with a call to the setParentFrame() method.
 *
 */
public class Alert extends JDialog implements ActionListener, KeyListener {

    /**
     * Constructs an Alert object. Note, that this constructor is for
     * extensions only, there is no public constructor for this class.
     *
     * @param     question    The text to be displayed in the box.
     * @param     mode        0 = simple OK box
     *                        1 = OK / Cancel box
     *                        2 = Yes / No box
     * @param     c           The component which issues the message.
     */
    protected Alert(String question, int mode, Component comp) {

        // create the dialog
        super(getFrame(comp), "TConsole16 Alert", true);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        result = false;
        
        // all components wil be placed in here
        TPanel p = new TPanel(null);

        // position for component placement
        int y = 21;
        int w = 200;
        
        // create and place the icon
        ImageIcon ic = (mode==0) ? Skin.ATTENTION : Skin.QUESTION;
        JLabel jl = new JLabel(ic);
        p.add(jl, new Rectangle(15, y, ic.getIconWidth(), ic.getIconHeight())); 
        
        // add the message text. may be multi-line, split the string
        // along newline characters
        FontMetrics fm = getFontMetrics(Skin.DIALOG14);
        StringTokenizer st = new StringTokenizer(question, "\n");
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            JLabel l = new JLabel(s);
            l.setFont(Skin.DIALOG14);
            int ww = fm.stringWidth(s);
            if (ww > w) w = ww;
            p.add(l, new Rectangle(75,y,ww+4,20));
            y += 20;
        }
        w += 95;
        y += 30;

        // create and place the buttons below the text
        TButton b1,b2;
        
        b1 = new TButton ("OK");
        b1.addActionListener(this);
        b1.addKeyListener(this);
        p.add(b1, new Rectangle(w-200,y,80,24));
        
        b2 = new TButton ("Cancel");
        b2.addActionListener(this);
        b2.addKeyListener(this);
        p.add(b2, new Rectangle(w-100,y,80,24));
      
        // modify the button labels accoring the alert box type
        if (mode==0) {
            b1.setVisible(false);
            b2.setText("OK");
        } else if (mode==2) {
            b1.setText("Yes");
            b2.setText("No");
        }
        y += 40;
        p.setSize(w,y);
        
        // add the panelö to the dialog window. 
        add(p);
        pack();
        if (p.getSize().height < 50) {
            setSize(w+6, y+20);
        }

        // place the alert boc centered above the parent frame
        Component parent = getParent();
        if (parent != null) {
            int vx = parent.getLocationOnScreen().x + (parent.getSize().width - w)/2;
            int vy = parent.getLocationOnScreen().y + (parent.getSize().height - y)/2;
            setLocation(new Point(vx,vy));
        }
        b1.requestFocus();
    }

    /**
     * Constructs an Alert object. Note, that this constructor is for
     * extensions only, there is no public constructor for this class.
     *
     * @param     question    The text to be displayed in the box.
     * @param     mode        0 = simple OK box
     *                        1 = OK / Cancel box
     */
    protected Alert(String question, int mode) {
        this(question, mode, parent);
    }
    
    /**
     * Shows an Alert box with the buttons "OK" and "Cancel". Blocks until the
     * user clicks one of the buttons or closes the window in the title bar.
     *
     * @return    True if the user clicked "OK", false otherwise.
     */
    public static boolean ok(String txt) {
        Alert alert = new Alert(txt, 1);
        alert.setVisible(true);
        return result;
    }

    /**
     * Shows an Alert box with the buttons "No" and "Yes". Blocks until the
     * user clicks one of the buttons or closes the window in the title bar.
     *
     * @return    True if the user clicked "Yes", false otherwise.
     */
    public static boolean yes(String txt) {
        Alert alert = new Alert(txt, 2);
        alert.setVisible(true);
        return result;
    }

    /**
     * Shows an Alert box with one "OK"  button. Does not block.
     *
     */
    public static void msgNoModal(String txt) {
        Alert alert = new Alert(txt, 0);
        alert.setModal(false);
        alert.setVisible(true);
    }

    /**
     * Shows an Alert box with one "OK"  button. Blocks until the
     * user clicks one of the buttons or closes the window in the title bar.
     *
     */
    public static void msg(String txt) {
        Alert alert = new Alert(txt, 0);
        alert.setVisible(true);
    }

    /**
     * Shows an Alert box with the buttons "OK" and "Cancel". Blocks until the
     * user clicks one of the buttons or closes the window in the title bar.
     *
     * @return    True if the user clicked "OK", false otherwise.
     */
    public static boolean ok(String txt, Component c) {
        Alert alert = new Alert(txt, 1, c);
        alert.setVisible(true);
        return result;
    }

    /**
     * Shows an Alert box with the buttons "No" and "Yes". Blocks until the
     * user clicks one of the buttons or closes the window in the title bar.
     *
     * @return    True if the user clicked "Yes", false otherwise.
     */
    public static boolean yes(String txt, Component c) {
        Alert alert = new Alert(txt, 2, c);
        alert.setVisible(true);
        return result;
    }

    /**
     * Shows an Alert box with one "OK"  button. Blocks until the
     * user clicks one of the buttons or closes the window in the title bar.
     *
     */
    public static void msg(String txt, Component c) {
        Alert alert = new Alert(txt, 0, c);
        alert.setVisible(true);
    }

    /**
     * Shows an Alert box with one "OK"  button. Does not block.
     */
    public static void msgNoModal(String txt, Component c) {
        Alert alert = new Alert(txt, 0, c);
        alert.setModal(false);
        alert.setVisible(true);
    }

    /**
     * Implements the ActionListener interface. The dialog gets closed
     * and disposed in any case
     *
     * @param     The action event, generated by one of the buttons
     */
    public void actionPerformed(ActionEvent e) {
        if ("OK".equals(e.getActionCommand())) result = true;
        if ("Yes".equals(e.getActionCommand())) result = true;
        setVisible(false);
        dispose();
    }

    /**
     * Processes key press events. 
     *
     * @param    ke    The KeyEvent received.
     */
    public void keyPressed(KeyEvent ke) {
    }


    /**
     * Processes key release events.
     *
     * @param    ke    The KeyEvent received.
     */
    public void keyReleased(KeyEvent ke) {
    }


    /**
     * Processes key type events.
     *
     * @param    ke    The KeyEvent received.
     */
    public void keyTyped(KeyEvent ke) {
        if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
            result = true;
            setVisible(false);
            dispose();
        } else if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
            setVisible(false);
            dispose();
        }
    }


    /**
     * sets the parent frame to be used for all Alert boxes
     *
     * @param  f   The common parent frame for all alart boxes.
     */
    public static void setParentFrame(Frame f) {
        parent = f;
    }

    /**
     * Delivers a reference to the frame, the Component c is part of.
     * 
     * @param     c     The component to test.
     * @return          The Frame, c is part of or null if the "getParent()"
     *                  link ends without a Frame in it. If c itself is a
     *                  Frame object, getFrame returns c.
     */
    protected static Frame getFrame(Component c) {
        for (;;) {
            if (c == null) return parent;
            if (c instanceof Frame) return (Frame) c;
            c = c.getParent();
        }
    }
    
    /**
     * Catches WindowEvents from this frame. A WINDOW_CLOSING
     * event causes the frame to be closed (destroyed).
     *
     * @param     e       The WindowEvent that occurred on this frame
     */
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            setVisible(false);
            dispose();
        } else {
             super.processWindowEvent(e);
        }
    }



    /** tranfers the users choice from actionPerformed() to doModal */
    private static boolean result;
    
    /** the parent frame needed for the dialog */
    private static Frame parent = null;

}
