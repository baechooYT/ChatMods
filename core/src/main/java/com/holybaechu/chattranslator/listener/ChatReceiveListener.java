package com.holybaechu.chattranslator.listener;

import com.holybaechu.chattranslator.ChatTranslatorAddon;
import com.holybaechu.chattranslator.ChatTranslatorConfiguration;
import com.holybaechu.chattranslator.misc.Language;
import com.holybaechu.chattranslator.translators.BaseTranslator;
import com.holybaechu.chattranslator.translators.GoogleTranslator;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.mojang.GameProfile;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.logging.Logging;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatReceiveListener {
  private final ChatTranslatorConfiguration config;
  private static BaseTranslator translator = new GoogleTranslator();
  private static Language targetLanguage = Language.EN;

  private final Pattern mcDefaultRegex = Pattern.compile("^(\\<.*\\> )(.*)");
  private final Pattern hypixelDefaultRegex = Pattern.compile("^(.*\\: )(.*)");

  public ChatReceiveListener(ChatTranslatorAddon addon) {
    this.config = addon.configuration();
  }

  public static void setTranslator(BaseTranslator newTranslator) {
    translator = newTranslator;
  }

  public static void setTargetLanguage(Language newLanguage) {
    targetLanguage = newLanguage;
  }

  private String getGroupFromRegex(Pattern regex, String input, int n) {
    if (input.matches(regex.pattern())) {
      Matcher m = regex.matcher(input);
      m.find();
      return m.group(n);
    }
    return null;
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    ChatMessage message = event.chatMessage();
    GameProfile sender = message.getSenderProfile();
    String messageStr = message.getFormattedText();

    String finalMessageStr = messageStr;
    if(sender != null) {
      if (getGroupFromRegex(mcDefaultRegex, finalMessageStr, 2) != null){
        finalMessageStr = getGroupFromRegex(mcDefaultRegex, finalMessageStr, 2);
      } else if (getGroupFromRegex(hypixelDefaultRegex, finalMessageStr, 2) != null) {
        finalMessageStr = getGroupFromRegex(hypixelDefaultRegex, finalMessageStr, 2);
      }
    } else if (config.doNotTranslateNonPlayerMessages().get()) {
      return;
    }

    String finalMessageStr1 = finalMessageStr;
    Task.builder(() -> {
      try {
        String finalTranslation = translator.translate("auto", String.valueOf(targetLanguage), finalMessageStr1);

        if (sender != null) {
          if (getGroupFromRegex(mcDefaultRegex, messageStr, 1) != null) {
            finalTranslation = getGroupFromRegex(mcDefaultRegex, messageStr, 1) + finalTranslation;
          } else if (getGroupFromRegex(hypixelDefaultRegex, messageStr, 1) != null) {
            finalTranslation = getGroupFromRegex(hypixelDefaultRegex, messageStr, 1) + finalTranslation;
          }
        }

        String finalTranslation1 = finalTranslation;
        Task.builder(() -> message.edit(Component.text(finalTranslation1))).build().executeOnRenderThread();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).build().execute();
  }
}
