package model.card;

import view.SoundPlayer;

import java.io.Serializable;

/**
 * @author Boris
 * @version 2.0
 *
 * Represents a Card used in a game of Cabo. Encapsulates both the card value, as well as whether the Card is currently
 * face-up and thus exposed to the players, or not.
 *
 * @see CaboCard
 * @see Deck
 * @see DiscardPile
 */
public class Card implements Serializable {

    private static final long serialVersionUID = 42L;

    private final CaboCard card;
    private boolean isFaceUp;

    /**
     * Constructs a new Card from a CaboCard representing it's value. The created Card is initially set face-down.
     *
     * @param card The CaboCard to base this Card off of.
     * @see CaboCard
     */
    public Card(CaboCard card) {
        this.card = card;
        this.isFaceUp = false;
    }

    /**
     * @return The CaboCard whose value this Card is based on.
     */
    public CaboCard getBackingCard() {
        return card;
    }

    /**
     * @return The value of this Card in the game of Cabo.
     */
    public int getValue() {
        return card.value;
    }

    /**
     * @return The special discard ability of this Card which is activated when the card is discarded.
     */
    public DiscardAbility getDiscardAbility() {
        return card.discardAbility;
    }

    /**
     * @return Whether the card is currently set face-up or not.
     */
    public boolean isFaceUp() {
        return this.isFaceUp;
    }

    /**
     * Sets the card face up if it wasn't already so.
     */
    public void setFaceUp() {
        if (!isFaceUp())
            SoundPlayer.playSound("flip.wav");
        isFaceUp = true;
    }

    /**
     * Sets the card face down if it wasn't already so.
     */
    public void setFaceDown() {
        if (isFaceUp())
            SoundPlayer.playSound("flip.wav");
        isFaceUp = false;
    }

}
