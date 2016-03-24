package com.epam.jugroote.plugin.parser;

import com.epam.jugroote.plugin.psi.GrutElementType;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;

public interface GrutTokenTypes extends GroovyTokenTypes {

    GrutElementType TEMPLATE_TEXT = new GrutElementType("TEMPLATE_TEXT");
    GrutElementType INJECT_START = new GrutElementType("${");
    GrutElementType INJECT_END = new GrutElementType("}");

    GrutElementType LEFT_PASTE_BRACE = new GrutElementType("@{");
    GrutElementType RIGHT_PASTE_BRACE = new GrutElementType("}@");

}
