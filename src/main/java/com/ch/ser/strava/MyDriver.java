package com.ch.ser.strava;

import com.gargoylesoftware.htmlunit.WebClient;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class MyDriver extends HtmlUnitDriver {
    public MyDriver() {
        super(true);
    }

    @Override
    protected WebClient modifyWebClient(WebClient client) {
        final WebClient webClient = super.modifyWebClient(client);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        return webClient;
    }
}
