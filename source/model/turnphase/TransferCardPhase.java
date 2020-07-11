package model.turnphase;

import model.CaboGame;
import model.Player;
import model.card.Card;

/**
 * @version 3.0
 *
 * This class lets the player transfer one of their cards to the opponent. This phase is invoked only when the player
 * has successfully dropped an opponent's card to the discard pile.
 * @see TurnPhase
 * @see CaboGame
 * @see Card
 */
public class TransferCardPhase extends TurnPhase {

    private final TurnPhase previousPhase;

    /**
     * Constructs a new turn phase with game, a description, a phase name, handling exceptional cases and the previous
     * turn phase to revert to afterwards.
     * @param game the game this turn phase exists on.
     * @param previousPhase the previous turn phase
     */
    public TransferCardPhase(CaboGame game, TurnPhase previousPhase) {
        super(game, "Good one! Give one of your cards to your opponent");
        this.previousPhase = previousPhase;
        setPhaseName(PhaseName.TRANSFER_CARD);
        if (!handleEdgeCases())
            game.setTurnPhase(this);
    }

    /**
     * Selects a card from the current player that can later be released into the opponent's player hand.
     * @param card the card that is to be selected.
     */
    @Override
    public void selectCard(Card card) {
        if (handleEdgeCases())
            return;

        if (game.getCurrentPlayer().hasCard(card))
            super.selectCard(card);

        setChanged();
        notifyObservers();
    }

    /**
     * Releases the selected card at a specific index and inserts the card at this index. Gives a card from the
     * player's hand to the opponent's hand.
     * @param insertionIndex the index at which a new card will be inserted.
     */
    @Override
    public void releaseCard(int insertionIndex) {

        if (getSelectedCard() == null || insertionIndex == -1 || handleEdgeCases())
            return;

        Player player = game.getCurrentPlayer();
        Player opponent = game.getOpponent();

        player.removeCard(getSelectedCard());
        opponent.addCard(insertionIndex, getSelectedCard());

        if (!(previousPhase instanceof SpyAndSwapPhase) && !(previousPhase instanceof SpyPhase))
            game.setTurnPhase(previousPhase);
        else
            game.nextPlayersTurn();

        setChanged();
        notifyObservers();
    }

    /**
     * Handles exceptional cases, for example when the player has no cards anymore to give to the opponent.
     * @return boolean indicating whether an exceptional case was found and dealt with.
     */
    private boolean handleEdgeCases() {
        Player player = game.getCurrentPlayer();
        if (player.getCards().isEmpty()) {
            game.setTurnPhase(previousPhase);
            return true;
        }
        return false;
    }
}
