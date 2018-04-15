package com.ch.ser.strava;

import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MileageLoader {
    private static final int NEW_DRIVER_COUNT = 10;

    private final String user;
    private final String password;


    public MileageLoader(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public void loadMileage(List<Athlete> athletes) {
        WebDriver webDriver = null;
        for (int i = 0; i < athletes.size(); i++) {
            if (i % NEW_DRIVER_COUNT == 0) {
                webDriver = getWebDriver();
            }
            final Athlete athlete = athletes.get(i);
            System.out.println("Loading " + athlete.getFirstName() + " " + athlete.getSecondName());
            athlete.setMileage(loadAthleteMileage(athlete.getId(), webDriver).replace(",", ""));
            System.out.println(String.format("%d/%d Done", i + 1, athletes.size()));
        }
    }

    private WebDriver getWebDriver() {
        final WebDriver webDriver = new HtmlUnitDriver(true);
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        System.out.println("logging");
        webDriver.get("https://www.strava.com/login");

        webDriver.findElement(By.id("email")).sendKeys(user);
        webDriver.findElement(By.id("password")).sendKeys(password);
        webDriver.findElement(By.id("login-button")).click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("logged");
        return webDriver;
    }

    private String loadAthleteMileage(int id, WebDriver webDriver) {
        webDriver.get(String.format("https://www.strava.com/athletes/%d", id));

        new WebDriverWait(webDriver, 10).until((ExpectedCondition<Boolean>) driver ->
                (Boolean) ((JavascriptExecutor) driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);"));

        try {
            final WebElement element = webDriver.findElement(By.xpath("//tbody[@id = 'running-ytd']//tr[1]//td[2]"));
            return element.getAttribute("innerHTML");
        } catch (NoSuchElementException e) {
            try {
                final byte[] pageSourceBytes = webDriver.getPageSource().getBytes();
                final String fileName = "user" + id + ".html";
                Files.write(Paths.get(fileName), pageSourceBytes);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return "Mileage not found";
        }
    }
}
