package controller.buttons;

import model.CaboGame;
import view.SoundPlayer;

import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;

/**
 * @author Boris
 * @version 1.5
 *
 * This ResetButton control starts a new game of Cabo by resetting the CaboGame when the player clicks on it.
 *
 * @see CaboGame
 */
public class ResetButton extends JButton {

    /**
     * Constructs ResetButton for a given CaboGame.
     *
     * @param game The CaboGame to add this control to.
     */
    public ResetButton(CaboGame game) {
        super();
        setButtonProperties();
        addActionListener(e -> {
            game.reset();
            SoundPlayer.playSound("shuffle.wav");
        });
    }

    /**
     * Sets the properties of this button - such as text and tooltips.
     */
    private void setButtonProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
        setMnemonic(KeyEvent.VK_R);
        setToolTipText("Start an entirely new game of Cabo - your progress in this game will be lost.");
        setText("New Game");
    }
}
