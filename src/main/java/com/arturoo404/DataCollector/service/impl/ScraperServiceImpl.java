package com.arturoo404.DataCollector.service.impl;

import com.arturoo404.DataCollector.model.Offer;
import com.arturoo404.DataCollector.service.DataService;
import com.arturoo404.DataCollector.service.OfferService;
import com.arturoo404.DataCollector.service.ScraperService;
import com.arturoo404.DataCollector.validation.UrlValidation;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScraperServiceImpl implements ScraperService {

    private static final String URL = "https://www.otodom.pl/pl/oferty/sprzedaz/mieszkanie/cala-polska?limit=1000&page=";
    Logger logger = LoggerFactory.getLogger(ScraperServiceImpl.class);

    private final WebDriver webDriver;
    private final UrlValidation urlValidation;
    private final OfferService offerService;
    private final DataService dataService;
    @Autowired
    public ScraperServiceImpl(WebDriver webDriver, UrlValidation urlValidation, OfferService offerService, DataService dataService) {
        this.webDriver = webDriver;
        this.urlValidation = urlValidation;
        this.offerService = offerService;
        this.dataService = dataService;
    }

    //https://www.youtube.com/watch?v=PF0iyeDmu9E&ab_channel=ShaneLee
    //https://www.browserstack.com/guide/find-element-by-xpath-in-selenium
    //https://devqa.io/selenium-css-selectors/
    //https://stackoverflow.com/questions/40072671/driver-findelementby-cssselector-selenium-is-not-working-in-windows-10
    @PostConstruct
    private void scrap() throws InterruptedException {
        Instant start = Instant.now();
        String startData = dataService.currentData();
        logger.warn("Start collect data: " + startData);

        List<String> allLinks = new ArrayList<>();

        webDriver.get(URL + "1");
        Thread.sleep(250);

        //Turn off cookie
        try {
            WebElement button = webDriver.findElement(By.id("onetrust-accept-btn-handler"));
            button.click();
        } catch (Exception ignored) {}

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        final WebElement element = webDriver.findElements(By.cssSelector(".eo9qioj1.css-ehn1gc.e1e6gtx31")).get(3);
        int pages = Integer.parseInt(element.getText());

        //Collect all offers url
        for (int i = 1; i <= pages; i++){
            webDriver.get(URL + i);
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(100);

            List<String> links = webDriver.findElements(By.tagName("a"))
                    .stream()
                    .filter(v -> urlValidation.validate(v.getAttribute("href")))
                    .map(v -> v.getAttribute("href"))
                    .collect(Collectors.toList());

            allLinks.addAll(links);
        }

        //Collect data from all links and save in database
        for (String url : allLinks){
            webDriver.get(url);

            Offer offer = new Offer();
            offer.setUrl(url);
            offer.setLink(url);

            if(!webDriver.findElements(By.cssSelector(".css-ubt094.es5t28b7")).isEmpty()){
                offer.setFrom(webDriver.findElements(By.cssSelector(".css-ubt094.es5t28b7")).get(0).getText());
            }

            if(!webDriver.findElements(By.cssSelector(".e1w8sadu0.css-1helwne.exgq9l20")).isEmpty()){
                offer.setLocation(webDriver.findElements(By.cssSelector(".e1w8sadu0.css-1helwne.exgq9l20")).get(0).getText());
            }

            if (!webDriver.findElements(By.cssSelector(".css-1i5yyw0.e1l1avn10")).isEmpty()){
                offer.setPrice(webDriver.findElements(By.cssSelector(".css-1i5yyw0.e1l1avn10")).get(0).getText());
            }

            if (!webDriver.findElements(By.cssSelector(".css-14br9o.efcnut311")).isEmpty()){
                offer.setPricePerSquareMeter(webDriver.findElements(By.cssSelector(".css-14br9o.efcnut311")).get(0).getText());
            }

            try {
                offer.setNumberOfRooms(webDriver.findElement(By.xpath("//div[@aria-label='Liczba pokoi']//div[@class='css-1wi2w6s enb64yk4']")).getText());
            }catch (Exception ignored) {}

            try {
                offer.setArea(webDriver.findElement(By.xpath("//div[@aria-label='Powierzchnia']//div[@class='css-1wi2w6s enb64yk4']")).getText());
            }catch (Exception ignored) {}

            try {
                WebElement phoneNumber = webDriver.findElement(By.cssSelector("button[data-cy='phone-number.show-full-number-button']"));
                phoneNumber.click();
            } catch (Exception ignored) {}

            Thread.sleep(100);
            final List<WebElement> elements = webDriver.findElements(By.cssSelector("a[data-cy='phone-number.full-phone-number']"));
            if (!elements.isEmpty()){
                if (elements.get(0).getText().isEmpty() && elements.size() > 1){
                    offer.setAdPhone(elements.get(1).getText());
                }else {
                    offer.setAdPhone(elements.get(0).getText());
                }
            }

            offerService.saveOffer(offer);
            Thread.sleep(500);
        }

        logger.warn("Start collect time: " + startData);
        logger.info("End collect time: " + dataService.currentData());
        logger.info("Collect time: " + dataService.timeBetweenTwoData(start, Instant.now()));
        logger.info("Total number of offer: " + allLinks.size());
        logger.info("Collect time one offer: " + dataService.timePerOneScan(start, Instant.now(), allLinks.size()));

        Thread.sleep(500);
        webDriver.quit();
    }
}
