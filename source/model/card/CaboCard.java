package model.card;

import view.CardTextures;

import java.io.Serializable;

/**
 * @version 2.0
 *
 * Represents the playing cards used for Cabo. Cabo can technically be player with a standard deck of 54 cards,
 * however some of the values of the cards are different than whats shown on a standard playing card, and they also
 * have special abilities which are represented by this custom enum.
 *
 * WARNING: Do not change the names of this enum! The names must match the filenames of the card textures, since
 * CardTextures uses the name of the enum to decide which texture to load for which card.
 *
 * @see Card
 * @see Deck
 * @see DiscardAbility
 * @see CardTextures
 */
public enum CaboCard implements Serializable {

    ACE_HEARTS      (1),
    TWO_HEARTS      (2),
    THREE_HEARTS    (3),
    FOUR_HEARTS     (4),
    FIVE_HEARTS     (5),
    SIX_HEARTS      (6),
    SEVEN_HEARTS    (7,  DiscardAbility.PEEK),
    EIGHT_HEARTS    (8,  DiscardAbility.PEEK),
    NINE_HEARTS     (9,  DiscardAbility.SPY),
    TEN_HEARTS      (10, DiscardAbility.SPY),
    JACK_HEARTS     (10, DiscardAbility.SWAP),
    QUEEN_HEARTS    (10, DiscardAbility.PEEK_SPY_AND_SWAP),
    KING_HEARTS     (-1),

    ACE_DIAMONDS    (1),
    TWO_DIAMONDS    (2),
    THREE_DIAMONDS  (3),
    FOUR_DIAMONDS   (4),
    FIVE_DIAMONDS   (5),
    SIX_DIAMONDS    (6),
    SEVEN_DIAMONDS  (7,  DiscardAbility.PEEK),
    EIGHT_DIAMONDS  (8,  DiscardAbility.PEEK),
    NINE_DIAMONDS   (9,  DiscardAbility.SPY),
    TEN_DIAMONDS    (10, DiscardAbility.SPY),
    JACK_DIAMONDS   (10, DiscardAbility.SWAP),
    QUEEN_DIAMONDS  (10, DiscardAbility.PEEK_SPY_AND_SWAP),
    KING_DIAMONDS   (-1),

    ACE_CLUBS       (1),
    TWO_CLUBS       (2),
    THREE_CLUBS     (3),
    FOUR_CLUBS      (4),
    FIVE_CLUBS      (5),
    SIX_CLUBS       (6),
    SEVEN_CLUBS     (7,  DiscardAbility.PEEK),
    EIGHT_CLUBS     (8,  DiscardAbility.PEEK),
    NINE_CLUBS      (9,  DiscardAbility.SPY),
    TEN_CLUBS       (10, DiscardAbility.SPY),
    JACK_CLUBS      (10, DiscardAbility.SWAP),
    QUEEN_CLUBS     (10, DiscardAbility.PEEK_SPY_AND_SWAP),
    KING_CLUBS      (10),

    ACE_SPADES      (1),
    TWO_SPADES      (2),
    THREE_SPADES    (3),
    FOUR_SPADES     (4),
    FIVE_SPADES     (5),
    SIX_SPADES      (6),
    SEVEN_SPADES    (7,  DiscardAbility.PEEK),
    EIGHT_SPADES    (8,  DiscardAbility.PEEK),
    NINE_SPADES     (9,  DiscardAbility.SPY),
    TEN_SPADES      (10, DiscardAbility.SPY),
    JACK_SPADES     (10, DiscardAbility.SWAP),
    QUEEN_SPADES    (10, DiscardAbility.PEEK_SPY_AND_SWAP),
    KING_SPADES     (10),

    BLACK_JOKER     (0),
    RED_JOKER       (0);

    private static final long serialVersionUID = 42L;

    /**
     * The value of this CaboCard used for calculating the final score of each player in Cabo.
     */
    public final int value;

    /**
     * The special ability that is activated by discarding this CaboCard in Cabo.
     */
    public final DiscardAbility discardAbility;

    /**
     * Initializes a CaboCard from it's value and a discard ability.
     *
     * @param value The value of the CaboCard.
     * @param discardAbility The special discard ability of the CaboCard.
     */
    CaboCard(int value, DiscardAbility discardAbility) {
        this.value = value;
        this.discardAbility = discardAbility;
    }

    /**
     * Initializes a CaboCard from it's value - and with no discard ability, since most CaboCards don't have one.
     *
     * @param value The value of the CaboCard.
     */
    CaboCard(int value) {
        this(value, DiscardAbility.NOTHING);
    }
}
