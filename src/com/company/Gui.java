package com.company;

import plotter.Graphic;
import plotter.LineStyle;
import plotter.Plotter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;

//Multithreading verhindert die Blockierung vom GuI-Thread
//Wegen einer langen Berechnung kann GuI-Thread blockiert werden.
//in diesem Projekt werden lange Berechnungen durchgeführt (z.B. bei API-Calls),
//deswegen ist multithreading für dieses Projekt geeignet.

public class Gui extends Thread {
    private DataController data;
    private Graphic graphic;
    private Plotter plotter;
    private JTextField stock1;
    private JTextField stock2;
    private HashSet<String> knownSymbols;

    public Gui(String dataPath){
        data = new DataController(dataPath);
    }

    @Override
    public void run() {
        super.run();
        graphic = new Graphic("Portfoliomanagement nach Markowitz");
        plotter = graphic.getPlotter();
        graphic.setPreferredSize(new Dimension(1000,1000));
        graphic.setLocation(500,50);

        addMenu(graphic);
        addTextFieldAndButton(graphic);

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
                        " und die Portfolioselection nach der Markowitz-Theorie von 2 Aktien.\n " +
                        "Die Kombination von Anlagealternativen wird mit Hilfe des Plotters graphisch dargestellt.",
                        "Allgemein", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenuItem ueberAutoren = new JMenuItem("Autoren");
        ueberAutoren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(graphic, "- Anggun Bhakti Sekarpelangi, 5264100\n"
                        + "- Jauhar Bariq Ghifari, 5265578", "über Autoren", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        infoMenu.add(allgemein);
        infoMenu.add(ueberAutoren);

    }

    private void addTextFieldAndButton(Graphic graphic) {
        stock1 = new JTextField();
        stock2 = new JTextField();
        plotter.removeAll();

        Box b = Box.createVerticalBox();
        b.setBorder(BorderFactory.createEmptyBorder(500, 0, 300, 50));
        b.add(new JLabel("Insert Stock's Ticker"));
        b.add(stock1);
        b.add(new JLabel("Insert Stock's Ticker"));
        b.add(stock2);

        JButton startButton = new JButton("START");
        startButton.addActionListener(new ActionListener() {
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

                if (sym1.equals("")|| !knownSymbols.contains(sym1)){
                    correctInput = false;
                    JOptionPane.showMessageDialog(graphic, "Please enter the first valid ticker" );
                    stock1.setText("");
                    stock2.setText("");
                }

                if (sym2.equals("")|| !knownSymbols.contains(sym2)){
                    correctInput = false;
                    JOptionPane.showMessageDialog(graphic, "Please enter the second valid ticker");
                    stock1.setText("");
                    stock2.setText("");
                }

                if (sym1.equals(sym2)) {
                    correctInput = false;
                    JOptionPane.showMessageDialog(graphic, "Please enter 2 different ticker");
                }

                if (correctInput){
                    PortfolioCalculator port = new PortfolioCalculator(data, sym1, sym2);
                    Thread t = new Thread(port);
                    t.start();
                    try {
                        t.join(); //current (gui) thread waits until the other thread finished its execution.
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

        JButton informationButton = new JButton("INFORMATION");
        informationButton.addActionListener(new ActionListener() {
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

                if (sym1.equals("")|| !knownSymbols.contains(sym1) ){
                    correctInput = false;
                    JOptionPane.showMessageDialog(graphic, "Please enter the first valid ticker");
                    stock1.setText("");
                    stock2.setText("");
                }

                if (sym2.equals("")|| !knownSymbols.contains(sym2)){
                    correctInput = false;
                    JOptionPane.showMessageDialog(graphic, "Please enter the second valid ticker");
                    stock1.setText("");
                    stock2.setText("");
                }

                if (sym1.equals(sym2)) {
                    correctInput = false;
                    JOptionPane.showMessageDialog(graphic, "Please enter 2 different ticker");
                }

                if (correctInput) {
                    PortfolioCalculator port = new PortfolioCalculator(data, sym1, sym2);

                    double[] portRendite = port.calculateRenditeForPortfolio(sym1,sym2);
                    double[] portRisiko = port.calculateRiskForPortfolio(sym1,sym2);

                    int weightSym1Return = 0;
                    int weightSym2Return = 100;
                    int weightSym1Risk = 0;
                    int weightSym2Risk = 100;

                    String msg = "jährliche erwartete Rendite (" + sym1 + "): " + port.calculateRenditeForStock(sym1) * 100 + " %"
                            + "\t \t jährliches Risiko (" + sym1 + "): " + port.calculateRiskForStocks(sym1) * 100 + " %"
                            + "\njährliche Rendite (" + sym2 + "): " + port.calculateRenditeForStock(sym2) * 100 + " %"
                            + "\t \t jährliches Risiko (" + sym2 + "): " + port.calculateRiskForStocks(sym2) * 100 + " %"
                            + "\nKovarianz von beiden Aktien: " + port.calculateCovarianceOfStocks(sym1, sym2)
                            + "\nKorrelation von beiden Aktien: " + port.calculateCorrelationOfStocks(sym1,sym2)
                            + "\n\ndie zu erwartenden Rendite des Portfolios: \n";

                    for( int i = 0; i < portRendite.length; i++ ) {
                        msg += weightSym1Return + " % von " + sym1 + " und " + weightSym2Return
                                + " % von " + sym2 + " ergibt: " + (portRendite[i]) * 100 + " % \n";
                        weightSym1Return +=10;
                        weightSym2Return -=10;
                    }
                    msg += "\n die Risiken des Portfolios: \n";

                    for( int i = 0; i < portRisiko.length; i++ ) {
                        msg += weightSym1Risk + " % von " + sym1 + " und " + weightSym2Risk
                                + " % von " + sym2 + " ergibt: " + (portRisiko[i]) * 100 + " % \n";
                        weightSym1Risk += 10;
                        weightSym2Risk -= 10;
                    }

                    JOptionPane.showMessageDialog(graphic, msg, "Information über " + sym1 + " und " + sym2,
                            JOptionPane.INFORMATION_MESSAGE);

                }
            }
        });
        b.add(startButton);
        b.add(informationButton);

        graphic.addEastComponent(b);
    }

    public void diagram(double[]Risiko,double[]Rendite) {
        graphic.setTitle("RISIKO-RENDITE-DIAGRAMM");
        plotter.removeAll();
        plotter.removeDataObject("Kreise");


        plotter.setDataLineStyle("Kreise", LineStyle.SYMBOL);
        plotter.setDataColor("Kreise", Color.BLUE);
        plotter.setSymbolSize(10);
//        plotter.setYLine(0);
//        plotter.setXLine(0);
        plotter.setBackground(Color.WHITE);
        plotter.setAutoXgrid(0.05);
        plotter.setXLabelFormat("%.2f");
        plotter.setYLabelFormat("%.2f");
        plotter.setAutoYgrid(0.02);
        plotter.setLabelFormat("%.2f");
        plotter.setStatusLine("Kombination von " + stock1.getText().toUpperCase() + " und "
                + stock2.getText().toUpperCase());


        for (int i = 0; i < Risiko.length; i++) {
            plotter.add(Risiko[i], Rendite[i]);
            plotter.add("Kreise", Risiko[i], Rendite[i]);
        }

        graphic.pack();
        graphic.repaint();
    }
}
