package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class FileController {
    private String fileLocation;

    public FileController(String fileLocation){
        this.fileLocation = fileLocation;
    }

    //reading my files
    public Stock readFile(String symbol){
        try {
            File myStocks = new File(fileLocation + symbol + ".csv");
            Scanner sc = new Scanner(myStocks);
            Stock s = new Stock(symbol);

            //reading the first line to move the scanner to the second line (1st line contains symbol name)
            String sym = "";
            if (sc.hasNextLine())
                sym = sc.nextLine();

            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                String[] splitted = data.split(",");
                Date date = new SimpleDateFormat("MM/dd/yyyy").parse(splitted[0]);

                double price;
                if (splitted[1].charAt(0) < '0' || splitted[1].charAt(0) > '9')
                    price = Double.parseDouble(splitted[1].substring(1));
                else
                    price = Double.parseDouble(splitted[1]);

                s.addNewPrice(date, price);
            }

            s.sortStocks();
            return s;
        }catch (IOException | ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    //daten vom API speichern
    public void appendToFile(ArrayList<String> s, String symbol) {
        String fileName = fileLocation + symbol + ".csv";
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < s.size(); ++i) {
                bw.write(s.get(i));
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
