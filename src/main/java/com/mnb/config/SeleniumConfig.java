package com.mnb.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {

	@Bean
	public WebDriver webDriver() {
		// Path to the WebDriver executable
		System.setProperty("webdriver.chrome.driver", "D:/chromedriver-win64/chromedriver.exe");
		return new ChromeDriver(); // Or another driver like FirefoxDriver
	}
}
