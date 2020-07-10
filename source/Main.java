import model.CaboGame;

import util.CaboIO;
import view.Frame;

import javax.swing.*;
import java.awt.*;

/**
 * Runs the game. Although technically a controller this class can be found
 * more easily if it's not in that package.
 * If an old game state is available it is loaded into the game.
 */
public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't set system look and feel: " + e.getLocalizedMessage());
        }

        CaboGame game = CaboIO.tryLoadAutoSave();
        EventQueue.invokeLater(() -> new Frame(game));
    }
}
