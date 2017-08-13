package com.ch.ser.strava;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MileageLoader {

    private final String user;
    private final String password;


    public MileageLoader(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public void loadMileage(List<Athlete> athletes) {
        final WebDriver webDriver = new HtmlUnitDriver(true);

        webDriver.get("https://www.strava.com/login");

        webDriver.findElement(By.id("email")).sendKeys("serega4@gmail.com");
        webDriver.findElement(By.id("password")).sendKeys("seraria");
        webDriver.findElement(By.id("login-button")).click();

        for (int i = 0; i < athletes.size(); i++) {
            final Athlete athlete = athletes.get(i);
            athlete.setMileage(loadAthleteMileage(athlete.getId(), webDriver).replace(",",""));
            System.out.println(String.format("%d/%d Done", i + 1, athletes.size()));
        }
    }

    private String loadAthleteMileage(int id, WebDriver webDriver) {
        webDriver.get(String.format("https://www.strava.com/athletes/%d", id));

        new WebDriverWait(webDriver, 10).until((ExpectedCondition<Boolean>) driver ->
                (Boolean) ((JavascriptExecutor) driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);"));
        try {
            Files.write(Paths.get("./logged.html"), webDriver.getPageSource().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final WebElement element = webDriver.findElement(By.xpath("//tbody[@id = 'running-ytd']//tr[1]//td[2]"));
        return element.getAttribute("innerHTML");
    }
}
