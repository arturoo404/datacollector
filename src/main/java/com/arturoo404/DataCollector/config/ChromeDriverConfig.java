package com.arturoo404.DataCollector.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChromeDriverConfig {

    //https://www.youtube.com/watch?v=PF0iyeDmu9E&ab_channel=ShaneLee
    @Bean
    public WebDriver chromeDriver(){
        ChromeOptions co = new ChromeOptions();
        co.addArguments("--no-sandbox");
        co.addArguments("--window-size=1920,1080");
        co.addArguments("--start-maximized");
        co.addArguments("--disable-dev-shm-usage");
        co.addArguments("--blink-settings=imagesEnabled=false");
        co.addArguments("--user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 " + "(KHTML, like Gecko) Chrome/60.0.3112.50 Safari/537.36");
        return WebDriverManager.chromedriver().capabilities(co).create();
    }
}
