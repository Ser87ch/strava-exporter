package com.ch.ser.strava;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class MembersCsvLoader implements MembersLoader {

    private final String file;

    public MembersCsvLoader(String file) {
        this.file = file;
    }

    @Override
    public List<Athlete> load() {
        try {
            return Files.lines(Paths.get(file))
                    .map(this::read)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Athlete read(String line) {
        final String[] s = line.split(",");
        return new Athlete(Integer.parseInt(s[3]), s[0], s[1]);
    }
}
