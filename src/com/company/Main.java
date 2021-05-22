package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import jserver.Board;
import jserver.XSendAdapter;
import plotter.Graphic;
import plotter.Plotter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;

public class Main {
private XSendAdapter xsend;
private JTextField indexField = new JTextField();

    public static void main(String[] args) {


       DataController data = new DataController("data/");
        /*Stock s = data.getDataForSymbol("NFLX");
        s.sortStocks();

        for (int i = 0; i < s.getStockPriceSize(); ++i){
            System.out.println("Date: " + s.getStockPriceAt(i).getDate().toString() + "\t Price : " +
                    s.getStockPriceAt(i).getPrice().toString());
        }*/

        String symbol1 = "BAYN.DE";
        String symbol2 = "BMW.DE";

        PortfolioCalculator portfolioCalculator = new PortfolioCalculator(data);
        PortfolioCalculator portfolioCalculator1 = new PortfolioCalculator(data);

       System.out.print("jährliche erwartete Rendite (" + symbol1 + "): " + portfolioCalculator.calculateRenditeForStock(symbol1)
       + "\t jährliches Risiko (" + symbol1 + "): " + portfolioCalculator.calculateRiskForStocks(symbol1)
       + "\njährliche Rendite (" + symbol2 + "): " + portfolioCalculator1.calculateRenditeForStock(symbol2)
       + "\t jährliches Risiko (" + symbol2 + "): " + portfolioCalculator1.calculateRiskForStocks(symbol2)
       + "\nKovarianz von beiden Aktien: " + portfolioCalculator.calculateCovarianceOfStocks(symbol1, symbol2)
       + "\nKorrelation von beiden Aktien: " + portfolioCalculator.calculateCorrelationOfStocks(symbol1,symbol2)
       + "\ndie zu erwartenden Rendite des Portfolios: [");

       double[] portRendite = portfolioCalculator.calculateRenditeForPortfolio(symbol1,symbol2);

       for (int i = 0; i < portRendite.length; ++i){
           if (i != 0)
                System.out.print("," + portRendite[i]);
           else
               System.out.print(portRendite[i]);
       }

       double[] portRisiken = portfolioCalculator.calculateRiskForPortfolio(symbol1, symbol2);

       System.out.print("]" + "\ndie Risiken des Portfolios: [");
       for (int i = 0; i < portRisiken.length; ++i){
           if(i != 0 )
               System.out.print("," + portRisiken[i] );
           else
               System.out.print(portRisiken[i] );
       }
       System.out.print("]");
       //diagram(portRisiken,portRendite);

        Main e = new Main();
        e.starten();

    }

    void starten() {
        Graphic graphic = new Graphic("Portfoliomanagement nach Markowitz");

        addMenu(graphic);
        addTextField(graphic);

        graphic.pack();
        graphic.repaint();
    }

    private void addMenu(Graphic graphic) {
        JMenu infoMenu = new JMenu("Information");
        graphic.addExternMenu(infoMenu);

        JMenuItem allgemein = new JMenuItem("Allgemein");
        allgemein.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(graphic, "Portfoliomanagement ist...");
            }
        });

        JMenu autorMenu = new JMenu("Autoren");
        graphic.addExternMenu(autorMenu);

        JMenuItem ueberAutoren = new JMenuItem("ueber Autoren");
        ueberAutoren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(graphic, "-Anggun Bhakti Sekarpelangi, 5264100\n"
                        + "-Jauhar Bariq Ghifari, 5265578", "�ber Autoren", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        infoMenu.add(allgemein);
        infoMenu.add(autorMenu);

    }

    private void addTextField(Graphic graphic) {
        JTextField t1 = new JTextField();
        t1.setSize(20,8);

        Box b = Box.createVerticalBox();
        b.setBorder(BorderFactory.createEmptyBorder(150, 0, 50, 150));
        b.add(new JLabel("Insert Stock's Ticker"));
        b.add(t1);
        b.add(new JLabel("Insert Stock's Ticker"));
        b.add( new JTextField());
        JButton butt = new JButton("OIIIIIIIIIIIII");
        butt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("taxation is theft");
            }
        });
        b.add(butt);/*

        graphic.addEastComponent(new JLabel("Insert Stock's Ticker"));
        graphic.addEastComponent(t1);
        graphic.addEastComponent(new JLabel("Insert Stock's Ticker"));
        graphic.addEastComponent(new JTextField());*/
        graphic.addEastComponent(b);


    }
    public static void diagram(double[]Risiko,double[]Rendite) {
        Graphic graphic = new Graphic("Risk-Return Graph");
        Plotter plotter = graphic.getPlotter();

        graphic.setTitle("Funktionsplotter V0.0 SAE");

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
