package com.applitools.quickstarts;

import static com.google.common.base.Strings.isNullOrEmpty;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Runs Applitools test for the demo app https://demo.applitools.com
 */
@RunWith(JUnit4.class)
public class BasicDemo {

	private EyesRunner runner;
	private Eyes eyes;
	private static BatchInfo batch;
	private WebDriver driver;
	private Configuration suiteConfig;
	private LogHandler logHandler;

	@BeforeClass
	public static void setBatch() {
		// Must be before ALL tests (at Class-level)
		batch = new BatchInfo("Demo batch");
	}

	@Before
	public void beforeEach() {
		// Initialize the Runner for your test.
		runner = new VisualGridRunner(10);

		suiteConfig = new Configuration();
		suiteConfig.addBrowser(900, 600, BrowserType.CHROME);

		// Initialize the eyes SDK
		eyes = new Eyes(runner);
		eyes.setLogHandler(new StdoutLogHandler(true));
		eyes.setConfiguration(suiteConfig);

		// Raise an error if no API Key has been found.
		if(isNullOrEmpty(System.getenv("APPLITOOLS_API_KEY"))) {
		    throw new RuntimeException("No API Key found; Please set environment variable 'APPLITOOLS_API_KEY'.");
		}

		// Set your personal Applitols API Key from your environment variables.
		eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

		// set batch name
		eyes.setBatch(batch);

		// Use Chrome browser
		driver = new ChromeDriver();

	}

	@Test
	public void basicTest() {
		// Set AUT's name, test name and viewport size (width X height)
		// We have set it to 800 x 600 to accommodate various screens. Feel free to
		// change it.
		eyes.open(driver, "Demo App", "Smoke Test vg", new RectangleSize(800, 800));

		// Navigate the browser to the "ACME" demo app.
		driver.get("https://demo.applitools.com");

		eyes.checkWindow("App window");
		// WebElement el = driver.findElement(By.id("log-in"));
		// eyes.check("App Window", Target.region(el));
		eyes.closeAsync();
	}

	@After
	public void afterEach() {
		// Close the browser.
		driver.quit();

		// If the test was aborted before eyes.close was called, ends the test as
		// aborted.
		eyes.abortIfNotClosed();

		// Wait and collect all test results
		TestResultsSummary allTestResults = runner.getAllTestResults();

		// Print results
		System.out.println(allTestResults);
	}
}
