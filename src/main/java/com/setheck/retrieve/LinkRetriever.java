package com.setheck.retrieve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by seth.thompson on 3/24/17.
 */

public class LinkRetriever
{
    private static final Logger log = LoggerFactory.getLogger(LinkRetriever.class);

    public static String getLink(String url)
    {
        log.info("Attempting to retrieve: {}", url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;

        String result = "";
        try
        {
            response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful())
            {
                log.info("Success! retrieved: {}", url);
            }
            else if (response.getStatusCode().is3xxRedirection())
            {
                log.info("Following redirect...");
                HttpHeaders responseHeaders = response.getHeaders();
                return LinkRetriever.getLink(responseHeaders.getLocation().toString());
            }

            result = response.getBody();
        }
        catch(RestClientException rce)
        {
            log.error("Failed to request {}, Error: {}", url, rce.getMessage());
        }
        return result;
    }

    public static RetrievedFile getFile(String url)
    {
        log.info("Attempting to retrieve File: {}", url);
        RestTemplate restTemplate = new RestTemplate();

        RetrievedFile result = new RetrievedFile();
        try
        {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

            if (response.getStatusCode().is2xxSuccessful())
            {
                log.info("Success! retrieved: {}", url);
            }
            else if (response.getStatusCode().is3xxRedirection())
            {
                log.info("Following redirect...");
                HttpHeaders responseHeaders = response.getHeaders();
                return LinkRetriever.getFile(responseHeaders.getLocation().toString());
            }

            int lastSlash = url.lastIndexOf("/");
            String fileName = url.substring(lastSlash + 1);
            result = new RetrievedFile(fileName, response.getBody());
        }
        catch(RestClientException rce)
        {
            log.error("Failed to request {}, Error: {}", url, rce.getMessage());
        }

        return result;
    }
}
