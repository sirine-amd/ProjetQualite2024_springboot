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
public class LoginPageFailedTest {

	@Autowired
	private WebDriver driver; // Autowire the WebDriver bean from the configuration

	@Test
	public void testInvalidLogin() {
		try {
			driver.get("http://localhost:8083/login");

			WebElement usernameInput = driver.findElement(By.name("username"));
			usernameInput.sendKeys("invalid_user");

			WebElement passwordInput = driver.findElement(By.name("password"));
			passwordInput.sendKeys("wrong_password");

			WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
			loginButton.click();

			// Wait for response
			Thread.sleep(2000);

			// Assert error message
			WebElement errorAlert = driver.findElement(By.className("alert-danger"));
			assertTrue(errorAlert.isDisplayed(), "Error alert not displayed for invalid credentials.");

			System.out.println("Invalid login test passed!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();
		}
	}
}
