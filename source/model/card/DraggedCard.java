package model.card;

import java.util.Observable;

/**
 * @author Boris
 * @version 1.5
 *
 * Represents a Card being dragged with the mouse. The DraggedCard is tethered to some original location where it came
 * from and then it stores its relative position to that tether location.
 *
 * @see Card
 */
public class DraggedCard extends Observable {

    private final Card card;
    private int relativeX;
    private int relativeY;
    private int tetherX;
    private int tetherY;

    /**
     * Constructs a DraggedCard from a given Card. The tether/relative locations are zero-initialized. The card cannot
     * be changed after constructing the DraggedCard, so a new DraggedCard must be constructed when the Card changes.
     *
     * @param card The Card being moved.
     * @see Card
     */
    public DraggedCard(Card card) {
        this.card = card;
        relativeX = 0;
        relativeY = 0;
        tetherX = 0;
        tetherY = 0;
    }

    /**
     * @return The card being moved.
     * @see Card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets the relative position of this DraggedCard to the original tether location where the card came from.
     *
     * @param x The relative x position of the card.
     * @param y The relative y position of the card.
     */
    public void setRelative(int x, int y) {
        relativeX = x;
        relativeY = y;
    }

    /**
     * Sets the tether position of this DraggedCard, which represents the cards original location before being moved.
     *
     * @param x The relative x position of the card.
     * @param y The relative y position of the card.
     */
    public void setTether(int x, int y) {
        tetherX = x;
        tetherY = y;
    }

    /**
     * @return The absolute X position of this DraggedCard.
     */
    public int getX() {
        return tetherX + relativeX;
    }

    /**
     * @return The absolute Y position of this DraggedCard.
     */
    public int getY() {
        return tetherY + relativeY;
    }

}
