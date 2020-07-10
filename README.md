# Cabo Card Game

GUI program for playing a variant of the [Cabo](https://en.wikipedia.org/wiki/Cabo_(game)) card game. Cabo is an information management game in which players compete to gather accurate information. This program specifically plays a German variant of Cabo.

<p align="center">
  <img width="531" height="504" src="screenshots/screenshot.png">
</p>

This program was made for an assignment in Object Oriented Programming, so it was a collaborative project with [my lab partner](https://github.com/janaheit). We also had help from Olga Wazny, who designed all of the card textures.

## Requirements

[Java](https://www.java.com/en/download/) version 1.8 or higher.

## How to run

[Download]() `cabo.jar`, then run `$ java -jar cabo.jar`.

## Licence

This program and all of its source code are in the public domain, you can use them for anything you want. The card textures have a proprietary licence - you cannot use those in your own projects without the author's permission.

## Game Rules

Cabo is an information management game in which players compete to gather accurate information. This particular version of Cabo is a German variation with slightly altered rules.

#### Start
---------

Each game starts by dealing 4 cards *face down* to to each player, and 1 card *face up* to the discard pile. At the end of each game, the player whose cards have the *lowest* sum value wins - so each player is looking to discard cards when possible, and to keep only low valued cards.

After the initial cards have been dealt, the players take turns peeking at any 2 of their cards. The cards are peeked in secret, so opponents cannot see the cards being peeked. When each player finishes peeking at 2 of their cards, the main phase of Cabo begins.

#### Main phase
---------------

In the main phase, each player starts their turn by drawing 1 card either from the deck, or from the discard pile. The drawn card is placed *face up* on the draw area. The player can then either swap the drawn card with one of their own cards, or discard the drawn card. If the player chooses to swap out one of their cards, the swapped out card is placed *face up* on top of the discard pile, and the drawn card is added to the player's hand *face-down*. The player's turn then ends.

If, instead, the player chooses to discard, the drawn card is placed *face-up* on top of the discard pile. If the discarded card has a special ability written on it, this ability is triggered and the player must use it before ending their turn - this is the only way to trigger a card's special ability. If the discarded card does *not* have a special ability, the player's turn immediately ends.

#### Card dropping
------------------

Additionally, at *any* point during a player's turn the player may try to discard one of the cards from their hand, or from any of their opponent's hands - this is called card *dropping*. If the card that was dropped is *the same* as the card on top of the discard pile, then the drop is successful, and the card stays in the discard pile. If a player successfully drops an opponent's card, they may then transfer one of their cards card over to their opponent, placing it *face down* in the opponent's hand without either player looking at the card. If the player makes a mistake during a card drop, and the dropped card is not the same as the card on top of the discard pile, the dropped card stays in the player's hand, and the player must draw one additional card from the deck *face-down*.

#### Calling Cabo!
------------------

At the start of any player's turn, before they have drawn any cards, they may instead choose to call Cabo! When a player does this, their turn ends immediately. All other players get 1 additional turn to play, during which they may not call Cabo again, and the game then ends. After the last turn is played, the sum value of all player cards is calculated, and the winner is declared.

#### Card special effects
-------------------------

Special abilities are printed in red writting on the cards that have them. The abilities are as follows:

**PEEK** Peek at 1 card from your hand in secret.

**SPY** Peek at 1 card from any opponent's hand in secret.

**SWAP** Swap one of your cards with one of your opponent's cards. The player can also choose to skip this ability.

**SPY \& SWAP** Peek at 1 card from your hand, and 1 card from any opponent's hand, both in secret. Then, swap the peeked cards. The player may also choose to skip the swap part of this ability.
