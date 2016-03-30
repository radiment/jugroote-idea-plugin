package com.epam.jugroote.plugin.codestyle;

import com.epam.jugroote.plugin.GrutLanguage;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.psi.codeStyle.CodeStyleSettings;

public class GrutCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
    protected GrutCodeStyleMainPanel(CodeStyleSettings currentSettings,
                                     CodeStyleSettings settings) {
        super(GrutLanguage.INSTANCE, currentSettings, settings);
    }

    @Override
    protected void initTabs(CodeStyleSettings settings) {
        super.initTabs(settings);
        addTab(new GrCodeStyleImportsPanelWrapper(settings));
    }
}
