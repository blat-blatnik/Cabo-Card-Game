package model;

import model.card.Card;
import model.card.Deck;
import model.card.DiscardPile;
import model.turnphase.*;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author Boris
 * @author Jana
 * @version 999.0
 *
 * Represents the state of a 1v1 game of Cabo. Including the players, deck, discard pile, and turn phases.
 *
 * @see Card
 * @see DiscardPile
 * @see Deck
 * @see Player
 * @see TurnPhase
 */
public class CaboGame extends Observable implements Observer, Serializable {

    private static final long serialVersionUID = 42L;

    private DiscardPile discardPile;
    private Deck deck;
    private Card drawnCard;
    private TurnPhase turnPhase;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    /**
     * Constructs a new CaboGame in the InitialPeekPhase.
     *
     * @see InitialPeekPhase
     */
    public CaboGame() {
        reset();
    }

    /**
     * Resets this CaboGame completely - as if it was constructed anew.
     */
    public void reset() {

        discardPile = new DiscardPile();
        deck = new Deck(discardPile);
        drawnCard = null;

        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        currentPlayer = player1;

        player1.addCard(deck.draw());
        player1.addCard(deck.draw());
        player1.addCard(deck.draw());
        player1.addCard(deck.draw());

        player2.addCard(deck.draw());
        player2.addCard(deck.draw());
        player2.addCard(deck.draw());
        player2.addCard(deck.draw());

        new InitialPeekPhase(this);
    }

    /**
     * Gives the turn to the next Player that should have one when the current Player's turn ends. This will set the
     * appropriate turn phase for the CaboGame, or call endGame() if the game should end.
     *
     * @see TurnPhase
     * @see Player
     */
    public void nextPlayersTurn() {

        Player nextPlayer = getOpponent();
        currentPlayer = nextPlayer;
        if (nextPlayer.hasCalledCabo())
            endGame();

        if (turnPhase instanceof InitialPeekPhase && !(player1.didInitialPeek() && player2.didInitialPeek()))
            new InitialPeekPhase(this);
        else
            new DrawOrCaboPhase(this);

        if (deck.isEmpty())
            deck.shuffleInDiscardPile(discardPile);

        setChanged();
        notifyObservers();
    }

    /**
     * @return Whether any of the Players have called Cabo.
     */
    public boolean caboWasCalled() {
        return (player1.hasCalledCabo() || player2.hasCalledCabo());
    }

    /**
     * If Cabo wasn't already called by some other Player, calls the current Player's setCalledCabo() method, and
     * gives the turn over to the next player.
     */
    public void callCabo() {
        if (!caboWasCalled()) {
            getCurrentPlayer().setCalledCabo();
            nextPlayersTurn();
        }
    }

    /**
     * @return This CaboGame's Deck of Cards.
     * @see Deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * @return This CaboGames's DiscardPile.
     * @see DiscardPile
     */
    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    /**
     * @return This CaboGame's Card from the draw area.
     * @see Card
     */
    public Card getDrawnCard() {
        return drawnCard;
    }

    /**
     * Sets this CaboGame's drawn Card to the given Card.
     *
     * @param card The new drawn Card.
     * @see Card
     */
    public void setDrawnCard(Card card) {
        if (drawnCard != card) {
            drawnCard = card;
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Clears any Cards in the draw area.
     *
     * @see Card
     */
    public void clearDrawnCard() {
        setDrawnCard(null);
    }

    /**
     * @return The TurnPhase this CaboGame is currently in.
     * @see TurnPhase
     */
    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    /**
     * Transitions the current TurnPhase of this CaboGame to the given TurnPhase.
     *
     * @param phase The TurnPhase to transition this CaboGame to.
     * @see TurnPhase
     */
    public void setTurnPhase(TurnPhase phase) {
        if (turnPhase != null)
            turnPhase.deleteObserver(this);

        turnPhase = phase;
        turnPhase.addObserver(this);

        setChanged();
        notifyObservers();
    }

    /**
     * @return The Player whose turn it is currently in this CaboGame.
     * @see Player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets all Cards face-down for a particular Player.
     *
     * @param player The Player whose Cards to put face-down.
     * @see Player
     */
    public void setCardsFaceDown(Player player) {
        List<Card> cards = player.getCards();
        for (Card card: cards){
            card.setFaceDown();
        }
        setChanged();
        notifyObservers();
    }

    /**
     * @return Get the opponent of the Player whose turn it currently is.
     * @see Player
     */
    public Player getOpponent() {
        if (currentPlayer == player1)
            return player2;
        else if (currentPlayer == player2)
            return player1;
        else
            return null;
    }

    /**
     * @return The first player of this CaboGame.
     * @see Player
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * @return The second Player of this CaboGame.
     * @see Player
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * The CaboGame observes the state of it's TurnPhases, so this method is called when the state of the current
     * TurnPhase changes so that the CaboGame can notify its own observes that something changed.
     *
     * @param observable The current TurnPhase whose state changed.
     * @param message The message being passed from the TurnPhase - nothing in this case.
     * @see TurnPhase
     */
    @Override
    public void update(Observable observable, Object message) {
        setChanged();
        notifyObservers();
    }

    /**
     * This is called when this CaboGame ends to display an message about which player won the game. This CaboGame is
     * then immediately reset.
     */
    private void endGame() {

        if (player1.getPoints() < player2.getPoints()) {
            JOptionPane.showMessageDialog(null,
                    String.format("Player 1: %d\nPlayer 2: %d\n", player1.getPoints(), player2.getPoints()),
                    "Player 1 Won!", JOptionPane.INFORMATION_MESSAGE);
        } else if (player2.getPoints() < player1.getPoints()) {
            JOptionPane.showMessageDialog(null,
                    String.format("Player 1: %d\nPlayer 2: %d\n", player1.getPoints(), player2.getPoints()),
                    "Player 2 Won!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    String.format("Player 1: %d\nPlayer 2: %d\n", player1.getPoints(), player2.getPoints()),
                    "Tie!", JOptionPane.INFORMATION_MESSAGE);
        }

        reset();
    }
}
