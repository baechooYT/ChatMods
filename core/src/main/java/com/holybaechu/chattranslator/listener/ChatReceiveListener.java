package com.holybaechu.chattranslator.listener;

import com.holybaechu.chattranslator.misc.Language;
import com.holybaechu.chattranslator.translators.BaseTranslator;
import com.holybaechu.chattranslator.translators.GoogleTranslator;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.logging.Logging;

import java.util.*;

public class ChatReceiveListener {
  private static BaseTranslator translator = new GoogleTranslator();
  private static Language targetLanguage = Language.EN;
  public static void setTranslator(BaseTranslator newTranslator) {
    translator = newTranslator;
  }

  public static void setTargetLanguage(Language newLanguage) {
    targetLanguage = newLanguage;
  }

  private static void translateMessage(List<Component> components, ChatMessage message){
    for (Component child : components) {
      List<Component> childChildren = child.getChildren();
      if (!childChildren.isEmpty()){
        translateMessage(childChildren, message);
      }

      if (child instanceof TextComponent textComponent) {
        if (Objects.equals(textComponent.getText(), ""))
          continue;
        if (Objects.equals(textComponent.getText(), " "))
          continue;

        try {
          textComponent.text(translator.translate("auto", String.valueOf(targetLanguage),
              textComponent.getText()));

          Task.builder(() -> message.edit(message.component())).build().executeOnRenderThread();
        } catch (Exception e) {
          Logging.getLogger().error("Failed to translate message: " + e);
        }
      }
    }
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    // TODO: Do not translate player names

    ChatMessage message = event.chatMessage();
    List<Component> messageChildren = message.component().getChildren();

    Task.builder(() -> {
      if (messageChildren.isEmpty()){
        try {
          String translatedMessage = translator.translate("auto", String.valueOf(targetLanguage), message.getFormattedText());

          Task.builder(() -> message.edit(Component.text(translatedMessage))).build().executeOnRenderThread();
        } catch (Exception e) {
          Logging.getLogger().error("Failed to translate message: " + e);
        }
      }else{translateMessage(messageChildren, message);}

    }).build().execute();
  }
}
