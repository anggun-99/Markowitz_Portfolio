package com.company;

import plotter.Graphic;
import plotter.Plotter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

//die Anwendung von Multithreading verhindert die Blockierung von dem GuI-Thread wegen einer langen Berechnungen.
public class Gui extends Thread {
    private JTextField indexField = new JTextField();
    private DataController data;
    private HashMap<String, PortfolioCalculator> ticker;
    private Graphic graphic;
    private Plotter plotter;
    private JTextField stock1;
    private JTextField stock2;
    private HashSet<String> knownSymbols;

    public Gui(String dataPath){
        data = new DataController(dataPath);
    };

    @Override
    public void run() {
        super.run();
        graphic = new Graphic("Portfoliomanagement nach Markowitz");
        plotter = graphic.getPlotter();

        addMenu(graphic);
        addTextField(graphic);

        graphic.pack();
        graphic.repaint();

        knownSymbols = new HashSet<>();

        try {
            File folder = new File("data/");
            for (final File fileEntry : folder.listFiles()) {
                knownSymbols.add(fileEntry.getName().substring(0, fileEntry.getName().indexOf('.')));
            }
        } catch (NullPointerException exp){
            System.out.println(exp.getStackTrace());
        }
    }

    private void addMenu(Graphic graphic) {
        JMenu infoMenu = new JMenu("Information");
        graphic.addExternMenu(infoMenu);

        JMenuItem allgemein = new JMenuItem("Allgemein");
        allgemein.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(graphic, "Dieses Projekt eignet sich für das Portfoliomanagement" +
                        " und die Portfolioselection nach der Markowitz-Theorie.\n Die Kombination von Anlagealternativen" +
                        " wird mit Hilfe des Plotters graphisch dargestellt.", "Allgemein", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenuItem ueberAutoren = new JMenuItem("Autoren");
        ueberAutoren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(graphic, "- Anggun Bhakti Sekarpelangi, 5264100\n"
                        + "-Jauhar Bariq Ghifari, 5265578", "über Autoren", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        infoMenu.add(allgemein);
        infoMenu.add(ueberAutoren);

    }

    private void addTextField(Graphic graphic) {
        stock1 = new JTextField();
        stock2 = new JTextField();
        plotter.removeAll();

        Box b = Box.createVerticalBox();
        b.setBorder(BorderFactory.createEmptyBorder(200, 0, 50, 200));
        b.add(new JLabel("Insert Stock's Ticker"));
        b.add(stock1);
        b.add(new JLabel("Insert Stock's Ticker"));
        b.add(stock2);
        JButton butt = new JButton("START");
        butt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean correctInput = true;
                String sym1 = stock1.getText();
                String sym2 = stock2.getText();

                sym1 = sym1.toUpperCase();
                sym2 = sym2.toUpperCase();

                sym1 = sym1.replaceAll(" ", "");
                sym1 = sym1.replaceAll("\t", "");
                sym2 = sym2.replaceAll(" ", "");
                sym2 = sym2.replaceAll("\t", "");

                if (sym1.equals("") || !knownSymbols.contains(sym1)){
                    correctInput = false;
                    JOptionPane.showMessageDialog(graphic, "Please enter the first valid ticker");
                    stock1.setText("");
                    stock2.setText("");
                }

                if (sym2.equals("") || !knownSymbols.contains(sym2)){
                    correctInput = false;
                    JOptionPane.showMessageDialog(graphic, "Please enter the second valid ticker");
                    stock1.setText("");
                    stock2.setText("");
                }

                if (correctInput){
                    PortfolioCalculator port = new PortfolioCalculator(data, sym1, sym2);
                    Thread t = new Thread(port);
                    t.start();
                    try {
                        t.join();
                        double[] portRendite = new double[11];
                        double[] portRisk = new double[11];

                        portRendite = port.getYearlyReturnPort();
                        portRisk = port.getYearlyRiskPort();
                        diagram(portRisk, portRendite);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        });
        b.add(butt);/*

        graphic.addEastComponent(new JLabel("Insert Stock's Ticker"));
        graphic.addEastComponent(t1);
        graphic.addEastComponent(new JLabel("Insert Stock's Ticker"));
        graphic.addEastComponent(new JTextField());*/
        graphic.addEastComponent(b);
    }

    public void diagram(double[]Risiko,double[]Rendite) {
        graphic.setTitle("RISIKO-RENDITE-DIAGRAMM");
        plotter.removeAll();

        plotter.setYLine(0);
        plotter.setXLine(0);
        plotter.setBackground(Color.WHITE);
        plotter.setAutoXgrid(0.02);
        plotter.setAutoYgrid(0.05);

        for (int i = 0; i < Risiko.length; i++) {
            plotter.add(Risiko[i], Rendite[i]);
        }


        graphic.pack();
        graphic.repaint();
    }
}
