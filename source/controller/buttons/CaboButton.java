package controller.buttons;

import model.CaboGame;
import model.turnphase.DrawOrCaboPhase;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * @version 3.0
 *
 * This CaboButton control can be used by a player to call Cabo during their turn in the DrawOrCaboPhase.
 *
 * @see CaboGame
 * @see DrawOrCaboPhase
 */
public class CaboButton extends JButton implements Observer {

    private final CaboGame game;

    /**
     * Constructs a button that the player can press in order to call Cabo during their turn.
     *
     * @param game The CaboGame to add this control to.
     */
    public CaboButton(CaboGame game) {
        super();
        this.game = game;
        this.game.addObserver(this);
        setProperties();
        fixVisibility();
        addActionListener(e -> game.callCabo());
    }

    /**
     * The CaboButton observes CaboGame, so that it can update its visibility to the player - it is only visible
     * if it's associated action is allowed given the current state of the CaboGame - which is during the DrawOrCaboPhase.
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
        setToolTipText("Every other player will get 1 more turn and then the game will end.");
        setText("Cabo!");
    }

    /**
     * Changes the button's visibility based on the current state of the CaboGame.
     */
    private void fixVisibility() {
        setEnabled(game.getTurnPhase() instanceof DrawOrCaboPhase && !game.caboWasCalled());
    }
}
