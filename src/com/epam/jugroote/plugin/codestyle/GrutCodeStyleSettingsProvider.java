package com.epam.jugroote.plugin.codestyle;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.codeStyle.GroovyCodeStyleMainPanel;
import org.jetbrains.plugins.groovy.codeStyle.GroovyCodeStyleSettings;

public class GrutCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
    @NotNull
    @Override
    public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSettings) {
        return new CodeStyleAbstractConfigurable(settings, originalSettings, "Grut") {
            @Override
            protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
                return new GroovyCodeStyleMainPanel(getCurrentSettings(), settings) {};
            }

            @Override
            public String getHelpTopic() {
                return "reference.settingsdialog.codestyle.groovy";
            }
        };
    }

    @Nullable
    @Override
    public String getConfigurableDisplayName() {
        return "Grut";
    }

    @Nullable
    @Override
    public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings) {
        return new GroovyCodeStyleSettings(settings);
    }
}
