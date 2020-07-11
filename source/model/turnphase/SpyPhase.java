package model.turnphase;

import model.CaboGame;
import model.Player;
import model.card.Card;

/**
 * @version 3.0
 *
 * This class is responsible for letting a player spy on one of their opponent's cards.
 * @see TurnPhase
 * @see CaboGame
 * @see Card
 */
public class SpyPhase extends TurnPhase {

    private Card spiedCard;

    /**
     * Constructs a new turn phase with the game, a description, a phase name and checking for exceptional cases.
     * @param game the game that the turn phase exists on.
     */
    public SpyPhase(CaboGame game) {
        super(game, "Spy on one of your opponent's cards");
        spiedCard = null;
        setPhaseName(PhaseName.SPY);
        if (!handleEdgeCases())
            game.setTurnPhase(this);
    }

    /**
     * Lets the player spy on a card of the opponent.
     * @param card the card that is to be spied on.
     */
    @Override
    public void selectCard(Card card) {

        if (handleEdgeCases())
            return;

        super.selectCard(card);

        Player opponent = game.getOpponent();
        if (opponent.getCards().contains(card)) {
            if (spiedCard == null) {
                card.setFaceUp();
                spiedCard = card;
            } else if (spiedCard == card) {
                card.setFaceDown();
                spiedCard = null;
                game.nextPlayersTurn();
            }
        }
    }

    /**
     * Handles exceptional cases, for example when the opponent has no cards anymore, the player cannot spy on any of
     * the cards. It prevents the player to be stuck in this turn phase without being able to get out.
     * @return a boolean indicating whether exceptional cases were found and handled or not.
     */
    private boolean handleEdgeCases() {
        Player opponent = game.getOpponent();
        if (opponent.getCards().isEmpty()) {
            game.nextPlayersTurn();
            return true;
        }
        return false;
    }
}

