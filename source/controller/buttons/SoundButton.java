package controller.buttons;

import view.SoundPlayer;

import javax.swing.*;

/**
 * @author Boris
 * @version 2.0
 *
 * This SoundButton controls whether the sound output from the SoundPlayer is turned on or off when the player clicks it.
 *
 * @see SoundPlayer
 */
public class SoundButton extends JButton {

    /**
     * Constructs a new SoundButton.
     */
    public SoundButton() {
        super();
        setProperties();

        addActionListener(e -> {
            SoundPlayer.setSoundIsOn(!SoundPlayer.soundIsOn());
            setProperties();
        });
    }

    /**
     * Sets the properties of this button - such as text and tooltips.
     */
    private void setProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
        setToolTipText("Toggle all sounds on or off");
        setMnemonic('S');
        if (SoundPlayer.soundIsOn())
            setText("Sound: on");
        else
            setText("Sound: off");
    }
}
