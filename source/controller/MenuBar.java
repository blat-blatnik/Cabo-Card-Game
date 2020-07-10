package controller;

import controller.buttons.*;
import model.CaboGame;
import view.Panel;

import javax.swing.*;

/**
 * @author Boris
 * @author Jana
 * @version 2.1
 *
 * This CaboMenu contains and organizes all of the button controls needed in a game of Cabo. This includes an
 * AbortSwapButton, a CaboButton, an InstructionsButton, a ResetButton, and a SoundButton.
 *
 * @see AbortSwapButton
 * @see CaboButton
 * @see InstructionsButton
 * @see ResetButton
 * @see SoundButton
 * @see Panel
 */
public class MenuBar extends JMenuBar {

    /**
     * Create a new CaboMenu with all the necessary buttons.
     *
     * @param game The CaboGame for which to create the button controls.
     * @see CaboGame
     */
    public MenuBar(CaboGame game) {
        add(new ResetButton(game));
        add(new CaboButton(game));
        add(new AbortSwapButton(game));

        //NOTE(Boris): add horizontal spacing so buttons before this point
        // are aligned on the left and buttons after are on the right.
        add(Box.createHorizontalGlue());

        add(new InstructionsButton());
        add(new SoundButton());
    }

}
