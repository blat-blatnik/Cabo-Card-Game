package view;

import controller.MenuBar;
import controller.Board;
import controller.CardDragger;
import model.CaboGame;
import util.CaboIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @version 1.5
 *
 * The main JFrame class used for the Cabo card game application.
 *
 * @see CaboGame
 * @see BoardLayout
 * @see Board
 * @see Panel
 * @see MenuBar
 * @see CardDragger
 * @see JFrame
 */
public class Frame extends JFrame {

    /**
     * Initializes a CaboFrame with a given CaboGame. The frame creates a CaboBoardLayout from the game, a CaboBoard and
     * a CaboPanel from this layout. It also sets up a menu bar with buttons to control some aspects of the CaboGame.
     *
     * @param game game that is passed to the frame.
     */
    public Frame(CaboGame game) {
        super("Cabo!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(new MenuBar(game));

        BoardLayout layout = new BoardLayout(game);
        Board board = new Board(layout);
        Panel panel = new Panel(layout);

        new CardDragger(game, board, panel);
        add(panel);
        setMinimumSize(new Dimension(400, 400));
        setPreferredSize(new Dimension(800, 800));
        setResizable(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CaboIO.saveCaboGame(game);
            }
        });
    }
}
