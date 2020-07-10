package view;

import controller.Board;
import model.CaboGame;
import model.Player;
import model.card.Card;
import model.card.DraggedCard;
import model.turnphase.InitialPeekPhase;

import java.awt.*;
import java.util.List;
import java.util.Observable;

/**
 * @author Boris
 * @version 2.0
 *
 * Stores the layout of the board for a CaboGame. This layout includes the player, deck, draw, and player areas - their
 * exact location and size on the screen, in pixels. It also includes the exact locations of all the players cards. This
 * view class observes the CaboGame and recalculates all of the areas when the game state changes.
 *
 * @see CaboGame
 * @see Panel
 * @see Board
 */
public class BoardLayout extends Observable {

    //NOTE(Boris): These dimensions are normalized - which means they should be in the range [0, 1]
    // where 0 is nothing, and 1 is the width/height of the whole panel. You have to multiply these values
    // by the width/height of the panel to get size in pixels.
    private static final double NORMALIZED_CARD_WIDTH   = 0.109;
    private static final double NORMALIZED_CARD_HEIGHT  = 0.15;
    private static final double NORMALIZED_CARD_SPACING = 0.001;
    private static final double NORMALIZED_FONT_SIZE    = 0.020;
    private static final double NORMALIZED_TEXT_OFFSET  = 0.040;

    private final CaboGame game;
    private Dimension dimensions;
    private Rectangle deckArea;
    private Rectangle discardArea;
    private Rectangle drawArea;
    private Rectangle player1Area;
    private Rectangle player2Area;
    private Rectangle[] player1CardAreas;
    private Rectangle[] player2CardAreas;
    private DraggedCard draggedCard;

    /**
     * Initialize the CaboBoardLayout for a particular CaboGame. The layout will observe the given game and keep track
     * of where all visual elements need to be.
     *
     * @param game The CaboGame whose layout to create and manage a layout for.
     * @see CaboGame
     */
    public BoardLayout(CaboGame game) {
        this.game = game;
        this.dimensions = new Dimension(1000, 1000);
        this.draggedCard = new DraggedCard(null);
        game.addObserver((observable, message) -> recomputeAreas());
        recomputeAreas();
    }

    /**
     * Computes and stores where all of the visual elements of the CaboGame need to be located based on the current
     * state of the game. This method is called whenever the CaboGame signals an update.
     *
     * @see Board
     * @see Panel
     */
    private void recomputeAreas() {

        List<Card> player1Cards = game.getPlayer1().getCards();
        List<Card> player2Cards = game.getPlayer2().getCards();

        deckArea = new Rectangle();
        discardArea = new Rectangle();
        drawArea = new Rectangle();
        player1Area = new Rectangle();
        player2Area = new Rectangle();
        player1CardAreas = new Rectangle[player1Cards.size()];
        player2CardAreas = new Rectangle[player2Cards.size()];

        double centerX = dimensions.width  / 2.0;
        double centerY = dimensions.height / 2.0;
        int playerAreaHorizontalPadding = (int)(0.2 * getWidth());

        deckArea.setBounds(
                (int)(centerX - 0.55 * getCardWidth()),
                (int)(centerY - 0.55 * getCardHeight()),
                (int)(1.1 * getCardWidth()),
                (int)(1.1 * getCardHeight()));
        drawArea.setBounds(
                (int)(centerX - 1.70 * getCardWidth()),
                (int)(centerY - 0.55 * getCardHeight()),
                (int)(1.1 * getCardWidth()),
                (int)(1.1 * getCardHeight()));
        discardArea.setBounds(
                (int)(centerX + 0.60 * getCardWidth()),
                (int)(centerY - 0.55 * getCardHeight()),
                (int)(1.1 * getCardWidth()),
                (int)(1.1 * getCardHeight()));
        player1Area.setBounds(
                playerAreaHorizontalPadding,
                getHeight() - 10 - (int)(1.2 * getCardHeight()),
                getWidth() - 2 * playerAreaHorizontalPadding,
                (int)(1.2 * getCardHeight()));
        player2Area.setBounds(
                playerAreaHorizontalPadding,
                10,
                getWidth() - 2 * playerAreaHorizontalPadding,
                (int)(1.2 * getCardHeight()));

        if (player1Cards.size() > 0) {
            double spacing = (player1Area.width - getCardWidth()) / (double)player1Cards.size();
            double baseX = player1Area.x + spacing / 2.0;
            double baseY = player1Area.getCenterY() - getCardHeight() / 2.0;
            for (int i = 0; i < player1Cards.size(); ++i) {
                int posX = (int)(baseX + i * spacing);
                int posY = (int)(baseY);
                player1CardAreas[i] = new Rectangle();
                player1CardAreas[i].setBounds(posX, posY, (int) getCardWidth(), (int) getCardHeight());
            }
        }

        if (player2Cards.size() > 0) {
            double spacing = (player2Area.width - getCardWidth()) / (double)player2Cards.size();
            double baseX = player2Area.x + spacing / 2.0;
            double baseY = player2Area.getCenterY() - getCardHeight() / 2.0;
            for (int i = 0; i < player2Cards.size(); ++i) {
                int posX = (int)(baseX + i * spacing);
                int posY = (int)(baseY);
                player2CardAreas[i] = new Rectangle();
                player2CardAreas[i].setBounds(posX, posY, (int) getCardWidth(), (int) getCardHeight());
            }
        }

        if (getDraggedCard().getCard() != null && game.getTurnPhase() instanceof InitialPeekPhase)
            setDraggedCard(new DraggedCard(null));

        setChanged();
        notifyObservers();
    }

    /**
     * @return The DraggedCard of this layout.
     * @see DraggedCard
     */
    public DraggedCard getDraggedCard() {
        return draggedCard;
    }

    /**
     * Sets the DraggedCard of this layout.
     * @param card The new DraggedCard for this layout.
     * @see DraggedCard
     */
    public void setDraggedCard(DraggedCard card) {
        this.draggedCard = card;
    }

    /**
     * @return The fractional size, in pixels of the spacing between cards in a pile such as the deck and the discard pile.
     */
    public double getCardSpacing() {
        return NORMALIZED_CARD_SPACING * getLimitingSize();
    }

    /**
     * @return The fractional size, in point of the font size for the current layout dimensions.
     */
    public double getFontSize() {
        return NORMALIZED_FONT_SIZE * getLimitingSize();
    }

    /**
     * @return The distance of the text labels from the discard pile, draw area, and deck, in pixels, for the current dimensions.
     */
    public double getTextOffset() {
        return NORMALIZED_TEXT_OFFSET * getLimitingSize();
    }

    /**
     * @param player The player whose area to get.
     * @return The area where the given player hand is located in pixels - or null if an invalid player was passed in.
     */
    public Rectangle getPlayerArea(Player player) {
        if (player.equals(game.getPlayer1()))
            return player1Area;
        else if (player.equals(game.getPlayer2()))
            return player2Area;
        else
            return null;
    }

    /**
     * @param player The player whose card areas to get.
     * @return An array of areas representing where each card of the given player should be placed on the screen - or null if invalid player was passed in.
     */
    public Rectangle[] getPlayerCardAreas(Player player) {
        if (player.equals(game.getPlayer1()))
            return player1CardAreas;
        else if (player.equals(game.getPlayer2()))
            return player2CardAreas;
        return null;
    }

    /**
     * @return The area where the deck of cards should be placed on the screen.
     */
    public Rectangle getDeckArea() {
        return new Rectangle(deckArea);
    }

    /**
     * @return The area where the discard pile should be placed on the screen.
     */
    public Rectangle getDiscardArea() {
        return new Rectangle(discardArea);
    }

    /**
     * @return The area where the draw area should be placed on the screen.
     */
    public Rectangle getDrawArea() {
        return new Rectangle(drawArea);
    }

    /**
     * @return The fractional width, in pixels, of cards drawn to the screen under the current layout dimensions.
     */
    public double getCardWidth() {
        return NORMALIZED_CARD_WIDTH * getLimitingSize();
    }

    /**
     * @return The fractional height, in pixels, of cards drawn to the screen under the current layout dimensions.
     */
    public double getCardHeight() {
        return NORMALIZED_CARD_HEIGHT * getLimitingSize();
    }

    /**
     * Sets the dimensions of this CaboBoardLayout. If the dimensions are different than the previous dimensions,
     * recomputeAreas() is called in order to scale all visual elements to the needed dimensions.
     *
     * @param width The new width of the board layout.
     * @param height The new height of the board layout.
     */
    public void setDimensions(int width, int height) {
        if (width != dimensions.width || height != dimensions.height) {
            dimensions = new Dimension(width, height);
            recomputeAreas();
        }
    }

    /**
     * @return The width of the current board layout.
     */
    public int getWidth() {
        return dimensions.width;
    }

    /**
     * @return The height of the current board layout.
     */
    public int getHeight() {
        return dimensions.height;
    }

    /**
     * @return The CaboGame represented by this board layout.
     */
    public CaboGame getGame() {
        return game;
    }

    /**
     * @return The smaller of the width and height of the dimensions of this CaboBoardLayout which is used to scale all NORMALIZED metrics to pixels.
     */
    private int getLimitingSize() {
        return Math.min(dimensions.width, dimensions.height);
    }

}
