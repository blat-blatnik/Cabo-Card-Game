package model.turnphase;

import model.CaboGame;
import model.Player;
import model.card.Card;

/**
 * @author Jana
 * @author Boris
 * @version 3.0
 *
 * This class represents the turn phase DiscardOrSwap. That means the player can either discard a drawn card
 * or swap the drawn card with one of the cards in their hand.
 *
 * @see TurnPhase
 */
public class DiscardOrSwapPhase extends TurnPhase {

    /**
     * Constructs an instance of the DiscardOrSwap turn phase. It takes the game as an argument and sets a description
     * of the turn phase and the phase name.
     * @param game the game that the turn phase happens in.
     */
    public DiscardOrSwapPhase(CaboGame game) {
        super(game,"Discard card or drag it over one of your own to swap them.");
        setPhaseName(PhaseName.DISCARD_OR_SWAP);
        game.setTurnPhase(this);
    }

    /**
     * Selects a card that can later be used to other be discarded or swapped.
     * @param card the card that is selected.
     */
    @Override
    public void selectCard(Card card) {
        super.selectCard(card);
    }

    /**
     * This method discards the drawn card (that is stored in selectedCard) onto the discardPile.
     * Every time after discarding a card from the Draw area, the card that is discarded needs to be checked for a
     * potential special function. This is stored in the 'discardAbility'. Depending on the discard ability, a new
     * turn phase will be instantiated or the next player will get their turn.
     */
    @Override
    public void releaseCard(){
        game.getDiscardPile().put(getSelectedCard());
        game.clearDrawnCard();

        switch (getSelectedCard().getDiscardAbility()) {
            case PEEK:
                clearSelectedCard();
                new PeekPhase(game);
                break;
            case SPY:
                clearSelectedCard();
                new SpyPhase(game);
                break;
            case SWAP:
                clearSelectedCard();
                new SwapPhase(game);
                break;
            case PEEK_SPY_AND_SWAP:
                clearSelectedCard();
                new SpyAndSwapPhase(game);
                break;
            default:
                game.nextPlayersTurn();
                break;
        }
    }

    /**
     * This enables swapping the drawn card with one of the player's cards on their hand.
     * @param cardAtReleasePoint card that can be discarded and will be exchanged with the drawn card.
     */
    @Override
    public void releaseCard(Card cardAtReleasePoint) {

        if (getSelectedCard() != game.getDrawnCard())
            return;

        Player player = game.getCurrentPlayer();

        int swapIndex = player.getCardIndex(cardAtReleasePoint);

        player.removeCard(swapIndex);
        player.addCard(swapIndex, getSelectedCard());
        getSelectedCard().setFaceDown();
        game.getDiscardPile().put(cardAtReleasePoint);
        game.clearDrawnCard();
        clearSelectedCard();
        game.nextPlayersTurn();
    }
}
