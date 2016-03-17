package com.epam.jugroote.plugin.lexers;

import com.intellij.lexer.*;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static com.epam.jugroote.plugin.parser.GroovyHtmlTokenTypes.*;

public class GroovyHtmlLexer extends LookAheadLexer {
    private static final TokenSet tokensToMerge = TokenSet.create(
            mSL_COMMENT,
            mML_COMMENT,
            mREGEX_CONTENT,
            mDOLLAR_SLASH_REGEX_CONTENT,
            TokenType.WHITE_SPACE,
            mGSTRING_CONTENT,
            TEMPLATE_TEXT
    );

    public static final TokenSet concatTokens = TokenSet.create(mNLS, TEMPLATE_TEXT);

    public GroovyHtmlLexer() {
        super(new MergingIntoLexerAdapter(
                new MergingLexerAdapter(new FlexAdapter(new GroovyHtmlFlexLexer(null)), tokensToMerge),
                concatTokens, TEMPLATE_TEXT));
    }

    private static class MergingIntoLexerAdapter extends MergingLexerAdapterBase {

        private IElementType targetToken;
        private final TokenSet tokensToMerge;

        public MergingIntoLexerAdapter(Lexer original, TokenSet tokensToMerge, IElementType targetToken) {
            super(original);
            this.tokensToMerge = tokensToMerge;
            this.targetToken = targetToken;
        }

        @Override
        public MergeFunction getMergeFunction() {
            return (type, originalLexer) -> {
                if (type != targetToken) {
                    return type;
                }

                while (tokensToMerge.contains(originalLexer.getTokenType())) {
                    originalLexer.advance();
                }
                return targetToken;
            };
        }
    }
}
