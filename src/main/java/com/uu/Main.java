package com.uu;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Main {
    static String CHANNEL_URL = "https://www.douyin.com/user/MS4wLjABAAAAaVEJo-_5RLwf-fYKSzYqe92-ENakXTdJiUq_COK0oE0"; // config the url of channel
    public static String savedLoc = "F:/savedVideo/"; //config the saved location of videos downloaded
    static int THREAD_NUM = 5; //config the number of parallel downloading videos
    public static AtomicInteger downloaded = new AtomicInteger(0);
    static Set<String> checked = new HashSet<>();
    static int MAX_DOWNLOAD_VIDEOS = 10; // max link download

    public static void main(String[] args) throws InterruptedException, IOException {
        // set location of driver
        System.setProperty("webdriver.chrome.driver", "F:/aa/chromedriver_win32/chromedriver.exe");
//        System.setProperty("webdriver.chrome.driver", "F:/chromedriver.exe");
        File downloaded = new File("downloaded.txt");
        File downloadError = new File("download_error.txt");
        if (!downloadError.exists()) downloadError.createNewFile();
        if (!downloaded.exists()) downloaded.createNewFile();
        readFile(downloaded);
//        readFile(downloadError); // comment this line if you want to re-download error links
        ChromeDriver driver = null;
        List<String> linksGet = new ArrayList<>();
        try {
            ChromeOptions options = new ChromeOptions();
//            options.setHeadless(true);
            options.addArguments("--window-size=1920,1080", "--mute-audio");
            driver = new ChromeDriver(options);
            driver.get(CHANNEL_URL);
            Thread.sleep(5000L);
            log.warn("checking captcha");
            WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(100L));
            webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("captcha_container")));
            log.info("now pass captcha");
            WebDriverWait waitForLoad = new WebDriverWait(driver, Duration.ofSeconds(60L));
            waitForLoad.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//a[@class='B3AsdZT9 chmb2GX8']")));
//            scrollToBottom(driver); // this will scroll to bottom in order to get all links of channel
            var links = driver.findElements(By.xpath("//a[@class='B3AsdZT9 chmb2GX8']"));
            for (var e : links) {
                String url = e.getAttribute("href");
                if (!checked.contains(url)) linksGet.add(url);
            }
            log.info("Now download {} links", linksGet.size());
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM);
            List<Future<SavedData>> list = new ArrayList<>();
            int i = 0;
            for(String url : linksGet) {
                i++;
                if (i >= MAX_DOWNLOAD_VIDEOS) break;
                Future<SavedData> future = executor.submit(new DownloadVideo(url));
                list.add(future);
            }
            executor.shutdown();
            List<SavedData> success = new ArrayList<>();
            List<SavedData> errorData = new ArrayList<>();
            for(Future<SavedData> fut : list){
                try {
                    var got = fut.get();
                    if (got.fileName != null) {
                        success.add(got);
                    } else errorData.add(got);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            // save the download info to file
            savedToFile(downloaded, success);
            savedToFile(downloadError, errorData);
        } finally {
            if (driver != null) driver.quit();
        }
    }

    private static void readFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine())!= null) {
                checked.add(line.split(",")[0]);
            }
        } catch (IOException ignored) {
        }
    }

    private static void savedToFile(File file, List<SavedData> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (var v : list) {
                writer.append(v.toString()).append("\n");
            }
        } catch (IOException ignored) {
        }
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