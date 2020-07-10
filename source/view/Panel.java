package view;

import model.card.DiscardPile;
import model.Player;
import model.card.Card;

import model.CaboGame;
import model.card.DraggedCard;
import model.card.Deck;
import model.turnphase.InitialPeekPhase;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;

/**
 * @author Boris
 * @version 5.0
 *
 * This panel is used to display the state of the CaboGame to the user. The panel observes the CaboBoardLayout for
 * a particular CaboGame, and redraws itself when the layout changes.
 *
 * @see CaboGame
 * @see BoardLayout
 * @see CardTextures
 */
public class Panel extends JPanel {

    private static final Color BACKGROUND_COLOR = new Color(47, 48, 65);
    private static final Color CARD_AREA_COLOR = new Color(54, 55, 77);
    private static final Color TEXT_COLOR = new Color(255, 243, 215);
    private static final Color LABEL_COLOR = new Color(116, 113, 114);

    private final BoardLayout layout;

    /**
     * Initializes a CaboPanel with a CaboBoardLayout that it will observe, and use to tell where to paint particular
     * aspects of the CaboGame.
     *
     * @param layout The CaboBoardLayout to use to paint this panel with.
     * @see BoardLayout
     */
    public Panel(BoardLayout layout) {

        setBackground(BACKGROUND_COLOR);
        setVisible(true);
        setOpaque(true);

        this.layout = layout;
        this.layout.addObserver((observable, message) -> repaint());
    }

    /**
     * Repaints the surface of this CaboPanel with up-to-date information from the CaboBoardLayout. This will paint the
     * game state indicators, discard pile, deck, draw area, player area, as well as the movable card.
     *
     * @param graphics The graphics object used to paint this panel.
     * @see BoardLayout
     * @see CaboGame
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (layout.getWidth() != getWidth() || layout.getHeight() != getHeight()) {
            layout.setDimensions(getWidth(), getHeight());
        }

        CaboGame game = layout.getGame();
        Graphics2D g = (Graphics2D)graphics;

        setupPainting(g);
        paintGameStateIndicator(g);
        paintDiscardPile(g);
        paintDeck(g);
        paintDrawArea(g);
        paintPlayerArea(g, game.getPlayer1());
        paintPlayerArea(g, game.getPlayer2());

        // The movable card is painted if it exists. It is painted after everything else, so it always appears on top.
        paintDraggedCard(g);
    }

    /**
     * Paint a String that is centered on the given location.
     *
     * @param g The Graphics object used to paint the String.
     * @param string The String to paint.
     * @param x The x position on which the String will be centered.
     * @param y The y position on which the String will be centered.
     */
    private void paintCenteredString(Graphics2D g, String string, double x, double y) {
        FontMetrics metrics = g.getFontMetrics();
        Rectangle2D textBounds = metrics.getStringBounds(string, g);
        int startX = (int)Math.round(x - textBounds.getWidth() / 2);
        int startY = (int)Math.round(y - textBounds.getHeight() / 2);
        g.drawString(string, startX, startY);
    }

    /**
     * Paints a given card immediately using an appropriate texture, even if it's a DraggedCard.
     *
     * @param g The Graphics object used to paint the Card.
     * @param x The position of left-most pixel where to paint the Card.
     * @param y The position of top-most pixel where to paint the Card.
     * @param card The Card to paint.
     * @see Card
     * @see CardTextures
     */
    private void paintCardImmediately(Graphics2D g, int x, int y, Card card) {
        if (card == null)
            return;

        int cardW = (int)Math.round(layout.getCardWidth());
        int cardH = (int)Math.round(layout.getCardHeight());

        Image cardImage;
        if (card.isFaceUp())
            cardImage = CardTextures.getCardFrontTexture(card, cardW, cardH);
        else
            cardImage = CardTextures.getCardBackTexture(cardW, cardH);

        g.drawImage(cardImage, x, y, cardW, cardH, this);
    }

    /**
     * Paints a given card at a particular location. If the given card is a DraggedCard, then the card is not painted
     * but rather its tether is set to this position so that it can be later be painted correctly by paintCardImmediately.
     *
     * @param g The Graphics object used to paint the Card.
     * @param x The position of left-most pixel where to paint the Card.
     * @param y The position of top-most pixel where to paint the Card.
     * @param card The Card to paint.
     * @see Card
     * @see DraggedCard
     */
    private void paintCard(Graphics2D g, int x, int y, Card card) {
        if (card == null)
            return;

        DraggedCard draggedCard = layout.getDraggedCard();

        if (card == draggedCard.getCard())
            draggedCard.setTether(x, y);
        else
            paintCardImmediately(g, x, y, card);
    }

    /**
     * Paint a given list of cards in an efficient and nice looking manner.
     *
     * @param g The Graphics object used to paint the Cards.
     * @param x The position of the left-most pixel in the card pile's area.
     * @param y The position of the top-most pixel in the card pile's area.
     * @param cards The pile of Cards which to paint.
     * @see Card
     */
    private void paintCardPile(Graphics2D g, double x, double y, java.util.List<Card> cards) {

        if (cards.isEmpty())
            return;

        int cardX = (int)Math.round(x);
        int firstY = (int)Math.round(y + layout.getCardHeight());
        int lastY = (int)Math.round(y + layout.getCardHeight() - layout.getCardSpacing() * (cards.size() - 2));

        //NOTE(Boris): Drawing the first few cards only as lines as they will covered up by the other
        // cards anyway - its wasteful to draw all of them full as there would be a lot of overdraw.
        // It also actually ends up looking worse because of aliasing, because the card spacing is not
        // an integer.

        boolean useLightGray = false;
        for (int scanLine = firstY; scanLine >= lastY; --scanLine) {
            if (useLightGray)
                g.setColor(new Color(104, 65, 25));
            else
                g.setColor(new Color(50, 50, 50));
            useLightGray = !useLightGray;
            g.drawLine(cardX, scanLine, cardX + (int)(layout.getCardWidth() - 0.5), scanLine);
        }

        //NOTE(Boris): The last *2* cards are drawn out fully. The first will obviously always be visible
        // but the second one will also be visible if the player exposes it by dragging the top card.

        if (cards.size() >= 2) {
            int index = cards.size() - 2;
            Card card = cards.get(index);
            int cardY = (int)Math.round(y - layout.getCardSpacing() * index);
            paintCard(g, cardX, cardY, card);
        }

        int index = cards.size() - 1;
        Card card = cards.get(index);
        int cardY = (int)Math.round(y - layout.getCardSpacing() * index);
        paintCard(g, cardX, cardY, card);
    }

    /**
     * Sets up the panel for painting by setting rendering hints and the Font.
     *
     * @param g Graphics Object to set up for painting.
     */
    private void setupPainting(Graphics2D g) {
        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int fontSize = (int)layout.getFontSize();
        g.setFont(new Font(Font.SERIF, Font.PLAIN, fontSize));
    }

    /**
     * Paints the CaboGame's state indicator, e.g. indicating which Player's turn it is or whether Cabo was called.
     *
     * @param g Graphics object used to paint the state indicator.
     */
    private void paintGameStateIndicator(Graphics2D g) {
        CaboGame game = layout.getGame();
        g.setColor(TEXT_COLOR);
        g.drawString(game.getCurrentPlayer().toString(), 10, 20);
        if (game.caboWasCalled()) {
            g.setColor(new Color(255, 92, 43));
            g.drawString("Cabo!", 10, 40);
        }
    }

    /**
     * Paints the DiscardPile of the CaboGame.
     *
     * @param g Graphics object used to paint the DiscardPile.
     * @see DiscardPile
     */
    private void paintDiscardPile(Graphics2D g) {
        Rectangle discardArea = layout.getDiscardArea();

        g.setColor(CARD_AREA_COLOR);
        g.fillRoundRect(discardArea.x, discardArea.y, discardArea.width, discardArea.height, 15, 15);

        g.setColor(LABEL_COLOR);
        double textOffset = layout.getTextOffset();
        paintCenteredString(g, "Discard", discardArea.getCenterX(), discardArea.y + discardArea.height + textOffset);

        double cardWidth = layout.getCardWidth();
        double cardHeight = layout.getCardHeight();
        double baseX = discardArea.getCenterX() - cardWidth / 2;
        double baseY = discardArea.getCenterY() - cardHeight / 2;
        DiscardPile discardPile = layout.getGame().getDiscardPile();
        paintCardPile(g, baseX, baseY, discardPile.asList());
    }

    /**
     * Paints the Deck of the CaboGame.
     *
     * @param g Graphics object used to paint the deck.
     * @see Deck
     */
    private void paintDeck(Graphics2D g) {
        Rectangle deckArea = layout.getDeckArea();

        g.setColor(CARD_AREA_COLOR);
        g.fillRoundRect(deckArea.x, deckArea.y, deckArea.width, deckArea.height, 15, 15);

        g.setColor(LABEL_COLOR);
        double textOffset = layout.getTextOffset();
        paintCenteredString(g, "Deck", deckArea.getCenterX(), deckArea.y + deckArea.height + textOffset);

        double cardWidth = layout.getCardWidth();
        double cardHeight = layout.getCardHeight();
        double baseX = deckArea.getCenterX() - cardWidth / 2;
        double baseY = deckArea.getCenterY() - cardHeight / 2;
        Deck deck = layout.getGame().getDeck();
        paintCardPile(g, baseX, baseY, deck.asList());
    }

    /**
     * Paints the draw area of the CaboGame.
     *
     * @param g Graphics object used to paint the draw area.
     */
    private void paintDrawArea(Graphics2D g) {
        Rectangle drawArea = layout.getDrawArea();

        g.setColor(CARD_AREA_COLOR);
        g.fillRoundRect(drawArea.x, drawArea.y, drawArea.width, drawArea.height, 15, 15);

        double textOffset = layout.getTextOffset();
        g.setColor(LABEL_COLOR);
        paintCenteredString(g, "Draw", drawArea.getCenterX(), drawArea.y + drawArea.height + textOffset);

        double cardWidth = layout.getCardWidth();
        double cardHeight = layout.getCardHeight();
        double baseX = Math.round(drawArea.getCenterX() - cardWidth / 2);
        double baseY = Math.round(drawArea.getCenterY() - cardHeight / 2);
        Card drawnCard = layout.getGame().getDrawnCard();
        paintCard(g, (int)baseX, (int)baseY, drawnCard);
    }

    /**
     * Paint the Player area of the given Player.
     *
     * @param g Graphics object used to paint the player area.
     * @param player Player whose area to paint.
     * @see Player
     */
    private void paintPlayerArea(Graphics2D g, Player player) {

        CaboGame game = layout.getGame();
        Rectangle playerArea = layout.getPlayerArea(player);
        Rectangle[] cardAreas = layout.getPlayerCardAreas(player);
        java.util.List<Card> cards = player.getCards();

        g.setColor(CARD_AREA_COLOR);
        g.fillRoundRect(playerArea.x, playerArea.y, playerArea.width, playerArea.height, 15, 15);

        for (int i = 0; i < cards.size(); ++i) {
            Card card = cards.get(i);
            Rectangle cardArea = cardAreas[i];
            paintCard(g, cardArea.x, cardArea.y, card);
        }

        if (player == game.getCurrentPlayer()) {
            double textOffset = layout.getTextOffset();
            g.setColor(TEXT_COLOR);
            String turnPhaseDescription = game.getTurnPhase().getDescription();
            if (player == game.getPlayer1())
                paintCenteredString(g, turnPhaseDescription, playerArea.getCenterX(), playerArea.y - textOffset);
            else
                paintCenteredString(g, turnPhaseDescription, playerArea.getCenterX(), playerArea.y + playerArea.height + textOffset);
        }

    }

    /**
     * Paints the dragged card.
     *
     * @param g Graphics object used to paint the dragged card.
     * @see DraggedCard
     */
    private void paintDraggedCard(Graphics2D g) {
        if (layout.getDraggedCard().getCard() != null && layout.getGame().getTurnPhase() instanceof InitialPeekPhase) {
            layout.setDraggedCard(new DraggedCard(null));
            return;
        }
        DraggedCard dragged = layout.getDraggedCard();
        paintCardImmediately(g, dragged.getX(), dragged.getY(), dragged.getCard());
    }
}
