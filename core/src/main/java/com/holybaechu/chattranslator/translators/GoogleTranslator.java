package com.holybaechu.chattranslator.translators;

import com.google.gson.Gson;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class GoogleTranslator extends BaseTranslator {

  private final Gson gson = new Gson();

  public String translate(String sourceLang, String targetLanguage, String input) throws Exception {
    String urlString = String.format(
        "https://%s&sl=%s&tl=%s&q=%s",
        "translate.googleapis.com/translate_a/single?client=gtx&dt=t&dj=1&source=input",
        URLEncoder.encode(sourceLang, StandardCharsets.UTF_8),
        URLEncoder.encode(targetLanguage, StandardCharsets.UTF_8),
        URLEncoder.encode(input, StandardCharsets.UTF_8)
    );

    Response response = gson.fromJson(getResponse(urlString), Response.class);
    List<Map<String, String>> sentences = response.sentences;

    if (sentences != null && !sentences.isEmpty()) {
      StringBuilder translation = new StringBuilder();
      for(Map<String, String> sentence : sentences) {
        if(sentence.get("trans") == null) continue;
        translation.append(sentence.get("trans"));
      }
      return translation.toString();
    } else {
      throw new Exception("Translation not found in the response.");
    }
  }

  private static class Response {
    public List<Map<String, String>> sentences;
  }
}
