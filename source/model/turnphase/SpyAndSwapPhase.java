package model.turnphase;

import model.CaboGame;
import model.Player;
import model.card.Card;

/**
 * @author Jana
 * @author Boris
 * @version 3.0
 *
 * This turn phase lets a player look at two cards, one from their own hand - one from the opponents hand, and lets
 * them swap the cards if wanted.
 * @see TurnPhase
 * @see CaboGame
 * @see Card
 */
public class SpyAndSwapPhase extends TurnPhase {

    private Card peekedPlayerCard;
    private Card spiedOpponentCard;

    /**
     * Constructs a new turn phase with the game, description, peeked cards, a phase name and handling exceptional
     * cases.
     * @param game the game that the turn phase exists on.
     * @see CaboGame
     */
    public SpyAndSwapPhase(CaboGame game) {
        super(game, "Peek at own card, spy on opponent, then swap the cards!");
        peekedPlayerCard = null;
        spiedOpponentCard = null;
        setPhaseName(PhaseName.SPY_AND_SWAP);
        if (!handleEdgeCases())
            game.setTurnPhase(this);
    }

    /**
     * Lets the player look at two cards - one from themselves and one from the opponent.
     * @param card the card that is looked at.
     * @see Card
     */
    @Override
    public void selectCard(Card card) {
        if (handleEdgeCases()) return;

        super.selectCard(card);

        Player player = game.getCurrentPlayer();
        Player opponent = game.getOpponent();

        if (peekedPlayerCard == null && player.hasCard(card)) {
            peekedPlayerCard = card;
            card.setFaceUp();
        } else if (spiedOpponentCard == null && opponent.hasCard(card)) {
            spiedOpponentCard = card;
            card.setFaceUp();
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Lets the player swap the two selected cards.
     * @param cardAtReleasePoint card that can be released.
     * @see Card
     */
    @Override
    public void releaseCard(Card cardAtReleasePoint) {
        if (handleEdgeCases()) return;

        if (getSelectedCard() == null || cardAtReleasePoint == null)
            return;

        Player player = game.getCurrentPlayer();
        Player opponent = game.getOpponent();

        if ((getSelectedCard() == peekedPlayerCard  && cardAtReleasePoint == spiedOpponentCard) ||
            (getSelectedCard() == spiedOpponentCard && cardAtReleasePoint == peekedPlayerCard))
        {
            player.swapCards(opponent, peekedPlayerCard, spiedOpponentCard);
            peekedPlayerCard.setFaceDown();
            spiedOpponentCard.setFaceDown();
            game.nextPlayersTurn();
        }
    }

    /**
     * Checks for exceptional cases, for example when the players don't have enough cards to swap.
     * @return indicates whether an exceptional case was handled or not.
     */
    private boolean handleEdgeCases() {
        //NOTE(Boris): Edge-case where there aren't enough cards to swap - avoid soft-lock by skipping the turn phase.
        boolean playerHasAtLeast1Card = !game.getCurrentPlayer().getCards().isEmpty();
        boolean opponentHasAtLeast1Card = !game.getOpponent().getCards().isEmpty();
        if (!playerHasAtLeast1Card || !opponentHasAtLeast1Card) {
            game.nextPlayersTurn();
            return true;
        }
        return false;
    }
}