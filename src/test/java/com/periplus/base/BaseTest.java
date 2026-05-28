package com.periplus.base;

import com.periplus.utils.ConfigReader;
import com.periplus.utils.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseTest {

    protected WebDriver driver;
    protected String baseUrl;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"baseUrl"})
    public void setUp(@Optional("") String baseUrlParam) {
        this.baseUrl = (baseUrlParam == null || baseUrlParam.isBlank())
                ? ConfigReader.get("baseUrl", "https://www.periplus.com")
                : baseUrlParam;
        this.driver = DriverFactory.create();
        driver.get(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver != null) {
            if (result != null && result.getStatus() == ITestResult.FAILURE) {
                captureFailureArtifacts(result.getMethod().getMethodName());
            }
            driver.quit();
        }
    }

    private void captureFailureArtifacts(String testName) {
        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        try {
            Path dir = Paths.get("target", "failure-artifacts");
            Files.createDirectories(dir);

            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(dir.resolve(testName + "-" + stamp + ".png"), png);

            String html = driver.getPageSource();
            Files.writeString(dir.resolve(testName + "-" + stamp + ".html"), html == null ? "" : html);

            String url = driver.getCurrentUrl();
            Files.writeString(dir.resolve(testName + "-" + stamp + ".url.txt"), url == null ? "" : url);

            System.err.println("Failure artifacts saved under " + dir.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Could not write failure artifacts: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error capturing artifacts: " + e.getMessage());
        }
    }
}
