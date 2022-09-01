package me.imshy.scraper.domain.http;

public interface HttpClient {

  Response fetch(JsonPostRequest jsonPostRequest);

}
