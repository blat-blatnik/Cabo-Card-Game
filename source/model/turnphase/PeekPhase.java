package model.turnphase;

import model.CaboGame;
import model.Player;
import model.card.Card;

import java.util.List;

/**
 * @author Jana
 * @author Boris
 * @version 3.0
 *
 * This turn phase lets the player peek at one of their own cards, when a special card is put on the discard pile.
 *
 * @see TurnPhase
 * @see CaboGame
 * @see Card
 */
public class PeekPhase extends TurnPhase {

    private Card peekedCard;

    /**
     * Constructs a new PeekPhase by taking the game that this phase exists on and setting a description of the
     * turn phase, as well as its phase name. It also directly checks exceptional cases that the turn phase might need
     * to be skipped for.
     * @param game the caboGame this turn phase exists in.
     */
    public PeekPhase(CaboGame game) {
        super(game, "Peek at one of your cards");
        peekedCard = null;
        setPhaseName(PhaseName.PEEK);
        if (!handleEdgeCases())
            game.setTurnPhase(this);
    }

    /**
     * Lets the player peek at one of their own cards. When the card is face down, it is toggled to be face up, and
     * vice versa.
     * @param card the card that is looked at.
     */
    @Override
    public void selectCard(Card card) {
        List<Card> currentPlayerCards = game.getCurrentPlayer().getCards();

        if (!currentPlayerCards.contains(card) || handleEdgeCases())
            return;

        super.selectCard(card);

        if (peekedCard == null) {
            peekedCard = card;
            card.setFaceUp();
            setChanged();
            notifyObservers();
        } else if (peekedCard == card) {
            card.setFaceDown();
            game.nextPlayersTurn();
        }

    }

    /**
     * Checks for exceptional cases that would inhibit a proper functioning of the turn phase. In this case
     * the player could not peek at any card if their cards were empty, so the next player would directly be called.
     * @return a boolean indicating whether edge cases were handled or not.
     */
    private boolean handleEdgeCases() {
        Player player = game.getCurrentPlayer();
        if (player.getCards().isEmpty()) {
            game.nextPlayersTurn();
            return true;
        }
        return false;
    }
}
