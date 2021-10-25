# Welcome to the Portfolio-Calculation project. #

## Project structure: ##
- /data : contains csv files with price data of various stocks \
- /src/com.company : contains all the source files \

## Run the program ##

This step is pretty easy, just run "Project.jar"

## Class documentation: ##

### Portfolio ###

- creates a new Gui thread and starts the thread

### GUI ###

- creates the GUI
- contains various helper methods to split up the logic of different gui parts

### DataController ###

- contains all stock information, calls StockDataController if any Stock information is missing

### StockDataController ###

- gets stock data for a specific symbol
- uses the File and API Controller to read files and get new data from an api

### File Controller ###

- reads stock data from a csv file
- appends data to a csv file (used to store the fetched data from the api call)

### API_Controller ###

- makes an API call to finnhub.io to get price data for a specific stock between the last available data point and today


### Stock ###

- contains the ticker symbol name and StockPrice informations

### StockPrice ###

- saves a date and price information

### PortfolioCalculator ###

- calculates the following aspects of the portfolio and the stocks:
  - risk
  - covariance
  - return
- contains various helper functions

### MatrixCalculator ###

- used to multiply a matrix
