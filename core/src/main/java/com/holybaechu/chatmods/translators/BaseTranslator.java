package com.holybaechu.chatmods.translators;

import com.holybaechu.chatmods.misc.TranslationPlatform;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaseTranslator {

  public static String getResponse(String urlToRead) throws Exception {
    StringBuilder result = new StringBuilder();
    URL url = new URL(urlToRead);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(conn.getInputStream()))) {
      for (String line; (line = reader.readLine()) != null; ) {
        result.append(line);
      }
    }
    return result.toString();
  }

  public String translate(TranslationPlatform platform, String sourceLang, String targetLanguage, String input)
      throws Exception {
    return "Translation";
  }
}