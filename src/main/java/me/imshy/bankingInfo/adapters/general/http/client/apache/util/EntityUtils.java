package me.imshy.bankingInfo.adapters.general.http.client.apache.util;

import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class EntityUtils {
  public static String toString(HttpEntity httpEntity) {
    try {
      return org.apache.hc.core5.http.io.entity.EntityUtils.toString(httpEntity);
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Parsing HTTP Body to String failed");
  }
}
