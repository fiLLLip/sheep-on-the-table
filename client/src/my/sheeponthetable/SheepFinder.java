package my.sheeponthetable;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import my.sheeponthetable.gui.PasswordScreen;

/**
 * This class starts the entire program.
 * 
 * @author Filip
 */
public class SheepFinder {

    /**
     * Main method.
     */
    public static void main(String args[])  {

        /* MAC OS X LOOK AND FEEL */
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
        } catch (InstantiationException e) {
            System.out.println("InstantiationException: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException: " + e.getMessage());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("UnsupportedLookAndFeelException: " + e.getMessage());
        }
        /* End Mac OS X Look and feel */

        // Create and display the first dialogue box.
        new PasswordScreen().setVisible(true);

    }
}
