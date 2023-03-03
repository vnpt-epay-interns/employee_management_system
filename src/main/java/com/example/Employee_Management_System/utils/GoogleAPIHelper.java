package com.example.Employee_Management_System.utils;

import com.example.Employee_Management_System.model.AccessTokenResponse;
import com.example.Employee_Management_System.model.GoogleUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GoogleAPIHelper {



        private static final String CLIENT_ID = "828625326282-k8fksboqf7e2q62j8i4s3o0kne477ovi.apps.googleusercontent.com";
        private static final String CLIENT_SECRET = "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";
        private static final String REDIRECT_URI = "http://127.0.0.1:5173";
        private static final String TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
        private static final String PEOPLE_API_BASE_URL = "https://people.googleapis.com/v1/people/me";
        private static final String PROFILE_FIELDS = "names,emailAddresses,photos";

        public static GoogleUserInfo getUserInfo(String authorizationCode) {
            // Exchange authorization code for access token
            AccessTokenResponse tokenResponse = exchangeAuthorizationCodeForToken(authorizationCode);
            GoogleUserInfo googleUserInfo = getUserInfoUsingAccessToken(tokenResponse.getAccessToken());
            return googleUserInfo;
        }

        public static AccessTokenResponse exchangeAuthorizationCodeForToken(String authorizationCode) {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", authorizationCode);
            params.add("client_id", CLIENT_ID);
            params.add("client_secret", CLIENT_SECRET);
            params.add("redirect_uri", REDIRECT_URI);
            params.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<AccessTokenResponse> response = restTemplate.postForEntity(TOKEN_ENDPOINT, request, AccessTokenResponse.class);
            return response.getBody();
        }



        private static GoogleUserInfo getUserInfoUsingAccessToken(String accessToken) {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(PEOPLE_API_BASE_URL)
                    .queryParam("personFields", PROFILE_FIELDS);

            HttpEntity<?> request = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    builder.build().toUri(),
                    HttpMethod.GET,
                    request,
                    Map.class);


            Map<String, Object> names = getTheResponseField(response, "names");
            String lastName = names.get("familyName").toString();
            String firstName = names.get("givenName").toString();

            Map<String, Object> emailAddresses = getTheResponseField(response, "emailAddresses");
            String email = emailAddresses.get("value").toString();

            Map<String, Object> photos = getTheResponseField(response, "photos");
            String photo = photos.get("url").toString();



            return GoogleUserInfo.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .avatar(photo)
                    .build();
        }

        private static Map<String, Object> getTheResponseField(ResponseEntity<Map> response, String fieldName) {
            List<Object> valuesOfField = (List<Object>) response.getBody().get(fieldName);
            return (Map<String, Object>) valuesOfField.get(0); // there is only one value that the Google API returns
        }

}
