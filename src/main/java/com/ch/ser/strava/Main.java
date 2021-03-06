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
        if (args.length != 4) {
            throw new RuntimeException("Should be provided following arguments: email, password, file name, pause");
        }

        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);

        final MembersLoader membersLoader = new MembersCsvLoader(args[2]);
        final List<Athlete> athletes = membersLoader.load();

        final MileageLoader mileageLoader = new MileageLoader(args[0], args[1], args[3]);
        mileageLoader.loadMileage(athletes);
        athletes.forEach(System.out::println);

        final List<String> athleteDesc = athletes.stream()
                .map(Athlete::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get("result.csv"), athleteDesc);
    }
}
