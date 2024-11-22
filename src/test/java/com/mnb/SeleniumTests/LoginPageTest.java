package com.mnb.SeleniumTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class) // Use Spring Test Context
public class LoginPageTest {

	@Autowired
	private WebDriver driver; // Autowire the WebDriver bean from the configuration

	@Test
	public void testLoginPage() {
		try {
			// Navigate to the login page
			driver.get("http://localhost:8083/login"); // Replace with your login URL

			// Find username input and enter data
			WebElement usernameInput = driver.findElement(By.name("username"));
			usernameInput.sendKeys("sirine");

			// Find password input and enter data
			WebElement passwordInput = driver.findElement(By.name("password"));
			passwordInput.sendKeys("test123");

			// Submit the login form
			WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
			loginButton.click();

			// Wait for the response (example with a small sleep, use WebDriverWait in
			// production)
			Thread.sleep(2000);

			// Assert successful login by checking the redirected page or some element on it
			// Replace with a proper check for your app (e.g., a greeting message,
			// dashboard, etc.)
			String currentUrl = driver.getCurrentUrl();
			System.out.println(driver.getCurrentUrl());

			assertTrue(currentUrl.equals("http://localhost:8083/"),
					"Login failed or didn't redirect to the expected page.");

			System.out.println("Login test passed successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}