package com.ch.ser.strava;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MembersApiLoader implements MembersLoader {

    private static final String URL = "https://www.strava.com/api/v3/clubs/%s/members?per_page=200";
    private static final String AUTH = "Bearer %s";

    private final String clubId;
    private final String publicToken;

    public MembersApiLoader(String clubId, String publicToken) {
        this.clubId = clubId;
        this.publicToken = publicToken;
    }

    @Override
    public List<Athlete> load() {
        final HttpResponse<JsonNode> response;
        try {
            response = Unirest.get(String.format(URL, clubId))
                    .header("Authorization", String.format(AUTH, publicToken))
                    .asJson();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        final JSONArray members = response.getBody().getArray();
        return StreamSupport.stream(members.spliterator(), false)
                .map(obj -> (JSONObject) obj)
                .map(Athlete::new)
                .collect(Collectors.toList());
    }
}
