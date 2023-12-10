package com.holybaechu.chattranslator;

import com.holybaechu.chattranslator.misc.Language;
import com.holybaechu.chattranslator.misc.TranslationPlatform;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@ConfigName("settings")
public class ChatTranslatorConfiguration extends AddonConfig {
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection("translator")
  @DropdownSetting
  private final ConfigProperty<TranslationPlatform> translationPlatform =
      new ConfigProperty<>(TranslationPlatform.google);
  @DropdownSetting
  private final ConfigProperty<Language> targetLanguage = new ConfigProperty<>(Language.EN);
  @SwitchSetting
  private final ConfigProperty<Boolean> doNotTranslateNonPlayerMessages = new ConfigProperty<>(false);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }
  public ConfigProperty<TranslationPlatform> translationPlatform() {
    return this.translationPlatform;
  }
  public ConfigProperty<Language> targetLanguage() {
    return this.targetLanguage;
  }
  public ConfigProperty<Boolean> doNotTranslateNonPlayerMessages() {
    return this.doNotTranslateNonPlayerMessages;
  }
}