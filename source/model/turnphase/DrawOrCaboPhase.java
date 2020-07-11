package model.turnphase;

import model.CaboGame;
import model.Player;
import model.card.Deck;
import model.card.DiscardPile;
import model.card.Card;

/**
 * @version 3.0
 *
 * This represents the turn phase DrawOrCabo. In this turn phase the user can draw a new card either from the
 * deck or the top card from the discard pile.
 *
 * @see TurnPhase
 * @see CaboGame
 * @see Player
 * @see Card
 */
public class DrawOrCaboPhase extends TurnPhase {

    /**
     * Constructs a new DrawOrCaboPhase by taking the game that this phase exists on and setting a description of the
     * turn phase, as well as its phase name.
     * @param game the caboGame this turn phase exists in.
     */
    public DrawOrCaboPhase(CaboGame game) {
        super(game,"Draw a card, or call Cabo!");
        setPhaseName(PhaseName.DRAW_OR_CABO);
        game.setTurnPhase(this);
    }

    /**
     * Selects a card that can later be drawn by the player.
     * @param card the card that is to be selected.
     * @see Card
     */
    @Override
    public void selectCard(Card card) {
        super.selectCard(card);
    }

    /**
     * Draws a card from either the deck or the discard pile, depending on whether the selectedCard (the one that
     * the user is dragging in the controller) comes from deck or discard pile.
     * @see Card
     */
    @Override
    public void releaseCard() {
        if (getSelectedCard() == null)
            return;

        Deck deck = game.getDeck();
        DiscardPile discardPile = game.getDiscardPile();

        if (deck.top() == getSelectedCard()) {
            getSelectedCard().setFaceUp();
            game.setDrawnCard(deck.draw());
            clearSelectedCard();
            new DiscardOrSwapPhase(game);
        } else if (discardPile.top() == getSelectedCard()) {
            getSelectedCard().setFaceUp();
            game.setDrawnCard(discardPile.draw());
            clearSelectedCard();
            new DiscardOrSwapPhase(game);
        }

        setChanged();
        notifyObservers();

    }

}
