package model.card;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;
import java.util.Iterator;

/**
 * @version 1.5
 *
 * Represents the discard pile of a CaboGame, in which all added cards are placed face-up.
 *
 * @see Card
 * @see Deck
 * @see model.CaboGame
 */
public class DiscardPile implements Iterable<Card>, Serializable {

    private static final long serialVersionUID = 42L;

    private final Stack<Card> pile;

    /**
     * Constructs an empty DiscardPile.
     */
    public DiscardPile() {
        pile = new Stack<>();
    }

    /**
     * Put a Card on top of the DiscardPile, the Card will be flipped face up.
     *
     * @param card The Card to add to the DiscardPile, which will be flipped face up. May be null in which case the method will return without doing anything.
     * @see Card
     */
    public void put(Card card) {
        if (card != null) {
            card.setFaceUp();
            pile.push(card);
        }
    }

    /**
     * Returns the Card on top of the DiscardPile, or null if none is present.
     */
    public Card top() {
        if (isEmpty())
            return null;
        return pile.peek();
    }

    /**
     * Returns an iterator which visits the cards in this discard pile
     * from BOTTOM to TOP, not from top to bottom.
     */
    @Override
    public Iterator<Card> iterator() {
        return pile.iterator();
    }

    /**
     * @return Returns the List of Cards backing this discard pile.
     */
    public List<Card> asList() {
        return pile;
    }

    /**
     * @return The number of Cards in this DiscardPile.
     */
    public int size() {
        return pile.size();
    }

    /**
     * @return Whether there are no Cards in this DiscardPile.
     */
    public boolean isEmpty() {
        return pile.isEmpty();
    }

    /**
     * Return the top card of the discard pile.
     *
     * @return The top-most card of DiscardPile, or null if none is present.
     */
    public Card draw() {
        if (isEmpty())
            return null;
        return pile.pop();
    }
}
