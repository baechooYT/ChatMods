package com.holybaechu.chatmods.translators;

import com.google.gson.Gson;
import com.holybaechu.chatmods.misc.TranslationPlatform;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class GoogleTranslator extends BaseTranslator {
  public String translate(TranslationPlatform platform, String sourceLang, String targetLanguage, String input)
      throws Exception {
    String UrlString = "https://";

      UrlString+="translate.googleapis.com/translate_a/single?client=gtx&dt=t&dj=1&source=input";

    UrlString+="&sl="+ URLEncoder.encode("auto", "UTF-8");
    UrlString+="&tl="+URLEncoder.encode(targetLanguage, "UTF-8");
    UrlString+="&q="+URLEncoder.encode(input, "UTF-8");

    Gson gson = new Gson();
    Map<String, Object> responseMap = gson.fromJson(getResponse(UrlString), Map.class);

    List<Map<String, String>> sentences = (List<Map<String, String>>) responseMap.get("sentences");
    if (sentences != null && !sentences.isEmpty()) {
      return sentences.get(0).get("trans");
    } else {
      throw new Exception("Translation not found in the response.");
    }
  }
}
