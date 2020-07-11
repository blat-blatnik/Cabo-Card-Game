package model.turnphase;

import model.CaboGame;
import model.Player;
import model.card.Card;

/**
  * @version 3.0
 *
 * This class is responsible for the Initial Peek of the game. In the beginning of the game the player can peek/look
 * at two of this own cards that they than need to remember throughout the game.
 *
 * @see TurnPhase
 * @see CaboGame
 * @see Card
 */
public class InitialPeekPhase extends TurnPhase {

    private Card peekedCard1;
    private Card peekedCard2;
    private boolean peekedBothCards;

    /**
     * Constructs the Initial Peek turn phase with the respective game, a description and two cards that they peeked at.
     * @param game the game this turn phase exists on.
     */
    public InitialPeekPhase(CaboGame game) {
        super(game,"Peek at 2 of your own cards");
        peekedCard1 = null;
        peekedCard2 = null;
        peekedBothCards = false;
        setPhaseName(PhaseName.INITIAL_PEEK);
        game.setTurnPhase(this);
    }

    /**
     * Lets the player peek at two cards. Selecting a card when face down toggles it to face up and the other way around.
     * Only two cards per player can be peeked at.
     * @param card the card that is be peeked/selected.
     */
    @Override
    public void selectCard(Card card) {

        Player currentPlayer = game.getCurrentPlayer();

        if (peekedBothCards) {

            if (card == peekedCard1)
                card.setFaceDown();
            if (card == peekedCard2)
                card.setFaceDown();

            if (!peekedCard1.isFaceUp() && !peekedCard2.isFaceUp()) {
                game.getCurrentPlayer().setDidInitialPeek();
                game.nextPlayersTurn();
            }

        } else if (currentPlayer.getCards().contains(card)) {

            if (peekedCard1 == null) {
                peekedCard1 = card;
                card.setFaceUp();
            } else if (peekedCard2 == null && card != peekedCard1) {
                peekedCard2 = card;
                card.setFaceUp();
                peekedBothCards = true;
            }
        }

        setChanged();
        notifyObservers();
    }

}
