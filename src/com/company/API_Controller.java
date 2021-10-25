package com.company;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


public class API_Controller {
    private String API_Token;
    private String baseRequestURL;

    public API_Controller() {
        API_Token = "c4ub5eqad3ie1t1fq6f0";
        baseRequestURL = "https://finnhub.io/api/v1/";
    }

    public void addMissingPriceData(Stock stock) {
        try {
            Date fromDate = stock.getLatestDate();
            long from = fromDate.getTime() / 1000 + 24 * 60 * 60;
            long to = System.currentTimeMillis() / 1000;
            StringBuffer content = new StringBuffer(); //Appending each line of result and build a response

            String path = baseRequestURL + "/stock/candle?symbol=" + stock.getSymbol() + "&resolution=D&from=" + from +
                    "&to=" + to + "&token=" + API_Token;
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //Request setup = GET
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            //successful connection has the response code of 200
            if (status == 200) {
                BufferedReader in = new BufferedReader(
                        //the response of HttpURLConnection is an InputStream
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            } else {
                System.out.println("Failed to complete Http-Request. Error-Code " + status);
            }
            con.disconnect();

                /* JSON = JavaScript Object Notation
                JSON is a data format, that is commonly used for API.
                JSON is easy to read/write, and integrates easily with programming languages.
                */

            if (!content.toString().equals("{\"s\":\"no_data\"}")) {
                JSONObject jo = new JSONObject(content.toString());
                //JSON arrays.
                //getJSONArray returns the array value at the specified position in the array
                JSONArray open = jo.getJSONArray("o");
                JSONArray high = jo.getJSONArray("h");
                JSONArray low = jo.getJSONArray("l");
                JSONArray close = jo.getJSONArray("c");
                JSONArray timeStamp = jo.getJSONArray("t");
                JSONArray vol = jo.getJSONArray("v");

                ArrayList<String> lines = new ArrayList<>();
                //add new date to file, for the next time
                for (int i = 0; i < open.length(); ++i) {
                    //add to current stock
                    Timestamp ts = new Timestamp(timeStamp.getLong(i) * 1000);
                    Date d = new Date(ts.getTime());
                    stock.addNewPrice(d, close.getDouble(i));

                    //format date for output
                    LocalDate localDate = ts.toLocalDateTime().toLocalDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    String dateString = localDate.format(formatter);

                    //add csv string format: Date,Close/Last,Volume,Open,High,Low
                    lines.add(dateString + ",$" + close.getDouble(i) + "," + vol.getInt(i) + ",$" + open.getDouble(i) +
                            ",$" + high.getDouble(i) + ",$" + low.getDouble(i));
                }
                stock.sortStocks();
                //write lines to file
                FileController fileController = new FileController("data/");
                fileController.appendToFile(lines, stock.getSymbol());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
