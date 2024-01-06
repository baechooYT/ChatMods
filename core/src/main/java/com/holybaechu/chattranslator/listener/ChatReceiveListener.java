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

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    // TODO: Do not translate player names

    ChatMessage message = event.chatMessage();
    List<Component> messageChildren = message.component().getChildren();

    Task.builder(() -> {
        for (Component child : messageChildren) {
            if (child instanceof TextComponent textComponent) {
                Logging.getLogger().info(textComponent.getText());

                if (Objects.equals(textComponent.getText(), "")) continue;
                if (Objects.equals(textComponent.getText(), " ")) continue;

                try {
                    textComponent.text(translator.translate("auto", String.valueOf(targetLanguage), textComponent.getText()));

                    Task.builder(() -> message.edit(message.component())).build().executeOnRenderThread();
                } catch (Exception e) {
                    Logging.getLogger().error("Failed to translate message: " + e);
                }
            }
        }
    }).build().execute();
  }
}
