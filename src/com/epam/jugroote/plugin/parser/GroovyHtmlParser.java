package com.epam.jugroote.plugin.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.GroovyBundle;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;
import org.jetbrains.plugins.groovy.lang.parser.GroovyElementTypes;
import org.jetbrains.plugins.groovy.lang.parser.GroovyParser;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.blocks.OpenOrClosableBlock;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.constructor.ConstructorBody;
import org.jetbrains.plugins.groovy.lang.parser.parsing.toplevel.CompilationUnit;

import static com.intellij.psi.xml.XmlTokenType.*;
import static org.jetbrains.plugins.groovy.lang.parser.parsing.util.ParserUtils.getToken;

public class GroovyHtmlParser extends GroovyParser {

    @Override
    @NotNull
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        if (root == GroovyElementTypes.OPEN_BLOCK) {
            OpenOrClosableBlock.parseOpenBlockDeep(builder, this);
        } else if (root == GroovyElementTypes.CLOSABLE_BLOCK) {
            OpenOrClosableBlock.parseClosableBlockDeep(builder, this);
        } else if (root == GroovyElementTypes.CONSTRUCTOR_BODY) {
            ConstructorBody.parseConstructorBodyDeep(builder, this);
        } else {
            assert root == GroovyHtmlParserDefinition.FILE : root;

            PsiBuilder.Marker rootMarker = builder.mark();
            CompilationUnit.parseFile(builder, this);
            rootMarker.done(root);
        }

        return builder.getTreeBuilt();
    }

    private void parseTagBlock(PsiBuilder builder, TokenSet until) {
        while (true) {
            if (builder.eof() || until.contains(builder.getTokenType())) break;
            if (!parseGenericStatement(builder, until)) break;
        }
    }

    private boolean parseGenericStatement(PsiBuilder builder, TokenSet until) {
        skipSeparators(builder);
        boolean plainStatement = parseStatement(builder, false);

        if (plainStatement || parseExtendedStatement(builder)) {
            if (until.contains(builder.getTokenType())) {
                return false;
            }
        } else {
            builder.error(GroovyBundle.message("wrong.statement"));
            /*assert builder.getTokenType() != GroovyTokenTypes.mLCURLY &&
                    builder.getTokenType() != GroovyTokenTypes.mRCURLY;*/
            builder.advanceLexer();
        }
        return true;
    }

    private boolean skipSeparators(PsiBuilder builder) {
        boolean hasSeparators = false;
        while (builder.getTokenType() == GroovyTokenTypes.mSEMI || isExtendedSeparator(builder.getTokenType()) || builder.getTokenType() ==
                GroovyTokenTypes.mNLS) {
            hasSeparators = true;
            builder.advanceLexer();
        }
        return hasSeparators;
    }


    @Override
    protected boolean parseExtendedStatement(PsiBuilder builder) {
        if (builder.getTokenType() == XML_START_TAG_START) {
            final PsiBuilder.Marker marker = builder.mark();
//            OpenOrClosableBlock.parseOpenBlockDeep(builder, this);
            builder.advanceLexer();
            getToken(builder, XML_START_TAG_START);
            getToken(builder, XML_NAME);
            if (!getToken(builder, XML_EMPTY_ELEMENT_END)) {
                getToken(builder, TokenSet.create(XML_TAG_END));
                parseTagBlock(builder, TokenSet.create(XML_END_TAG_START));
                getToken(builder, XML_END_TAG_START);
                getToken(builder, XML_NAME);
                getToken(builder, XML_TAG_END);
            }
            marker.done(GroovyHtmlTypes.TAG);
            return true;
        }
        return XML_END_TAG_START == builder.getTokenType();

    }
}
