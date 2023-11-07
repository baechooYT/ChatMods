package com.holybaechu.chatmods.listener;

import com.holybaechu.chatmods.ChatModsAddon;
import com.holybaechu.chatmods.translators.BaseTranslator;
import com.holybaechu.chatmods.translators.GoogleTranslator;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.concurrent.task.Task;

public class ChatReceiveListener {
  private final ChatModsAddon addon;
  private BaseTranslator translator;
  private String targetLang;

  public ChatReceiveListener(ChatModsAddon addon) {
    this.addon = addon;
    switch (addon.configuration().translationPlatform().get()){
      case GoogleTranslator:
        this.translator = new GoogleTranslator();
        break;
      default:
        this.translator = new GoogleTranslator();
        break;
    }
    this.targetLang = addon.configuration().targetLanguage().get().name().replace("_", "-");
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) throws Exception {
    if (!addon.configuration().enabled().get()) return;
    if (!addon.configuration().translatorEnabled().get()) return;

    ChatMessage message = event.chatMessage();

    Task.builder(() -> {
      try {
        String translation = translator.translate(addon.configuration().translationPlatform().get(), "en", targetLang, event.chatMessage().getFormattedText());
        Task.builder(() -> {
          message.edit(Component.text(translation));
        }).build().executeOnRenderThread();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).build().execute();
  }
}
