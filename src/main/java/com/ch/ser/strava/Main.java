package com.ch.ser.strava;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length != 3) {
            throw new RuntimeException("Should be provided following arguments: email, password, file name");
        }

        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
//        final String publicToken = args.length == 4 ? args[3] : "62ee9e8b017042d482ed933930c6b4b5b58e9280";
        final MembersLoader membersLoader = new MembersCsvLoader(args[2]);
//        final MembersLoader membersLoader = new MembersApiLoader(args[0], publicToken);

        final List<Athlete> athletes = membersLoader.load();

        final MileageLoader mileageLoader = new MileageLoader(args[0], args[1]);
        mileageLoader.loadMileage(athletes);
        athletes.forEach(System.out::println);

        final List<String> athleteDesc = athletes.stream()
                .map(Athlete::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get("result.csv"), athleteDesc);
    }
}
