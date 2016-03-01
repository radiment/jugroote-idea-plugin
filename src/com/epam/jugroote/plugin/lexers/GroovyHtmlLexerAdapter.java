package com.epam.jugroote.plugin.lexers;

import com.intellij.lexer.FlexAdapter;

public class GroovyHtmlLexerAdapter extends FlexAdapter {
    public GroovyHtmlLexerAdapter() {
        super(new GroovyHtmlLexer(null));
    }
}
