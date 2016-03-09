package com.epam.jugroote.plugin.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class GroovyHtmlParser implements PsiParser {

    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
//        builder.enforceCommentTokens(TokenSet.EMPTY);
//        builder.putUserDataUnprotected(PsiBuilderImpl.CUSTOM_COMPARATOR, REPARSE_XML_TAG_BY_NAME);
        final PsiBuilder.Marker file = builder.mark();
        new GroovyHtmlParsing(builder).parseDocument();
        file.done(root);
        return builder.getTreeBuilt();
    }
}
