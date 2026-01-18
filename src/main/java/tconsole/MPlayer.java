package tconsole;




/**
 * MPlayer provides a method for audio playback using the external
 * 'mplayer' program. This program is available for Linux, Mac (homebrew) 
 * and Windows. To make MPlayer work, mplayer must be installed and 
 * in the serach path.
 *
 * mplayer is operated in 'slave' / 'idle' mode, in this mode it does 
 * nothing until it receives commands from stdin. The static play(),
 * stop() and volume() methods of MPlayer send such commands to mplayer
 * to control it.
 *
 * A background thread consumes all output from mplayer, ensuring that
 * the external proces does not get stuck because of a full buffer/pipe
 *
 * The Config parameter 'audio.mplayerCLI' may be tweaked to modify the
 * behavior of mplayer, it is also possible to run mplayer on another
 * machine using ssh.
 * 
 */
public class MPlayer implements Runnable {
    
    /**
     * Constructs a MPlayer object, starts the background thread 
     * communicating with the mplayer process.
     * 
     * There is exactly one MPlayer instance in the application
     * it gets created by calling the static MPlayer.initialize()
     * method.
     */
    private MPlayer() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Creates and starts a MPlayer object. The static member variable
     * 'mplayer' holds a reference to this object.
     */
    public static void initialize() {
        mplayer = new MPlayer();
    }

    /**
     * Starts playing a song. If another song is already playing, 
     * it gets stopped.
     * 
     * @param     song  The song number, 1..99
     */
    public static void play(int song) {
        // the command to send to mplayer
        String cmd = "loadfile "+String.format("%02d", song)+".mp3";
        try {
            // write the command
            mplayer.process.outputWriter().write(cmd);
            mplayer.process.outputWriter().newLine();
            mplayer.process.outputWriter().flush();
            // also set the volume to the stored value, otherwise
            // mplayer would start play with 100% volume
            volume(mplayer.volume);
        } catch (Exception e) {
            // something went wrong
            System.out.println("mplayer error: "+cmd);
        }
    }
    
    /**
     * Stops playing
     */
    public static void stop() {
        // the command to send to mplayer
        String cmd = "stop";
        try {
            // write the command
            mplayer.process.outputWriter().write(cmd);
            mplayer.process.outputWriter().newLine();
            mplayer.process.outputWriter().flush();
        } catch (Exception e) {
            // something went wrong
            System.out.println("mplayer error: "+cmd);
        }
    }
    
    /**
     * Sets the playback volume
     * 
     * @param     percent   The volume, 0..100
     */
    public static void volume(int percent) {
        // the command to send to mplayer
        String cmd = "volume "+percent+" 1";
        try {
            // remember the volume. it must be set again
            // after a song has been started
            mplayer.volume = percent;
            // write the command
            mplayer.process.outputWriter().write(cmd);
            mplayer.process.outputWriter().newLine();
            mplayer.process.outputWriter().flush();
        } catch (Exception e) {
            // something went wrong
            System.out.println("mplayer error: "+cmd);
        }
    }

    /**
     * The background thread running the mplayer process and 
     * consuming all stdout output from the process
     */
    public void run() {
        // the outer loop starts the mplayer again if it died
        for(;;) {
            try {
                // start mplayer
                process = Runtime.getRuntime().exec(Config.mplayerCLI.split(" +"));
                // consume all output from mplayer, log it to the terminal
                for(;;) {
                    String line = process.inputReader().readLine();
                    if (line == null) break;
                    System.out.println(line);
                }
                // mplayer ended, nevertheless force process termination
                process.destroyForcibly();
                process = null;
            } catch (Exception e) {
                // something went wrong
                System.out.println("failed to start mplayer");
                // if there is a mplayer process, kill it.
                if (process != null) {
                    process.destroyForcibly();
                    process = null;
                }
            }
            // wait a second after mplayer terminated to avoid a race
            // condition if mplayer is not installed
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }

    /** The process unning mplayer */
    private Process process;

    /** The volume set with volume(). */
    private int volume = 100;

    /** The one and only instance of MPlayer, created with initialize() */
    private static MPlayer mplayer;

}
