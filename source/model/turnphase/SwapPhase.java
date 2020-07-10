package model.turnphase;

import model.CaboGame;
import model.Player;
import model.card.Card;

/**
 * @author Jana
 * @author Boris
 * @version 3.0
 *
 * This class is responsible for letting a player swap one of their cards with one of the opponent's cards. This
 * happens blindly, that means the player does not know the value of the swapped cards.
 * @see TurnPhase
 * @see CaboGame
 * @see Card
 */
public class SwapPhase extends TurnPhase {

    /**
     * Constructs a new turn phase with the game, a description, a phase name and handling exceptional cases.
     * @param game the game the turn phase exists on.
     */
    public SwapPhase(CaboGame game) {
        super(game, "Swap one of your cards for one of your opponents cards!");
        setPhaseName(PhaseName.SWAP);
        if (!handleEdgeCases())
            game.setTurnPhase(this);
    }

    /**
     * Selects the card that the player starts dragging.
     * @param card the card that is to be selected.
     */
    @Override
    public void selectCard(Card card) {
        if (handleEdgeCases())
            return;

        super.selectCard(card);

        Player player = game.getCurrentPlayer();
        Player opponent = game.getOpponent();
        if (player.hasCard(card) || opponent.hasCard(card))
            super.selectCard(card);

        setChanged();
        notifyObservers();
    }

    /**
     * Swaps the card that was selected with the card at the release point.
     * @param cardAtReleasePoint card that can be released.
     */
    @Override
    public void releaseCard(Card cardAtReleasePoint) {

        if (handleEdgeCases()) return;

        if (getSelectedCard() == null || cardAtReleasePoint == null)
            return;

        Player player = game.getCurrentPlayer();
        Player opponent = game.getOpponent();

        if (player.hasCard(getSelectedCard()) && opponent.hasCard(cardAtReleasePoint)) {
            player.swapCards(opponent, getSelectedCard(), cardAtReleasePoint);
            game.nextPlayersTurn();
        } else if (opponent.hasCard(getSelectedCard()) && player.hasCard(cardAtReleasePoint)) {
            opponent.swapCards(player, getSelectedCard(), cardAtReleasePoint);
            game.nextPlayersTurn();
        } else {
            clearSelectedCard();
        }
    }

    /**
     * Handles exceptional cases, for example when the players don't have enough cards to be swapped. Prevents the
     * player to be locked in this turn phase.
     * @return whether an exceptional case was handled or not
     */
    private boolean handleEdgeCases() {
        boolean playerHasAtLeast1Card = !game.getCurrentPlayer().getCards().isEmpty();
        boolean opponentHasAtLeast1Card = !game.getOpponent().getCards().isEmpty();
        if (!playerHasAtLeast1Card || !opponentHasAtLeast1Card) {
            game.nextPlayersTurn();
            return true;
        }
        return false;
    }
}
