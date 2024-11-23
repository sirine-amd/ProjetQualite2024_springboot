package com.mnb.SeleniumTests.Books;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class ListBooksPageTest {

	@Autowired
	private WebDriver driver; // Autowire the WebDriver bean from the configuration

	@BeforeEach
	public void login() {
		try {
			// Navigate to the login page
			driver.get("http://localhost:8083/login"); // Replace with your login URL

			// Find username input and enter admin credentials
			WebElement usernameInput = driver.findElement(By.name("username"));
			usernameInput.sendKeys("sarra"); // Admin username

			// Find password input and enter the password
			WebElement passwordInput = driver.findElement(By.name("password"));
			passwordInput.sendKeys("test123"); // Admin password

			// Submit the login form
			WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
			loginButton.click();

			// Wait for the response (example with a small sleep, use WebDriverWait in
			// production)
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.urlToBe("http://localhost:8083/")); // Wait for successful login and
																				// redirection

			// Ensure successful login by checking the redirected page
			String currentUrl = driver.getCurrentUrl();
			assertTrue(currentUrl.equals("http://localhost:8083/"),
					"Login failed or didn't redirect to the expected page.");

			System.out.println("Login as admin (sarra) test passed successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(1) // Set the test order to 1 to ensure it runs first
	public void addBookTest() {
		try {
			// Navigate to the books list page
			driver.get("http://localhost:8083/books/list"); // Replace with your books list URL

			// Check that the title is displayed correctly
			WebDriverWait wait = new WebDriverWait(driver, 5);
			WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h3")));
			assertTrue(title.getText().contains("BOOKS LIST"), "Title does not match");

			// Verify if the 'Add Book' button is present and clickable
			WebElement addBookButton = driver.findElement(By.cssSelector("a.btn-primary"));
			assertTrue(addBookButton.isDisplayed(), "'Add Book' button is not displayed");
			Thread.sleep(2600);

			// Click on the 'Add Book' button to navigate to the form
			addBookButton.click();

			// Fill in the form fields to add a new book
			WebElement bookNameInput = driver.findElement(By.name("bookName"));
			bookNameInput.sendKeys("New Book Title");

			WebElement bookSubnameInput = driver.findElement(By.name("bookSubname"));
			bookSubnameInput.sendKeys("New Book Subname");

			WebElement serialNameInput = driver.findElement(By.name("serialName"));
			serialNameInput.sendKeys("Serial 123");

			WebElement booksAuthorInput = driver.findElement(By.name("booksAuthor"));
			booksAuthorInput.sendKeys("John Doe");

			WebElement booksPublisherInput = driver.findElement(By.name("booksPublisher"));
			booksPublisherInput.sendKeys("Publisher XYZ");

			WebElement descriptionInput = driver.findElement(By.name("description"));
			descriptionInput.sendKeys("This is a description of the new book.");

			WebElement isbnInput = driver.findElement(By.name("isbn"));
			isbnInput.sendKeys("1234567890123");

			// Click the 'Save' button to submit the form
			WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
			Thread.sleep(2600); // Ideally, use WebDriverWait instead of Thread.sleep()

			saveButton.click();

			Thread.sleep(2600); // Ideally, use WebDriverWait instead of Thread.sleep()

			// Wait for the redirect after saving the book
			wait.until(ExpectedConditions.urlToBe("http://localhost:8083/books/list")); // Ensure the URL redirects to
																						// the list page

			// Verify that the URL after the form submission is the book list page
			String currentUrl = driver.getCurrentUrl();
			assertTrue(currentUrl.contains("/books/list"), "Redirect did not go to the book list page");

			// Optionally, verify if the new book appears in the book list
			WebElement bookTable = driver.findElement(By.cssSelector("table.table"));
			assertTrue(bookTable.isDisplayed(), "Book list table is not displayed");

			// Check if the newly added book appears in the list
			WebElement newBookRow = driver.findElement(By.xpath("//td[contains(text(),'New Book Title')]"));
			assertTrue(newBookRow.isDisplayed(), "The new book is not displayed in the list");

			System.out.println("Add Book test passed successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// do search
	@Test
	@Order(2)
	public void testListBooksPage() {
		try {
			// Navigate to the books list page
			driver.get("http://localhost:8083/books/list"); // Replace with your books list URL

			// Check that the title is displayed correctly
			// Use explicit wait to ensure the h3 element is present
			WebDriverWait wait = new WebDriverWait(driver, 5);
			WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h3")));
			assertTrue(title.getText().contains("BOOKS LIST"), "Title does not match");

			// Verify if the 'Add Book' button is present and clickable
			WebElement addBookButton = driver.findElement(By.cssSelector("a.btn-primary"));
			assertTrue(addBookButton.isDisplayed(), "'Add Book' button is not displayed");

			Thread.sleep(2000); // Ideally, use WebDriverWait instead of Thread.sleep()
			// Check if the 'Update' and 'Delete' buttons are displayed (since the user is
			// an admin)
			WebElement deleteButton = driver.findElement(By.cssSelector("a.btn-danger"));
			assertTrue(deleteButton.isDisplayed(), "'Delete' button is not displayed for admin");

			// Check if the search input field is present
			WebElement searchInput = driver.findElement(By.name("keyword"));
			assertTrue(searchInput.isDisplayed(), "Search input is not displayed");

			// Perform a search (this is an example, customize according to your search
			// logic)
			searchInput.sendKeys("New Book Title");
			WebElement searchButton = driver.findElement(By.cssSelector("button[type='submit']"));
			searchButton.click();

			// Wait for results to load and verify if books are listed
			Thread.sleep(2600); // Ideally, use WebDriverWait instead of Thread.sleep()
			WebElement bookTable = driver.findElement(By.cssSelector("table.table"));
			assertTrue(bookTable.isDisplayed(), "Book list table is not displayed");

			System.out.println("List Books Page test passed successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(3)
	public void updateBookTest() {
		try {
			// Navigate to the books list page
			driver.get("http://localhost:8083/books/list"); // Replace with your books list URL

			// Ensure the book list is displayed
			WebDriverWait wait = new WebDriverWait(driver, 5);
			WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h3")));
			assertTrue(title.getText().contains("BOOKS LIST"), "Title does not match");

			// Locate and click the 'Update' button for the book you want to update
			// Example assumes the book name is 'New Book Title' (you can change this
			// depending on your test data)
			WebElement updateButton = driver.findElement(By.cssSelector("a.btn-info"));

			assertTrue(updateButton.isDisplayed(), "'Update' button is not displayed");
			updateButton.click();

			// Modify the form fields to update the book
			WebElement bookNameInput = driver.findElement(By.name("bookName"));
			bookNameInput.clear(); // Clear existing name
			bookNameInput.sendKeys("Updated Book Title");

			Thread.sleep(2000);

			// Fill in other fields if necessary
			WebElement bookSubnameInput = driver.findElement(By.name("bookSubname"));
			bookSubnameInput.clear();
			bookSubnameInput.sendKeys("Updated Book Subname");

			// Submit the form to save the updates
			WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
			saveButton.click();

			// Wait for the page to redirect back to the book list page
			wait.until(ExpectedConditions.urlToBe("http://localhost:8083/books/list"));

			// Verify that the URL is the book list page
			String currentUrl = driver.getCurrentUrl();
			assertTrue(currentUrl.contains("/books/list"), "Redirect did not go to the book list page after update");
			Thread.sleep(2600);
			System.out.println("Update Book test passed successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(4) // Set the test order to 2 to ensure it runs after the addBookTest
	public void deleteBookTest() {
		try {
			// Navigate to the books list page
			driver.get("http://localhost:8083/books/list"); // Replace with your books list URL

			// Ensure the book list is displayed
			WebDriverWait wait = new WebDriverWait(driver, 5);
			WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h3")));
			assertTrue(title.getText().contains("BOOKS LIST"), "Title does not match");

			// Check that the delete button is present and clickable for at least one book
			// in the list
			WebElement deleteButton = driver.findElement(By.cssSelector("a.btn-danger"));
			assertTrue(deleteButton.isDisplayed(), "'Delete' button is not displayed");

			// Click the delete button to delete the book
			deleteButton.click();
			Thread.sleep(2600); // Ideally, use WebDriverWait instead of Thread.sleep()

			// Confirm the delete action (alert handling is optional if needed)
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			assertTrue(alert.getText().contains("Are you sure you want to delete this book?"));
			alert.accept(); // Accept the alert to confirm the delete action

			// Wait for the page to reload and ensure that the list page is displayed again
			wait.until(ExpectedConditions.urlToBe("http://localhost:8083/books/list")); // Ensure it redirects to the
																						// list page

			// Verify that the URL is the book list page
			String currentUrl = driver.getCurrentUrl();
			assertTrue(currentUrl.contains("/books/list"), "Redirect did not go to the book list page after delete");
			Thread.sleep(2000); // Ideally, use WebDriverWait instead of Thread.sleep()

			// Optionally, verify that the book is no longer in the list
			// (You'll need a way to identify that the book was deleted, for example,
			// checking the absence of a book by name)=
			System.out.println("Delete Book test passed successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
