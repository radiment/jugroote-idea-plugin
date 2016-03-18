package com.epam.jugroote.plugin.parser;

import com.epam.jugroote.plugin.psi.GroovyHtmlElementType;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;

public interface GroovyHtmlTokenTypes extends GroovyTokenTypes {

    GroovyHtmlElementType TEMPLATE_TEXT = new GroovyHtmlElementType("TEMPLATE_TEXT");
    GroovyHtmlElementType INJECT_START = new GroovyHtmlElementType("${");
    GroovyHtmlElementType INJECT_END = new GroovyHtmlElementType("}");

}
