package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        try{
            File myStocks = new File("data/HistoricalData_1619797223602.csv");
            Scanner sc = new Scanner(myStocks);

            while(sc.hasNextLine()) {
                String data = sc.nextLine();
                System.out.println(data);
            }

            sc.close();
             //BufferedReader = reading the lines directly
            BufferedReader reader = new BufferedReader(new FileReader("data/HistoricalData_1619797306203.csv"));

            List<String> lines = new ArrayList<>();
            String line = null;

            while ((line = reader.readLine()) != null ) {
                lines.add(line);
            }

            System.out.println(lines.get(1));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
