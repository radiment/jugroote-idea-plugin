package com.epam.jugroote.plugin.parser;

import com.epam.jugroote.plugin.psi.GroovyHtmlTokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;

public interface GroovyHtmlTokenTypes extends GroovyTokenTypes {

    IElementType TAG_START = new GroovyHtmlTokenType("<?*");
    IElementType TAG_CLOSE = new GroovyHtmlTokenType("/>");
    IElementType TAG_END = new GroovyHtmlTokenType(">");
    IElementType TAG_SELF_CLOSE = new GroovyHtmlTokenType("<?*/>");
    IElementType TAG_ATTRIBUTE = new GroovyHtmlTokenType("attribute");
    IElementType ATTRIBUTE_VALUE = new GroovyHtmlTokenType("attribute value");
}
