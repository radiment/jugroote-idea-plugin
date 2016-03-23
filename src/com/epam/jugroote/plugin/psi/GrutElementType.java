package com.epam.jugroote.plugin.psi;

import com.epam.jugroote.plugin.GrutLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GrutElementType extends IElementType {

    public GrutElementType(@NotNull @NonNls String debugName) {
        super(debugName, GrutLanguage.INSTANCE);
    }
}
