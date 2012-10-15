package my.sheeponthetable.tools;

import my.sheeponthetable.gui.SheepPanel;

/**
 * The ServerPinger class is a separate thread that every once in a while pings
 * the server and asks whether there are new updates availible to fetch.
 *
 * @author eliasaa
 */
public class ServerPinger extends Thread {

    public static final int WAITING_PERIOD = 10000;
    private ServerConnector connect;
    private SheepPanel sheepPanel;
    private boolean running;

    public ServerPinger(SheepPanel sp) {
        this.sheepPanel = sp;
        connect = sheepPanel.getConnector();
    }

    public void run() {
        running = true;
        while (running) {
            System.out.println("Doing a check!");
            if (connect.haveNewUpdates()) {
                System.out.println("New stuff!");
                sheepPanel.refreshSheepList();
            }
            try {
                Thread.sleep(WAITING_PERIOD);
            }
            catch (InterruptedException ie) {
                System.out.println("Interupted!");
            }
        }
    }

}