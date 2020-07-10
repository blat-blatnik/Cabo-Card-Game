package model.card;

import view.SoundPlayer;

import java.io.Serializable;
import java.util.*;

/**
 * @author Boris
 * @version 3.0
 *
 * Represents a deck of cards used for a game of Cabo. All cards in the deck are normally set face down. The Deck
 * is also paired with a DiscardPile. When the deck runs out of cards the Cards from the DiscardPile can be shuffled in.
 *
 * @see Card
 * @see DiscardPile
 * @see model.CaboGame
 */
public class Deck implements Iterable<Card>, Serializable {

    private static final long serialVersionUID = 42L;

    private final List<Card> cards;

    /**
     * Initializes a Deck with a given DiscardPile pair. All possible CaboCards are added to the deck and then
     * shuffled. One of the Cards is then drawn from the Deck onto the DiscardPile.
     *
     * @param discardPile the discard pile
     * @see DiscardPile
     * @see Card
     * @see CaboCard
     */
    public Deck(DiscardPile discardPile) {
        this.cards = new ArrayList<>();

        for (CaboCard card : CaboCard.values())
            cards.add(new Card(card));

        shuffle();
        if (discardPile != null)
            discardPile.put(draw());
    }

    /**
     * @return An iterator over the Cards backing this Deck - the iterator goes from BOTTOM to TOP.
     */
    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    /**
     * @return The list of Cards that is backing this Deck.
     */
    public List<Card> asList() {
        return cards;
    }

    /**
     * @return The number of Cards present in this Deck.
     */
    public int size() {
        return cards.size();
    }

    /**
     * @return Whether there are no Cards left in this Deck.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * @return The Card which is on top of the Deck, or null if none is present.
     * @see Card
     */
    public Card top() {
        if (cards.isEmpty())
            return null;
        return cards.get(cards.size() - 1);
    }

    /**
     * Draws a Card from the deck, removing it from the deck in the process.
     *
     * @return The Card which is on top of the Deck, or null if none is present.
     * @see Card
     */
    public Card draw() {
        if (isEmpty())
            return null;
        return cards.remove(cards.size() - 1);
    }

    /**
     * Shuffles in all cards from the given DiscardPile - except for the one on top of the DiscardPile, which stays
     * face-up in the DiscardPile. All other cards are added to this Deck and set face down.
     *
     * @param discardPile The DiscardPile to shuffle in.
     */
    public void shuffleInDiscardPile(DiscardPile discardPile) {
        Card discardPileTop = discardPile.top();
        while (!discardPile.isEmpty())
            cards.add(discardPile.draw());
        if (!isEmpty()) {
            SoundPlayer.playSound("shuffle.wav");
            Collections.shuffle(cards);
            boolean soundIsOn = SoundPlayer.soundIsOn();
            SoundPlayer.setSoundIsOn(false);
            if (discardPileTop == null)
                discardPile.put(draw());
            else {
                cards.remove(discardPileTop);
                discardPile.put(discardPileTop);
            }
            for (Card card : cards)
                card.setFaceDown();
            SoundPlayer.setSoundIsOn(soundIsOn);
        }
    }

    /**
     * Shuffle the cards in this Deck.
     */
    private void shuffle() {
        Collections.shuffle(cards);
    }
}
