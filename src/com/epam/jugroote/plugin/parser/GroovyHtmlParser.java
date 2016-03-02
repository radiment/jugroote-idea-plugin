package com.epam.jugroote.plugin.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.lang.parser.GroovyElementTypes;
import org.jetbrains.plugins.groovy.lang.parser.GroovyParser;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.blocks.OpenOrClosableBlock;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.constructor.ConstructorBody;
import org.jetbrains.plugins.groovy.lang.parser.parsing.toplevel.CompilationUnit;
import org.jetbrains.plugins.groovy.lang.parser.parsing.util.ParserUtils;

import static com.epam.jugroote.plugin.parser.GroovyHtmlTokenTypes.*;

public class GroovyHtmlParser extends GroovyParser {

    @Override @NotNull
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        if(root == GroovyElementTypes.OPEN_BLOCK) {
            OpenOrClosableBlock.parseOpenBlockDeep(builder, this);
        } else if(root == GroovyElementTypes.CLOSABLE_BLOCK) {
            OpenOrClosableBlock.parseClosableBlockDeep(builder, this);
        } else if(root == GroovyElementTypes.CONSTRUCTOR_BODY) {
            ConstructorBody.parseConstructorBodyDeep(builder, this);
        } else {
            assert root == GroovyHtmlParserDefinition.FILE : root;

            PsiBuilder.Marker rootMarker = builder.mark();
            CompilationUnit.parseFile(builder, this);
            rootMarker.done(root);
        }

        return builder.getTreeBuilt();
    }

    @Override
    protected boolean parseExtendedStatement(PsiBuilder builder) {
        if (builder.getTokenType() == TAG_START) {
            final PsiBuilder.Marker marker = builder.mark();
//            OpenOrClosableBlock.parseOpenBlockDeep(builder, this);
            builder.advanceLexer();
            ParserUtils.getToken(builder, TAG_START);
            ParserUtils.getToken(builder, TokenSet.create(TAG_CLOSE, TAG_END));
            marker.done(GroovyHtmlTypes.TAG);
            return true;
        }
        return false;
    }
}
