package com.epam.jugroote.plugin.highlighter;

import com.epam.jugroote.plugin.lexers.GroovyHtmlLexerAdapter;
import com.epam.jugroote.plugin.parser.GroovyHtmlTokenTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.XmlHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.highlighter.GroovySyntaxHighlighter;

import java.util.HashMap;
import java.util.Map;

public class GroovyHtmlHSyntaxHighlighter extends GroovySyntaxHighlighter {

    public static final TextAttributesKey TAG = TextAttributesKey.createTextAttributesKey("TAG",
            XmlHighlighterColors.XML_TAG);

    private static final Map<IElementType, TextAttributesKey> MARKUP = new HashMap<IElementType, TextAttributesKey>();

    static {
        fillMap(MARKUP, TAG, GroovyHtmlTokenTypes.TAG_START);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new GroovyHtmlLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType type) {
        TextAttributesKey result = MARKUP.get(type);
        return result != null ? pack(result) : super.getTokenHighlights(type);
    }
}
