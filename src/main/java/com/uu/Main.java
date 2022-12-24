package com.uu;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Main {
    static String CHANNEL_URL = "https://www.douyin.com/user/MS4wLjABAAAAaVEJo-_5RLwf-fYKSzYqe92-ENakXTdJiUq_COK0oE0";
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "F:/aa/chromedriver_win32/chromedriver.exe");
        DownloadVideo downloadVideo = new DownloadVideo("https://www.douyin.com/video/7179579691047587105");
        downloadVideo.run();
//        ChromeDriver driver = null;
//        try {
//            ChromeOptions options = new ChromeOptions();
////            options.setHeadless(true);
//            options.addArguments("--window-size=1920,1080", "--mute-audio");
//            driver = new ChromeDriver(options);
//            driver.get(CHANNEL_URL);
//            Thread.sleep(5000L);
//            System.out.println("checking captcha");
//            WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(100L));
//            webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("captcha_container")));
//            System.out.println("now pass captcha");
//            WebDriverWait waitForLoad = new WebDriverWait(driver, Duration.ofSeconds(60L));
//            waitForLoad.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//a[@class='B3AsdZT9 chmb2GX8']")));
////            scrollToBottom(driver);
//            var links = driver.findElements(By.xpath("//a[@class='B3AsdZT9 chmb2GX8']"));
//            for (var e : links) {
//                System.out.println(e.getAttribute("href"));
//            }
//        } finally {
//            if (driver != null) driver.quit();
//        }

    }

    public static void scrollToBottom(WebDriver webDriver) {
        try {
            long lastHeight = (long) ((JavascriptExecutor) webDriver).executeScript("return document.body.scrollHeight");

            while (true) {
                ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(2000);

                long newHeight = (long) ((JavascriptExecutor) webDriver).executeScript("return document.body.scrollHeight");
                if (newHeight == lastHeight) {
                    break;
                }
                lastHeight = newHeight;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}