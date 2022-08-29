package me.imshy.scraper.domain.http;

import me.imshy.scraper.domain.http.request.JsonPostRequest;
import me.imshy.scraper.domain.http.request.Response;

public interface HttpClient {

  Response fetch(JsonPostRequest jsonPostRequest);

}
