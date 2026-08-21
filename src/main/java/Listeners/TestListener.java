package Listeners;

import Utils.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("Starting suite: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("--> {}", describe(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("PASSED  {}", describe(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("FAILED  {}", describe(result));
        if (result.getThrowable() != null) {
            log.error("        {}", result.getThrowable().getMessage());
        }
        attachEvidence();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("SKIPPED {}", describe(result));
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Finished suite: {} — passed {}, failed {}, skipped {}",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    private String describe(ITestResult result) {
        Object[] parameters = result.getParameters();
        String name = result.getMethod().getMethodName();
        if (parameters == null || parameters.length == 0) {
            return name;
        }
        StringBuilder sb = new StringBuilder(name).append(" [");
        for (int i = 0; i < parameters.length; i++) {
            sb.append(i > 0 ? ", " : "").append(parameters[i]);
        }
        return sb.append(']').toString();
    }

    private void attachEvidence() {
        WebDriver driver = DriverManager.currentDriver();
        if (driver == null) {
            return;
        }
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot on failure", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");
            Allure.addAttachment("Page source on failure", "text/html",
                    driver.getPageSource(), ".html");
        } catch (Exception e) {
            log.warn("Could not capture failure evidence: {}", e.getMessage());
        }
    }
}
