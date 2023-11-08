package com.holybaechu.chattranslator.listener;

import com.holybaechu.chattranslator.ChatTranslatorAddon;
import com.holybaechu.chattranslator.translators.BaseTranslator;
import com.holybaechu.chattranslator.translators.GoogleTranslator;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.mojang.GameProfile;
import net.labymod.api.util.concurrent.task.Task;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatReceiveListener {
  private final ChatTranslatorAddon addon;
  private BaseTranslator translator;
  private String targetLang;

  public ChatReceiveListener(ChatTranslatorAddon addon) {
    this.addon = addon;
  }

  private String getGroupFromRegex(Pattern regex, String input, int n){
    if (input.matches(regex.pattern())){
      Matcher m = regex.matcher(input);
      m.find();
      return m.group(n);
    }
    return null;
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) throws Exception {
    switch (addon.configuration().translationPlatform().get()){
      case google:
        this.translator = new GoogleTranslator();
        break;
      default:
        this.translator = new GoogleTranslator();
        break;
    }
    this.targetLang = addon.configuration().targetLanguage().get().name().replace("_", "-");

    if (!addon.configuration().enabled().get()) return;

    ChatMessage message = event.chatMessage();
    GameProfile sender = message.getSenderProfile();
    String messageStr = message.getFormattedText();

    Pattern mcDefaultRegex = Pattern.compile("^(\\<.*\\> )(.*)");
    Pattern hypixelDefaultRegex = Pattern.compile("^(.*\\: )(.*)");

    String finalMessageStr = messageStr;
    if(sender != null){
      if (getGroupFromRegex(mcDefaultRegex, finalMessageStr, 2) != null){
        finalMessageStr = getGroupFromRegex(mcDefaultRegex, finalMessageStr, 2);
      }else if (getGroupFromRegex(hypixelDefaultRegex, finalMessageStr, 2) != null){
        finalMessageStr = getGroupFromRegex(hypixelDefaultRegex, finalMessageStr, 2);
      }
    }else{
      return;
    }

    String finalMessageStr1 = finalMessageStr;
    Task.builder(() -> {
      try {
        String translation = translator.translate(addon.configuration().translationPlatform().get(), "auto", targetLang,
            finalMessageStr1);

        String finalTranslation = translation;

        if(sender != null){
          if (getGroupFromRegex(mcDefaultRegex, messageStr, 1) != null){
            finalTranslation = getGroupFromRegex(mcDefaultRegex, messageStr, 1) + finalTranslation;
          }else if (getGroupFromRegex(hypixelDefaultRegex, messageStr, 1) != null){
            finalTranslation = getGroupFromRegex(hypixelDefaultRegex, messageStr, 1) + finalTranslation;
          }
        }

        String finalTranslation1 = finalTranslation;
        Task.builder(() -> {
          message.edit(Component.text(finalTranslation1));
        }).build().executeOnRenderThread();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).build().execute();
  }
}
