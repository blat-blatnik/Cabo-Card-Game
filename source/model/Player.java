package model;

import model.card.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jana and Boris
 * @version 1.1
 *
 * This class encompasses one player in the game. It stores the player's cards and some more information about the
 * player, for example whether the player has called cabo or not.
 *
 * @see Card
 * @see Serializable
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 42L;

    private final String description;
    private final List<Card> cards;
    private boolean calledCabo;
    private boolean didInitialPeek;

    /**
     * Constructs a player with a description and the default settings for a new player, i.e. empty cards and false
     * booleans.
     * @param description the description of a player.
     */
    public Player(String description) {
        this.description = description;
        this.cards = new ArrayList<>();
        this.calledCabo = false;
        this.didInitialPeek = false;
    }

    /**
     * Swaps a card of the player with one card of another player.
     * @param opponent the opponent the player swaps cards with
     * @param playerCard the player card that will be swapped
     * @param opponentCard the opponent card that will be swapped for the player card
     */
    public void swapCards(Player opponent, Card playerCard, Card opponentCard) {

        int playerCardIndex = cards.indexOf(playerCard);
        int opponentCardIndex = opponent.cards.indexOf(opponentCard);

        if (playerCardIndex < 0 || opponentCardIndex < 0)
            throw new RuntimeException("Invalid cards passed to Player.swapCards()");

        removeCard(playerCard);
        opponent.removeCard(opponentCard);

        addCard(playerCardIndex, opponentCard);
        opponent.addCard(opponentCardIndex, playerCard);
    }

    /**
     * Gets the points a player has, depending on the value the cards have.
     * @return points to calculate who won
     */
    public int getPoints() {
        int points = 0;
        for (Card card : cards)
            points += card.getValue();
        return points;
    }

    /**
     * Adds a card to the player's hand.
     * @param card card that is added.
     * @see Card
     */
    public void addCard(Card card){
        cards.add(card);
    }

    /**
     * Adds a card to a specific index in the player's hand.
     * @param index index the card with be added at
     * @param card the card that should be added
     */
    public void addCard(int index, Card card){
        cards.add(index, card);
    }

    /**
     * Roves a card from the player's hand.
     * @param card the card that is removed
     */
    public void removeCard(Card card){
        cards.remove(card);
    }

    /**
     * Removes a card from the player's hand depending on its index.
     * @param index the index a card will be removed from
     */
    public void removeCard(int index) {
        cards.remove(index);
    }

    /**
     * Gets the index of a specific card.
     * @param card the card we want to know the index of
     * @return returns the index of that card
     */
    public int getCardIndex(Card card){
        return cards.indexOf(card);
    }

    /**
     * Gets the player's cards.
     * @return the player's cards
     */
    public List<Card> getCards(){
        return cards;
    }

    /**
     * Checks whether the player has a specific card in their hand.
     * @param card the card that is checked for
     * @return boolean indicating whether the player has the card or not
     */
    public boolean hasCard(Card card) {
        return cards.contains(card);
    }

    /**
     * Checks whether the player has called cabo.
     * @return true if cabo was called by the player, false if not
     */
    public boolean hasCalledCabo(){
        return calledCabo;
    }

    /**
     * Lets the player call cabo.
     */
    public void setCalledCabo(){
        calledCabo = true;
    }

    /**
     * Sets variable didInitialPeek to true.
     */
    public void setDidInitialPeek() {
        didInitialPeek = true;
    }

    /**
     * Checks whether the player already did their initial peek.
     * @return true if they have, false if not.
     */
    public boolean didInitialPeek() {
        return didInitialPeek;
    }

    /**
     * Overwrites the toString() method and gives a customized String representation of the player. In this case,
     * the player represents themselves with their description.
     * @return description of the player
     */
    @Override
    public String toString() {
        return description;
    }
}
