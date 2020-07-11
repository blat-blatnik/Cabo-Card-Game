package model.turnphase;

import model.CaboGame;
import model.Player;
import model.card.Deck;
import model.card.Card;

/**
 * @version 3.0
 *
 * This class is responsible for picking up a new card and placing it in the player's hand.
 * @see TurnPhase
 * @see CaboGame
 * @see Card
 */
public class PickupPhase extends TurnPhase {

    private final TurnPhase previousPhase;
    private Card selectedCard;

    /**
     * Constructs a new PickUp phase with the Cabo game and also knowing the previous turn phase to be able to
     * redirect the player to the previous turn phase after having finished this turn phase.
     * @param game the game this turn phase exists on.
     * @param previousPhase the turn phase that was stored before PickUp phase.
     */
    public PickupPhase(CaboGame game, TurnPhase previousPhase) {
        super(game, "That wasn't the right card. Take an extra card from the deck into your hand");
        this.selectedCard = null;
        this.previousPhase = previousPhase;
        setPhaseName(PhaseName.PICKUP);
        if (!handleEdgeCases())
            game.setTurnPhase(this);
    }

    /**
     * Selects the card that is later put into the player's hand.
     * @param card the card that is to be selected.
     */
    @Override
    public void selectCard(Card card) {
        if (handleEdgeCases())
            return;

        selectedCard = card;

        setChanged();
        notifyObservers();
    }

    /**
     * Inserts a new card (selected card) into the player's hand.
     * @param insertionIndex the index at which a new card will be inserted.
     */
    @Override
    public void releaseCard(int insertionIndex) {
        if (handleEdgeCases()) return;

        if (selectedCard == null || insertionIndex == -1)
            return;

        Player player = game.getCurrentPlayer();

        player.addCard(insertionIndex, game.getDeck().draw());
        game.setTurnPhase(previousPhase);

        setChanged();
        notifyObservers();
    }

    /**
     * Handles exceptional cases, such as when the deck is empty and cannot be reshuffled.
     * @return boolean indicating whether exceptional cases occurred or not.
     */
    private boolean handleEdgeCases() {
        Deck deck = game.getDeck();
        if (deck.isEmpty()) {
            deck.shuffleInDiscardPile(game.getDiscardPile());
            if (deck.isEmpty()) {
                game.setTurnPhase(previousPhase);
                return true;
            }
        }
        return false;
    }
}