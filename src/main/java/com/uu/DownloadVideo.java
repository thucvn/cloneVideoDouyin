package com.uu;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DownloadVideo {
    String link;

    public DownloadVideo(String link) {
        this.link = link;
    }

    public void run() {
        ChromeDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
//            options.setHeadless(true);
            options.addArguments("--window-size=1920,1080", "--mute-audio");
            driver = new ChromeDriver(options);
            driver.get(link);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60L));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("video")));
            var elem = driver.findElements(By.tagName("video"));
            for (var e : elem) {
                var e1 = e.findElements(By.tagName("source"));
                System.out.println(e1.get(0).getAttribute("src"));
            }
        } finally {
            if (driver != null) driver.quit();
        }
    }
}
