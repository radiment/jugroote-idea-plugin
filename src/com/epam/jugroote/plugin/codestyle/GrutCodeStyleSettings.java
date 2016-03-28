package com.epam.jugroote.plugin.codestyle;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.plugins.groovy.codeStyle.GroovyCodeStyleSettings;

public class GrutCodeStyleSettings extends GroovyCodeStyleSettings {
    public GrutCodeStyleSettings(CodeStyleSettings container) {
        super(container);
    }
}
