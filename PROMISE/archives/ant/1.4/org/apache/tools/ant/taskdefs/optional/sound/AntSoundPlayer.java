package org.apache.tools.ant.taskdefs.optional.sound;

import org.apache.tools.ant.*;

import javax.sound.sampled.*;

import java.io.*;
import java.util.*;


/**
 * This class is designed to be used by any AntTask that requires audio output.
 *
 * It implements the BuildListener interface to listen for BuildEvents and could
 * be easily extended to provide audio output upon any specific build events occuring.
 *
 * I have only tested this with .WAV and .AIFF sound file formats. Both seem to work fine.
 *
 * @author Nick Pellow
 * @version $Revision: 268577 $, $Date: 2001-02-04 18:51:01 +0800 (周日, 2001-02-04) $
 */

public class AntSoundPlayer implements LineListener, BuildListener {

    private File fileSuccess = null;
    private int loopsSuccess = 0;
    private Long durationSuccess = null;

    private File fileFail = null;
    private int loopsFail = 0;
    private Long durationFail = null;

    public AntSoundPlayer() {

    }

    /**
     * @param source the location of the audio file to be played when the build is successful
     * @param loops the number of times the file should be played when the build is successful
     * @param duration the number of milliseconds the file should be played when the build is successful
     */
    public void addBuildSuccessfulSound(File file, int loops, Long duration) {
        this.fileSuccess = file;
        this.loopsSuccess = loops;
        this.durationSuccess = duration;
    }


    /**
     * @param fileName the location of the audio file to be played when the build fails
     * @param loops the number of times the file should be played when the build is fails
     * @param duration the number of milliseconds the file should be played when the build fails
     */
    public void addBuildFailedSound(File fileFail, int loopsFail, Long durationFail) {
        this.fileFail = fileFail;
        this.loopsFail = loopsFail;
        this.durationFail = durationFail;
    }

    /**
     * Plays the file for duration milliseconds or loops.
     */
    private void play(Project project, File file, int loops, Long duration) {

        Clip audioClip = null;

        AudioInputStream audioInputStream = null;


		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
		}
		catch (UnsupportedAudioFileException uafe) {
			project.log("Audio format is not yet supported: "+uafe.getMessage());
		}
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

		if (audioInputStream != null) {
			AudioFormat	format = audioInputStream.getFormat();
			DataLine.Info	info = new DataLine.Info(Clip.class, format,
                                             AudioSystem.NOT_SPECIFIED);
			try {
				audioClip = (Clip) AudioSystem.getLine(info);
				audioClip.addLineListener(this);
                audioClip.open(audioInputStream);
			}
			catch (LineUnavailableException e) {
                project.log("The sound device is currently unavailable");
                return;
			}
			catch (IOException e) {
				e.printStackTrace();
			}

            if (duration != null) {
                playClip(audioClip, duration.longValue());
            } else {
                playClip(audioClip, loops);
            }
            audioClip.drain();
            audioClip.close();
		}
		else {
			project.log("Can't get data from file " + file.getName());
		}
    }

    private void playClip(Clip clip, int loops) {

        clip.loop(loops);
        while (clip.isRunning()) {
        }
    }

    private void playClip(Clip clip, long duration) {

        long currentTime = System.currentTimeMillis();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        try {
            Thread.sleep(duration);
        }
        catch (InterruptedException e) {
        }
    }

    /**
     * This is implemented to listen for any line events and closes the
     * clip if required.
     */
    public void update(LineEvent event) {
        if (event.getType().equals(LineEvent.Type.STOP)) {
            Line line = event.getLine();
            line.close();
		}
		else if (event.getType().equals(LineEvent.Type.CLOSE)) {
			/*
			 *	There is a bug in JavaSound 0.90 (jdk1.3beta).
			 *	It prevents correct termination of the VM.
			 *	So we have to exit ourselves.
			 */
		}
	}


    /**
     *  Fired before any targets are started.
     */
    public void buildStarted(BuildEvent event){
    }
    
    /**
     *  Fired after the last target has finished. This event
     *  will still be thrown if an error occured during the build.
     *
     *  @see BuildEvent#getException()
     */
    public void buildFinished(BuildEvent event){
        if (event.getException() == null && fileSuccess != null) {
            play(event.getProject(), fileSuccess, loopsSuccess, durationSuccess);
        } else if ( event.getException() != null && fileFail != null) {
            play(event.getProject(), fileFail, loopsFail, durationFail);
        }
    }

    /**
     *  Fired when a target is started.
     *
     *  @see BuildEvent#getTarget()
     */
    public void targetStarted(BuildEvent event){
    }

    /**
     *  Fired when a target has finished. This event will
     *  still be thrown if an error occured during the build.
     *
     *  @see BuildEvent#getException()
     */
    public void targetFinished(BuildEvent event){
    }

    /**
     *  Fired when a task is started.
     *
     *  @see BuildEvent#getTask()
     */
    public void taskStarted(BuildEvent event){
    }

    /**
     *  Fired when a task has finished. This event will still
     *  be throw if an error occured during the build.
     *
     *  @see BuildEvent#getException()
     */
    public void taskFinished(BuildEvent event){
    }

    /**
     *  Fired whenever a message is logged.
     *
     *  @see BuildEvent#getMessage()
     *  @see BuildEvent#getPriority()
     */
    public void messageLogged(BuildEvent event){
    }
}

