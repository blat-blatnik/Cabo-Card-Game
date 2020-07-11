package model.turnphase;

import model.card.DiscardAbility;
import model.CaboGame;
import model.card.DiscardPile;
import model.Player;
import model.card.Card;

import java.io.Serializable;
import java.util.Observable;

/**
 * @version 3.0
 *
 * This is an abstract superclass for all TurnPhases in the Cabo game. It is observed by the CaboGame itself and when
 * changed, it notifies CaboGame which then notifies the view. Most methods are overloaded by the subclasses of
 * TurnPhase to fit their particular purpose except for handleCardDrop.
 *
 * @see Serializable
 * @see Cloneable
 * @see Observable
 * @see CaboGame
 * @see Card
 */
public abstract class TurnPhase extends Observable implements Serializable, Cloneable {

    private static final long serialVersionUID = 42L;

    /**
     * Describes the specific TurnPhases for easy comparisons. Each TurnPhases has a phaseName.
     */
    public enum PhaseName {
        INITIAL_PEEK,
        DRAW_OR_CABO,
        DISCARD_OR_SWAP,
        PEEK,
        SPY,
        SPY_AND_SWAP,
        SWAP,
        TRANSFER_CARD,
        PICKUP,
    }

    private final String description;
    protected final CaboGame game;
    private Card selectedCard;
    private PhaseName phaseName;

    /**
     * Constructing a TurnPhases with its game object it exists on and a description of the TurnPhases.
     * @param game game object of the Cabo game.
     * @param description description that can be displayed to help the user understand the TurnPhases.
     */
    public TurnPhase(CaboGame game, String description) {
        this.description = description;
        this.game = game;
    }

    /**
     * Selecting a specific card and setting it to the selectedCard field.
     * @param card the card that is to be selected.
     * @see Card
     */
    public void selectCard(Card card) {
        this.selectedCard = card;

        setChanged();
        notifyObservers();
    }

    /**
     * Releasing a card. This is overwritten by the specific subclasses that need this method.
     */
    public void releaseCard(){}

    /**
     * Enables the user to do something with the card.
     * @param cardAtReleasePoint card that can be released.
     */
    public void releaseCard(Card cardAtReleasePoint){}

    /**
     * Enables the model to insert a card at the specific index. Subclasses override this method depending on the
     * implementation.
     * @param insertionIndex the index at which a new card will be inserted.
     */
    public void releaseCard(int insertionIndex){}


    /**
     * During any part of the game (except for InitialPeek, PickUp and TransferCard), the user can drag one of his or
     * one of the opponent's cards to the discard area. Only when the card that they dragged is the same as the top of
     * the discard pile, it will succeed. Otherwise, the player must take one extra card. If the player manages to put
     * a card of their opponent onto the discard pile and they succeed (meaning the card is the same as the top of the
     * discard pile), they are able to give one of their cards to the opponent.
     *
     * @return boolean indicating whether anything happened or not. Returns true if there was a change, and false if
     * nothing happened.
     */
    public boolean handleCardDrop() {
        if (selectedCard == null)
            return false;

        Player player = game.getCurrentPlayer();
        Player opponent = game.getOpponent();

        boolean playerHasCard = player.hasCard(selectedCard);
        boolean opponentHasCard = opponent.hasCard(selectedCard);

        if (!playerHasCard && !opponentHasCard) return false;

        DiscardPile discardPile = game.getDiscardPile();

        int discardValue = discardPile.top().getValue();
        DiscardAbility discardAbility = discardPile.top().getDiscardAbility();
        int selectedValue = selectedCard.getValue();
        DiscardAbility selectedDiscardAbility = selectedCard.getDiscardAbility();

        if (discardValue != selectedValue || discardAbility != selectedDiscardAbility) {
            game.setTurnPhase(new PickupPhase(game, this));
        } else {
            if (playerHasCard) {
                player.removeCard(selectedCard);
                discardPile.put(selectedCard);
                if (this instanceof PeekPhase || this instanceof SpyAndSwapPhase)
                    game.nextPlayersTurn();
                setChanged();
                notifyObservers();
            } else {
                opponent.removeCard(selectedCard);
                discardPile.put(selectedCard);
                game.setTurnPhase(new TransferCardPhase(game, this));
            }
        }
        return true;

    }

    /**
     * Gets the description of the TurnPhases.
     * @return description of the TurnPhases.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the selected card of the player.
     * @return selected Card
     * @see Card
     */
    protected Card getSelectedCard(){
        return selectedCard;
    }

    /**
     * Clears the selected field, i.e. sets it to null.
     */
    protected void clearSelectedCard(){
        selectedCard = null;
    }

    /**
     * Makes it possible to clone the TurnPhases. This is needed in serialization, so that the TurnPhases class
     * can be properly stored and loaded.
     * @return the object that was cloned, null if an exception was raised.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Gets the PhaseName of this TurnPhase.
     * @return name used to easily identify TurnPhases.
     */
    public PhaseName getPhaseName() {
        return phaseName;
    }

    /**
     * Sets the PhaseName to a new value. Can be called from subclasses.
     * @param phaseName new value of PhaseName.
     */
    protected void setPhaseName(PhaseName phaseName){
        this.phaseName = phaseName;
    }
}
