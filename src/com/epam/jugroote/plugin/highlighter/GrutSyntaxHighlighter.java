package com.epam.jugroote.plugin.highlighter;

import com.epam.jugroote.plugin.lexers.GrutLexer;
import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.highlighter.GroovySyntaxHighlighter;

public class GrutSyntaxHighlighter extends GroovySyntaxHighlighter {

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new GrutLexer();
    }

}
