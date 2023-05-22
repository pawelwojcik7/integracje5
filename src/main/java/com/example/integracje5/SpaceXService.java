package com.example.integracje5;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SpaceXService {

    private static final String SPACEX_API_URL = "https://api.spacexdata.com/v4/launches";

    private final RestTemplate restTemplate;

    public Launch[] getLaunches() {
        ResponseEntity<Launch[]> response = restTemplate.getForEntity(SPACEX_API_URL, Launch[].class);
        return response.getBody();
    }

    public Launch getLaunchById(String id) {
        ResponseEntity<Launch> response = restTemplate.getForEntity(SPACEX_API_URL + "/" + id, Launch.class);
        return response.getBody();
    }
}
