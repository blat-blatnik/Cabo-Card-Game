package view;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Boris
 * @version 1.2
 *
 * The SoundPlayer plays sounds from the "sounds" resources folder. It also caches the sound clips in a map for faster
 * access. The sound output can be toggled on and off if needed.
 *
 * @see Clip
 */
public class SoundPlayer {

    private static final Map<String, Clip> soundClips = new HashMap<>();
    private static boolean soundIsOn = true;

    /**
     * Plays a sound from the given filename - if the sound is toggled on. The requested sound is looked up in an
     * internal cache to avoid loading sounds from disk every time a sound is played. If the cache does not contain the
     * sound, the sound is loaded from disk and then played. If the sound could not be loaded from disk for whatever
     * reason, that particular sound clip will be unavailable for the rest of the program run. If the requested sound
     * is already playing - then it is stopped and reset before being started again.
     *
     * @param filename The filename of the sound clip to play - must be located in the "sounds" resources directory.
     */
    public static void playSound(String filename) {
        if (!soundIsOn)
            return;

        if (!soundClips.containsKey(filename))
            soundClips.put(filename, loadSoundClip("sounds/" + filename));

        Clip soundClip = soundClips.get(filename);
        if (soundClip != null) {
            soundClip.stop();
            soundClip.setMicrosecondPosition(0);
            soundClip.start();
        }
    }

    /**
     * @return Whether the sound is currently on.
     */
    public static boolean soundIsOn() {
        return soundIsOn;
    }

    /**
     * Toggles the sound on/off depending on passed value. If the sound is turned off - all currently playing sound
     * clips are stopped and further calls to playSound() will do nothing until the sound is turned on again.
     *
     * @param value Whether to turn sounds on or off.
     */
    public static void setSoundIsOn(boolean value) {
        soundIsOn = value;
        if (!soundIsOn)
            stopAllSoundClips();
    }

    /**
     * Loads a sound clip from a given file and returns the clip if it loaded successfully.
     *
     * @param filename The name of the file from which to load the sound Clip from.
     * @return The loaded Clip if loading from disk was successful, or null if not.
     */
    private static Clip loadSoundClip(String filename) {
        try {
            URL soundFile = Thread.currentThread().getContextClassLoader().getResource(filename);
            if (soundFile == null)
                throw new IOException("Couldn't find " + filename);

            AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.printf("Failed to load sound (%s) - this sound will be disabled\n", e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Stops all active SoundClips from playing.
     */
    private static void stopAllSoundClips() {
        for (Clip clip : soundClips.values())
            clip.stop();
    }
}
