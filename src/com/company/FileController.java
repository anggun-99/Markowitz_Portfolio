package com.company;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class FileController {
    private String fileLocation;

    public FileController(String fileLocation){
        this.fileLocation = fileLocation;
    }

    public Stock readFile(String symbol){
        try {
            File myStocks = new File(fileLocation + symbol + ".csv");
            Scanner sc = new Scanner(myStocks);

            Stock s = new Stock(symbol);

            String sym = "";
            if (sc.hasNextLine()){
                sym = sc.nextLine();
            }

            while (sc.hasNextLine()) {
                String data = sc.nextLine();

                String[] splitted = data.split(",");

                Date date = new SimpleDateFormat("MM/dd/yyyy").parse(splitted[0]);


                Double price = 0.0;
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

}
