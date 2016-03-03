package com.epam.jugroote.plugin.highlighter;

import com.epam.jugroote.plugin.lexers.GroovyHtmlLexerAdapter;
import com.intellij.ide.highlighter.XmlFileHighlighter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.highlighter.GroovySyntaxHighlighter;

public class GroovyHtmlHSyntaxHighlighter extends SyntaxHighlighterBase {

    private XmlFileHighlighter xmlHighlighter;
    private GroovySyntaxHighlighter groovyHighlighter;

    public GroovyHtmlHSyntaxHighlighter() {
        xmlHighlighter = new XmlFileHighlighter();
        groovyHighlighter = new GroovySyntaxHighlighter();
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new GroovyHtmlLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType type) {
        TextAttributesKey[] result = xmlHighlighter.getTokenHighlights(type);
        return result != SyntaxHighlighterBase.EMPTY ? result : groovyHighlighter.getTokenHighlights(type);
    }
}
