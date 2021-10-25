package com.company;

public class Portfolio {

    public static void main(String[] args) {
        Gui gui = new Gui("data/");
        gui.start();

        try {
            //join: thread wartet, bis andere Thread fertig ist
            gui.join();
            //wenn ein anderer thread den gui thread unterbricht
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
