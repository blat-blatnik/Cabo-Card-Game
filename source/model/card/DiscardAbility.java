package model.card;

import view.CardTextures;

import java.io.Serializable;

/**
 * @version 1.0
 *
 * Represents a special discard ability of a CaboCard that is activated when the card is discarded.
 *
 * WARNING: Do not change the names of this enum! The names must match the filenames of the card textures, since
 * CardTextures uses the name of the enum to decide which texture to load for which card.
 *
 * @see CaboCard
 * @see Card
 * @see CardTextures
 */
public enum DiscardAbility implements Serializable {
    NOTHING,
    SWAP,
    SPY,
    PEEK,
    PEEK_SPY_AND_SWAP
}