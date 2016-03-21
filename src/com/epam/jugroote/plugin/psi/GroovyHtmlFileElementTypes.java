package com.epam.jugroote.plugin.psi;

import com.epam.jugroote.plugin.GroovyHtmlLanguage;
import com.epam.jugroote.plugin.parser.GroovyHtmlTokenTypes;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;

public class GroovyHtmlFileElementTypes {
    private static final IElementType OUTER_ELEMENT_TYPE = new IElementType("GHTML_FRAGMENT", GroovyHtmlLanguage.INSTANCE);
    public static final TemplateDataElementType TEMPLATE_DATA =
            new TemplateDataElementType("GHTML_TEMPLATE_DATA", GroovyHtmlLanguage.INSTANCE,
                    GroovyHtmlTokenTypes.TEMPLATE_TEXT, OUTER_ELEMENT_TYPE);

    private GroovyHtmlFileElementTypes() {
    }
}
