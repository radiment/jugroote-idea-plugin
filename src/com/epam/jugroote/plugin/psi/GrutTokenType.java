package com.epam.jugroote.plugin.psi;

import com.epam.jugroote.plugin.GrutLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GrutTokenType extends IElementType {

    public GrutTokenType(@NotNull @NonNls String debugName) {
        super(debugName, GrutLanguage.INSTANCE);
    }

}
