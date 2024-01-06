package com.holybaechu.chattranslator;

import com.holybaechu.chattranslator.listener.ChatReceiveListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class ChatTranslatorAddon extends LabyAddon<ChatTranslatorConfiguration> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.registerListener(new ChatReceiveListener());

    this.logger().info("Enabled the Addon");
  }

  @Override
  protected Class<ChatTranslatorConfiguration> configurationClass() {
    return ChatTranslatorConfiguration.class;
  }
}
