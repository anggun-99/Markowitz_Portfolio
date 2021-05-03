package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        DataController data = new DataController("data/");
        Stock s = data.getDataForSymbol("AMD");
        s.sortStocks();

        for (int i = 0; i < s.getStockPriceSize(); ++i){
            System.out.println("Date: " + s.getStockPriceAt(i).getDate().toString() + "\t Price : " +
                    s.getStockPriceAt(i).getPrice().toString());
        }


	// write your code here
        /*try{
            File myStocks = new File("data/AMD.csv");
            Scanner sc = new Scanner(myStocks);

            while(sc.hasNextLine()) {
                String data = sc.nextLine();
                //System.out.println(data);
            }

            sc.close();
             //BufferedReader = reading the lines directly
            BufferedReader reader = new BufferedReader(new FileReader("data/CAT.csv"));

            List<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null ) {
                lines.add(line);
            }

            System.out.println(lines.get(1));

            //Tagesrendite = (Rendite(Zeitpunkt t) - Rendite(Zeitpunkt t-1))/Rendite(Zeitpunkt t-1))
            double[] prices = new double[lines.size()-1];

            //Prices in einem Array speichern
            for(int i= 1; i<= lines.size()-1; i++) {
                String datei = lines.get(i);
                String[] dateiSplitted = datei.split(",");
                prices[i-1] = Double.parseDouble(dateiSplitted[1].substring(1));
            }

            System.out.println(Arrays.toString(prices));



            double[] tagesrendite = new double[prices.length-1];
            double counter = 1;

            for(int i = 1; i < prices.length; i++) {
                tagesrendite[i - 1] = (prices[i-1] - prices[i]) / prices[i];
                counter++;
            }

            System.out.println(Arrays.toString(tagesrendite));


            //Erwartete Rendite
            //wir sagen aus, dass die wahrscheinlichkeit der Rendite gleich sind.
            double wahrscheinlichkeit = 1/counter;
            double gesamtRendite = 0;
            double erwarteteRendite = 0;
            double average = 0;
            Arrays.sort(tagesrendite);

            for( int i = 0; i<= tagesrendite.length-1; i++ ) {
                erwarteteRendite += wahrscheinlichkeit * tagesrendite[i];
                gesamtRendite += tagesrendite[i];
            }
            average = gesamtRendite / counter;
            System.out.println("die erwartete Rendite lautet: " + erwarteteRendite);
            System.out.println("Durchschnittliche Rendite ist " + average*250);


        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }
}
