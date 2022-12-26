package com.uu;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
@Slf4j
public class DownloadVideo implements Callable<SavedData> {
    String link;

    public DownloadVideo(String link) {
        this.link = link;
    }

    public SavedData call() {
        ChromeDriver driver = null;
        log.info("Downloading link {}, {}", Main.downloaded.incrementAndGet(), link);
        try {
            ChromeOptions options = new ChromeOptions();
//            options.setHeadless(true);
            options.addArguments("--window-size=1920,1080", "--mute-audio");
            driver = new ChromeDriver(options);
            driver.get(link);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60L));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("video")));
            var timeE = driver.findElement(By.className("aQoncqRg"));
            String x = timeE.getText();
            var prefix = new SimpleDateFormat("yyMMdd_HHmm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(x.substring(x.length() - 16)));
            var elem = driver.findElements(By.tagName("video"));
            List<String> url = new ArrayList<>();
            for (var e : elem) {
                var e1 = e.findElements(By.tagName("source"));
                for (var e2 : e1) url.add(e2.getAttribute("src"));
            }
            return new SavedData(link, getFile(prefix, url));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new SavedData(link, null);
        }
        finally {
            if (driver != null) driver.quit();
        }
    }

    private String getFile(String prefix, List<String> urls) {
        String filename = prefix + "_" + RandomStringUtils.randomAlphanumeric(8) + ".mp4";
        log.info(link + " - " + filename);
        for (String url : urls) {
            try {
                FileUtils.copyURLToFile(new URL(url), new File(Main.savedLoc + filename));
                return filename;
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }
}
