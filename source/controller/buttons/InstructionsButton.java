package controller.buttons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @version 2.0
 *
 * This InstructionButton can be pressed to give pop-up instructions for how to play Cabo.
 *
 * @see model.CaboGame
 */
public class InstructionsButton extends JButton {

    private static final String CABO_INSTRUCTIONS =
            "Cabo is an information management game in which players compete to gather accurate information about their own cards,\n" +
            "as well as their opponent's cards. This particular version of Cabo is Jana's personal variation which has slightly\n" +
            "altered rules from the standard Cabo game.\n" +
            "\n" +
            "Each game of Cabo starts by dealing 4 cards *face down* to to each player, and 1 card is dealt *face up* on the discard\n" +
            "pile. At the end of each game, the player whose cards have the lowest *sum value* wins - so each player is looking to\n" +
            "discard as many cards as possible, and keep only cards with a low value.\n" +
            "\n" +
            "After the initial cards have been dealt, the players take turns peeking at any 2 of their 4 cards. The cards are peeked\n" +
            "in secret, so opponents cannot see the cards being peeked. When each player finishes peeking at their 2 cards, the\n" +
            "main phase of Cabo begins.\n" +
            "\n" +
            "In the main phase of Cabo, each player starts their turn by drawing 1 card either from the deck, or from the discard\n" +
            "pile. The drawn card is placed *face up* on the draw area for all players to see. The player can choose to either swap\n" +
            "the drawn card with one of their own cards, or to discard the drawn card. If the player chooses to swap out one of their\n" +
            "cards, the swapped out card is placed *face up* on top of the discard pile, and the drawn card is added to the player's\n" +
            "hand *face-down*. The player's turn then ends.\n" +
            "\n" +
            "If, instead, the player chooses to discard the pile from the draw area\n" +
            "the drawn card is placed *face-up* on top of the discard pile. If the card is discarded from the draw area in this way\n" +
            "and it has a special ability written on it, the special ability is then triggered and the player must use it before\n" +
            "ending their turn - this is the only way to trigger a card's special ability. If the discarded card does *not* have a\n" +
            "special ability, the player's turn then immediately ends.\n" +
            "\n" +
            "At the start of any player's turn, before they have drawn any cards, they may instead choose to call Cabo! When a player\n" +
            "does this, their turn ends immediately. All other players get 1 additional turn to play, during which they may not call\n" +
            "Cabo again, and the game then ends. After the last turn is played, the *sum value* of all player cards is calculated,\n" +
            "and the winner is declared.\n" +
            "\n" +
            "Additionally, at *any* point of a player's turn the player may try to discard one of the cards from their hand, or one\n" +
            "of the cards from any of their opponent's hands - this is called card *dropping*. If the card that was dropped is the\n" +
            "same as the card on top of the discard pile, then the drop is successful, and the card stays in the discard pile. If a\n" +
            "player successfully drops an opponent's card, they may then transfer one of their cards card over to their opponent,\n" +
            "placing it *face down* in the opponent's hand without either player looking at the card. If the player makes a mistake\n" +
            "during a card drop, and the dropped card is not the same as the card on top of the discard pile, the dropped card stays\n" +
            "put, and the player must draw one additional card from the deck *face-down*.\n";

    /**
     * Constructs a new InstructionButton.
     */
    public InstructionsButton() {
        super();
        setProperties();

        addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        CABO_INSTRUCTIONS,
                        "How to play Cabo!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    /**
     * Sets the properties of this button - such as text and tooltips.
     */
    private void setProperties() {
        setVerticalTextPosition(AbstractButton.CENTER);
        setHorizontalTextPosition(AbstractButton.CENTER);
        setToolTipText("Read about how to play Cabo! Or freshen up your memory on the rules.");
        setMnemonic('I');
        setText("Instructions");
    }

}
