package com.epam.jugroote.plugin.psi;

import com.epam.jugroote.plugin.GroovyHtmlLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GroovyHtmlTokenType extends IElementType {

    public GroovyHtmlTokenType(@NotNull @NonNls String debugName) {
        super(debugName, GroovyHtmlLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "GroovyHtmlTokenType." + super.toString();
    }
}
