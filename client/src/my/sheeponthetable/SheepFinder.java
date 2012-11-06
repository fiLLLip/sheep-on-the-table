/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import my.sheeponthetable.gui.PasswordScreen;

/**
 *
 * @author Filip
 */
public class SheepFinder {

    /**
     * @param args the command line arguments
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

        /* Create and display the form */
        new PasswordScreen().setVisible(true);

    }
}
