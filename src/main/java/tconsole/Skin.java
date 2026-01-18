package tconsole;

import java.io.InputStream;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;

/** Defines the colors, fonts and icons used in this application*/
public class Skin {
    public static Color BACKGROUND  = new Color(0x000000);
    public static Color PRESSED     = new Color(0x505050);
    public static Color OBJECT      = new Color(0x202020);
    public static Color LINE        = new Color(0x808080);
    public static Color MEDIA       = new Color(0x202030);
    public static Color DISABLED    = new Color(0x606060);
    public static Color HIGHLIGHT   = new Color(0xC0C0C0);
    public static Color GREEN       = new Color(0x00FF00);
    public static Color BLUE        = new Color(0x002080);
    public static Color RED         = new Color(0x600000);
    public static Font  DIALOG12    = new Font("Dialog", Font.PLAIN, 12);
    public static Font  DIALOG14    = new Font("Dialog", Font.PLAIN, 14);
    public static Font  BOLD14      = new Font("Dialog", Font.BOLD, 14);
    public static Font  MONO        = new Font("Monospaced", Font.PLAIN, 24);
    public static Font  SANS        = new Font("Dialog", Font.BOLD, 24);
    public static Font  SEG7        = readFont("fonts/seg7.ttf");
    public static ImageIcon SLIDER  = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icons/redcap.png"));
    public static ImageIcon DSLIDER = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icons/graycap.png"));
    public static ImageIcon FRWD    = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icons/frwd.png"));
    public static ImageIcon FFWD    = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icons/ffwd.png"));
    public static ImageIcon BACK    = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icons/back.png"));
    public static ImageIcon ATTENTION = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icons/attention.png"));
    public static ImageIcon QUESTION  = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("icons/question.png"));

    // reads a TTF font from the resources directory in the jar file
    private static Font readFont(String name) {
        try (InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(name)) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
            System.out.println(font);
            return font.deriveFont(32.0f);
        } catch (Exception e) {
            System.err.println("font "+name+" not found in jar file.");
            System.exit(-1);
        }
        return null;
    }

}

