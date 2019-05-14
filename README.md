# bmon-odds-scraper, mini bookie software scraping a well known bookie.

1. Checkout and import to Intellij.
2. Download chromedriver.exe and place it in \\bin\\.
3. Create a postgresql database `bmon` with user `postgres` and pass `d7MY3fhiqo1Z51rhjWR8` (or edit application.properties). 
4. Run `BmonOddsScraperApplication.java`
5. Send the following request to start the Chrome instance
````
POST http://localhost:8080/webdriver
Content-Type: application/json

{
    "sportName": "Tennis"
}
````
6. Send the following request to start scraping the specified Sport/Market
````
POST http://localhost:8080/scrape/timer
Content-Type: application/json

{
    "sportName": "Tennis",
    "marketName": "Winner"
}
````
7. Start the front-end with `npm start`
8. Create a user in `/register` and place bets etc. (OPTIONAL)
