package com.holybaechu.chatmods;

import com.holybaechu.chatmods.listener.ChatReceiveListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class ChatModsAddon extends LabyAddon<ChatModsConfiguration> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.registerListener(new ChatReceiveListener(this));

    this.logger().info("Enabled the Addon");
  }

  @Override
  protected Class<ChatModsConfiguration> configurationClass() {
    return ChatModsConfiguration.class;
  }
}
