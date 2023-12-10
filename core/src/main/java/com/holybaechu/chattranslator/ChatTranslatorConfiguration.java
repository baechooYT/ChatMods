package com.holybaechu.chattranslator;

import com.holybaechu.chattranslator.listener.ChatReceiveListener;
import com.holybaechu.chattranslator.misc.Language;
import com.holybaechu.chattranslator.misc.TranslationPlatform;
import com.holybaechu.chattranslator.translators.BaseTranslator;
import com.holybaechu.chattranslator.translators.GoogleTranslator;
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
      new ConfigProperty<>(TranslationPlatform.google).addChangeListener((value) -> {
        BaseTranslator translator = switch (value) {
          // if you want to add a translator just use "case <enumValue> -> new <Class>();"

          default -> new GoogleTranslator();
        };

        ChatReceiveListener.setTranslator(translator);
      });
  @DropdownSetting
  private final ConfigProperty<Language> targetLanguage = new ConfigProperty<>(Language.EN);
  @SwitchSetting
  private final ConfigProperty<Boolean> doNotTranslateNonPlayerMessages =
      new ConfigProperty<>(false);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }
  public String targetLanguage() {
    return targetLanguage.get().name().replace("_", "-");
  }
  public ConfigProperty<Boolean> doNotTranslateNonPlayerMessages() {
    return this.doNotTranslateNonPlayerMessages;
  }
}