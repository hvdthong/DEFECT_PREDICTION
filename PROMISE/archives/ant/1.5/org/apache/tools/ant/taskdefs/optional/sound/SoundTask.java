package org.apache.tools.ant.taskdefs.optional.sound;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.util.Random;
import java.util.Vector;

/**
 * Plays a sound file at the end of the build, according to whether the build failed or succeeded.
 *
 * There are three attributes to be set:
 *
 * <code>source</code>: the location of the audio file to be played
 * <code>duration</code>: play the sound file continuously until "duration" milliseconds has expired
 * <code>loops</code>: the number of times the sound file should be played until stopped
 *
 * I have only tested this with .WAV and .AIFF sound file formats. Both seem
 * to work fine.
 *
 * plans for the future:
 * - use the midi api to define sounds (or drum beat etc) in xml and have
 *   Ant play them back
 *
 * @author Nick Pellow
 * @version $Revision: 274041 $, $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 */

public class SoundTask extends Task {

    private BuildAlert success = null;
    private BuildAlert fail = null;

    /**
     * add a sound when the build succeeds
     */
    public BuildAlert createSuccess() {
        success = new BuildAlert();
        return success;
    }

    /**
     * add a sound when the build fails
     */
    public BuildAlert createFail() {
        fail = new BuildAlert();
        return fail;
     }

    public SoundTask() {
    }

    public void init(){
    }

    public void execute() {

        AntSoundPlayer soundPlayer = new AntSoundPlayer();

        if (success == null) {
            log("No nested success element found.", Project.MSG_WARN);
        } else {
            soundPlayer.addBuildSuccessfulSound(success.getSource(),
              success.getLoops(), success.getDuration());
        }

        if (fail == null) {
            log("No nested failure element found.", Project.MSG_WARN);
        } else {
            soundPlayer.addBuildFailedSound(fail.getSource(),
              fail.getLoops(), fail.getDuration());
        }

        getProject().addBuildListener(soundPlayer);

    }

    /**
     * A class to be extended by any BuildAlert's that require the output
     * of sound.
     */
    public class BuildAlert {
        private File source = null;
        private int loops = 0;
        private Long duration = null;

        /**
         * Sets the duration in milliseconds the file should be played; optional.
         */
        public void setDuration(Long duration) {
            this.duration = duration;
        }

        /**
         * Sets the location of the file to get the audio; required.
         *
         * @param source the name of a sound-file directory or of the audio file
         */
        public void setSource(File source) {
            this.source = source;
        }

        /**
         * Sets the number of times the source file should be played; optional.
         *
         * @param loops the number of loops to play the source file
         */
        public void setLoops(int loops) {
            this.loops = loops;
        }

        /**
         * Gets the location of the file to get the audio.
         */
        public File getSource() {
            File nofile = null ;
            if (source.exists()) {
                if (source.isDirectory()) {
                    String[] entries = source.list() ;
                    Vector files = new Vector() ;
                    for (int i = 0 ; i < entries.length ; i++) {
                        File f = new File(source, entries[i]) ;
                        if (f.isFile()) {
                            files.addElement(f) ;
                        }
                    }
                    if (files.size() < 1) {
                        throw new BuildException("No files found in directory " + source);
                    }
                    int numfiles = files.size() ;
                    Random rn = new Random() ;
                    int x = rn.nextInt(numfiles) ;
                    this.source = (File) files.elementAt(x);
                }
            } else {
                log(source + ": invalid path.", Project.MSG_WARN) ;
                this.source = nofile ;
            }
            return this.source ;
        }

        /**
         * Sets the number of times the source file should be played.
         *
         * @return the number of loops to play the source file
         */
        public int getLoops() {
            return this.loops;
        }

        /**
         * Gets the duration in milliseconds the file should be played.
         */
        public Long getDuration() {
            return this.duration;
        }
    }
}

