package controller;

import model.CaboGame;
import model.Player;
import model.card.Card;
import model.card.DraggedCard;
import view.BoardLayout;
import view.SoundPlayer;

import java.awt.*;

/**
 * @version 1.1
 *
 * The CaboBoard is a helper controller used by the CardDragger to determine where on the Cabo game board particular
 * points are located, or what card is under a point.
 *
 * @see CardDragger
 * @see CaboGame
 * @see BoardLayout
 */
public class Board {

    private final CaboGame game;
    private final BoardLayout layout;

    /**
     * Constructs a CaboBoard for a CaboGame from the given CaboBoardLayout.
     *
     * @param layout The CaboBoardLayout whose board this CaboBoard will represent.
     */
    public Board(BoardLayout layout) {
        this.game = layout.getGame();
        this.layout = layout;
    }

    /**
     * @param point Point at which to look for a Card.
     * @return The Card underneath the given point, or null if none is there.
     */
    public Card getCardAt(Point point) {

        // TODO(Boris): Maybe we need to loop in reverse order for player 2?
        // Check if selections make sense when cards are overlapping, especially
        // if clicking a card selects the card underneath it or something similar

        if (isInDeckArea(point))
            return game.getDeck().top();
        else if (isInDiscardArea(point))
            return game.getDiscardPile().top();
        else if (isInDrawArea(point))
            return game.getDrawnCard();

        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        Rectangle[] player1CardAreas = layout.getPlayerCardAreas(player1);
        Rectangle[] player2CardAreas = layout.getPlayerCardAreas(player2);

        for (int i = 0; i < player1CardAreas.length; ++i)
            if (player1CardAreas[i].contains(point))
                return player1.getCards().get(i);

        for (int i = 0; i < player2CardAreas.length; ++i)
            if (player2CardAreas[i].contains(point))
                return player2.getCards().get(i);

        return null;
    }

    /**
     * Sets the DraggedCard of the layout when a Card is being dragged.
     *
     * @param card The card to begin dragging.
     * @see DraggedCard
     */
    public void startDraggingCard(Card card) {
        layout.setDraggedCard(new DraggedCard(card));
    }

    /**
     * Stops dragging the DraggedCard if one was present - and plays a sound effect.
     *
     * @see DraggedCard
     */
    public void stopDraggingCard() {
        if (layout.getDraggedCard().getCard() != null) {
            SoundPlayer.playSound("deal.wav");
            layout.setDraggedCard(new DraggedCard(null));
        }
    }

    /**
     * @return Returns the DraggedCard of the given layout.
     */
    public DraggedCard getDraggedCard() {
        return layout.getDraggedCard();
    }

    /**
     * @param point The Point to test.
     * @return Whether the given Point is in the discard area of the CaboGame.
     */
    public boolean isInDiscardArea(Point point) {
        Rectangle extendedArea = layout.getDiscardArea();
        extendedArea.y      -= layout.getCardSpacing() * game.getDiscardPile().size();
        extendedArea.height += layout.getCardSpacing() * game.getDiscardPile().size();
        return extendedArea.contains(point);
    }

    /**
     * @param point The Point to test.
     * @return Whether the given Point is in the deck area of the CaboGame.
     */
    public boolean isInDeckArea(Point point) {
        Rectangle extendedArea = layout.getDeckArea();
        extendedArea.y      -= layout.getCardSpacing() * game.getDeck().size();
        extendedArea.height += layout.getCardSpacing() * game.getDeck().size();
        return extendedArea.contains(point);
    }

    /**
     * @param point The Point to test.
     * @return Whether the given Point is in the draw area of the CaboGame.
     */
    public boolean isInDrawArea(Point point) {
        return layout.getDrawArea().contains(point);
    }

    /**
     * @param player The Player whose area to test against the point.
     * @param point The Point to test against the player area.
     * @return Whether the area of a given player contains the given Point.
     */
    public boolean isInPlayerArea(Player player, Point point) {
        Rectangle area = layout.getPlayerArea(player);
        if (area == null)
            return false;
        return area.contains(point);
    }

    /**
     * @param player The Player whose area the card should be inserted into.
     * @param point The Point at which the card was to be inserted.
     * @return The index into the given Player's cards where the new card should be inserted based on the given Point.
     * @see Player
     */
    public int getPlayerCardInsertionIndex(Player player, Point point) {
        if (!isInPlayerArea(player, point))
            return -1;

        Rectangle[] playerCardAreas = layout.getPlayerCardAreas(player);
        if (playerCardAreas == null)
            return -1;

        return getCardInsertionIndex(playerCardAreas, point);
    }

    /**
     * @param cardAreas The Card areas in between which to get an insertion point from.
     * @param point The point at which the inserted Card should go.
     * @return The index into the given cardAreas in which a new Card should be inserted.
     */
    private static int getCardInsertionIndex(Rectangle[] cardAreas, Point point) {
        for (int i = 0; i < cardAreas.length; ++i) {
            Rectangle area = cardAreas[i];
            if (point.x < area.getCenterX())
                return i;
        }
        return cardAreas.length;
    }

}