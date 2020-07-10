package controller.buttons;

import model.CaboGame;
import model.turnphase.SpyAndSwapPhase;
import model.turnphase.SwapPhase;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Jana
 * @version 1.0
 *
 * This AbortSwapButton control can be used if the player decides they don't want to swap a card during a SwapPhase or a
 * SpyAndSwapPhase.
 *
 * @see CaboGame
 * @see SwapPhase
 * @see SpyAndSwapPhase
 */
public class AbortSwapButton extends JButton implements Observer {

    private final CaboGame game;

    /**
     * Constructs an AbortSwapButton for a given CaboGame.
     *
     * @param game The CaboGame to add this controller to.
     */
    public AbortSwapButton(CaboGame game) {
        super();
        this.game = game;
        this.game.addObserver(this);
        setProperties();
        fixVisibility();
        addActionListener(e -> {
            game.nextPlayersTurn();
            game.setCardsFaceDown(game.getCurrentPlayer());
            game.setCardsFaceDown(game.getOpponent());
        });
    }

    /**
     * The AbortSwapButton observes CaboGame, so that it can update its visibility to the player - it is only visible
     * if it's associated action is allowed given the current state of the CaboGame - which is during a SwapPhase or a
     * SpyAndSwapPhase.
     *
     * @param o The Observable that changed, in this case the CaboGame being controlled.
     * @param arg The object that is updated, in this case the AbortSwapButton.
     */
    @Override
    public void update(Observable o, Object arg) {
        fixVisibility();
    }

    /**
     * Sets the properties of this button - such as text and tooltips.
     */
    private void setProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
        setToolTipText("Don't swap any cards");
        setText("Don't swap!");
    }

    /**
     * Changes the button's visibility based on the current state of the CaboGame.
     */
    private void fixVisibility() {
        setVisible(game.getTurnPhase() instanceof SpyAndSwapPhase || game.getTurnPhase() instanceof SwapPhase);
    }
}