package controller;

import model.CaboGame;
import model.Player;
import model.card.Card;
import model.card.DraggedCard;
import model.turnphase.TurnPhase;
import view.Panel;

import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author Jana, Boris and adapted from Demo
 * @version 4.0
 *
 * This controller listens for user input from the mouse and updates the model accordingly.
 * @see CaboGame
 * @see Panel
 * @see Board
 * @see Card
 * @see TurnPhase
 */
public class CardDragger extends MouseInputAdapter {

    private final CaboGame game;
    private final Panel panel;
    private final Board board;

    private boolean isDraggingCard;
    private int startX;
    private int startY;

    /**
     * Create a new card dragger that receives mouse events from the CaboPanel
     * supplied to this constructor
     *
     * @param game The actual CaboGame
     * @param panel CaboPanel needed to receive mouse events from
     * @param board the CaboBoard that provides important methods indicating where a mouse is clicked
     */
    public CardDragger(CaboGame game, Board board, Panel panel) {
        this.game = game;
        this.panel = panel;
        this.board = board;
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        isDraggingCard = false;
    }

    /**
     * If the mouse button is pressed the area where it is pressed is checked and the right corresponding method
     * in caboGame or turnPhase is called.
     *
     * @param event The MouseEvent needed to locate the position of the cursor
     */
    @Override
    public void mousePressed(MouseEvent event) {

        Card clickedCard = board.getCardAt(event.getPoint());
        if (clickedCard == null)
            return;

        TurnPhase turnPhase = game.getTurnPhase();
        TurnPhase.PhaseName phaseName = turnPhase.getPhaseName();

        if (phaseName == TurnPhase.PhaseName.PICKUP) {
            if (board.isInDeckArea(event.getPoint()))
                turnPhase.selectCard(clickedCard);
        } else
            turnPhase.selectCard(clickedCard);

        if (phaseName == TurnPhase.PhaseName.INITIAL_PEEK)
            return;

        isDraggingCard = true;
        startX = event.getX();
        startY = event.getY();
        board.startDraggingCard(clickedCard);
    }

    /**
     * When the mouse is released, the first thing that is checked is whether the player was trying to drop a card
     * from their own or their opponent's hand into the discard area, as this can be done in almost every turn phase.
     * If that was not successful, it goes on to further checks for different turn phases, where the player can for
     * example draw a card from the deck or discard pile etc.
     *
     * @param event The MouseEvent needed to locate the position of the cursor
     */
    @Override
    public void mouseReleased(MouseEvent event) {
        if (!isDraggingCard)
            return;

        Point releasePoint = event.getPoint();
        Card cardUnderReleasePoint = board.getCardAt(releasePoint);

        Player currentPlayer = game.getCurrentPlayer();
        Player opponent = game.getOpponent();

        TurnPhase turnPhase = game.getTurnPhase();
        TurnPhase.PhaseName phaseName = turnPhase.getPhaseName();

        if (phaseName != TurnPhase.PhaseName.INITIAL_PEEK
                && phaseName != TurnPhase.PhaseName.PICKUP
                && phaseName != TurnPhase.PhaseName.TRANSFER_CARD
                && board.isInDiscardArea(event.getPoint())){
            if (turnPhase.handleCardDrop()){
                board.stopDraggingCard();
                panel.repaint();
                isDraggingCard = false;
                return;
            }
        }

        switch(phaseName) {
            case DRAW_OR_CABO:
                if (board.isInDrawArea(event.getPoint())){
                    turnPhase.releaseCard();
                }
                break;
            case DISCARD_OR_SWAP:
                if (currentPlayer.hasCard(cardUnderReleasePoint)){
                    turnPhase.releaseCard(cardUnderReleasePoint);
                } else if (board.isInDiscardArea(releasePoint)){
                    turnPhase.releaseCard();
                }
                break;
            case PICKUP:
                if (board.isInPlayerArea(currentPlayer, releasePoint)){
                    int insertionIndex = board.getPlayerCardInsertionIndex(currentPlayer, releasePoint);
                    turnPhase.releaseCard(insertionIndex);
                }
                break;
            case TRANSFER_CARD:
                if (board.isInPlayerArea(opponent, releasePoint)){
                    int insertionIndex = board.getPlayerCardInsertionIndex(opponent, releasePoint);
                    turnPhase.releaseCard(insertionIndex);
                }
                break;
            case SWAP:
            case SPY_AND_SWAP:
                turnPhase.releaseCard(cardUnderReleasePoint);
                break;
        }

        board.stopDraggingCard();
        panel.repaint();
        isDraggingCard = false;
    }


    /**
     * If a card is isDraggingCard it is moved relative to the positions the mouse
     * was first pressed.
     *
     * @param event The MouseEvent needed to locate the position of the cursor
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        if (isDraggingCard) {
            DraggedCard card = board.getDraggedCard();
            card.setRelative(event.getX() - startX, event.getY() - startY);
            panel.repaint();
        }
    }
}
