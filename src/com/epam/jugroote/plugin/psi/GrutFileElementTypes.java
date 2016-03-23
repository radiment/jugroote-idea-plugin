package com.epam.jugroote.plugin.psi;

import com.epam.jugroote.plugin.GrutLanguage;
import com.epam.jugroote.plugin.parser.GrutTokenTypes;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;

public class GrutFileElementTypes {
    private static final IElementType OUTER_ELEMENT_TYPE = new IElementType("GHTML_FRAGMENT", GrutLanguage.INSTANCE);
    public static final TemplateDataElementType TEMPLATE_DATA =
            new TemplateDataElementType("GHTML_TEMPLATE_DATA", GrutLanguage.INSTANCE,
                    GrutTokenTypes.TEMPLATE_TEXT, OUTER_ELEMENT_TYPE);

    private GrutFileElementTypes() {
    }
}
