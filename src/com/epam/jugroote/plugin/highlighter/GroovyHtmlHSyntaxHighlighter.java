package com.epam.jugroote.plugin.highlighter;

import com.epam.jugroote.plugin.lexers.GroovyHtmlLexer;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.highlighter.GroovySyntaxHighlighter;

public class GroovyHtmlHSyntaxHighlighter extends GroovySyntaxHighlighter {

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new GroovyHtmlLexer();
    }

}
