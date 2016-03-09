package com.epam.jugroote.plugin.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.impl.source.parsing.xml.XmlParsing;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlElementType;
import com.intellij.psi.xml.XmlTokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.GroovyBundle;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;
import org.jetbrains.plugins.groovy.lang.parser.GroovyElementTypes;
import org.jetbrains.plugins.groovy.lang.parser.GroovyParser;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.blocks.OpenOrClosableBlock;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.constructor.ConstructorBody;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.imports.ImportStatement;
import org.jetbrains.plugins.groovy.lang.parser.parsing.toplevel.CompilationUnit;

import static com.intellij.codeInsight.daemon.XmlErrorMessages.message;
import static com.intellij.psi.xml.XmlElementType.XML_ENTITY_REF;
import static com.intellij.psi.xml.XmlTokenType.*;

public class GroovyHtmlParsing extends XmlParsing {

    public GroovyParser groovyParser;

    public GroovyHtmlParsing(PsiBuilder psiBuilder) {
        super(psiBuilder);
        groovyParser = new GroovyParser() {
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

            @Override
            public boolean parseStatementWithImports(PsiBuilder builder) {
                return ImportStatement.parse(builder, this) || parseStatement(builder, false) || parseXmlStatement();
            }

            @Override
            protected boolean parseExtendedStatement(PsiBuilder builder) {
                return parseXmlStatement();
            }
        };
    }

    @Override
    public void parseDocument() {
        CompilationUnit.parseFile(myBuilder, groovyParser);
    }

    private boolean parseXmlStatement() {
        IElementType tt = token();
        if (tt == XML_START_TAG_START) {
            parseTag(false);
            return true;
        } else if (tt == XML_DOCTYPE_START) {
            parseDoctype();
            return true;
        }
        else if (tt == XML_ENTITY_REF_TOKEN) {
            parseReference();
            return true;
        }
        else if (tt == XML_CHAR_ENTITY_REF) {
            parseReference();
            return true;
        }
        else if (tt == XML_CDATA_START) {
            parseCData();
            return true;
        }
        else if (isCommentToken(tt)) {
            parseComment();
            return true;
        }
        return false;
    }

    @Override
    public void parseTagContent() {
        while (token() != XML_END_TAG_START && !eof()) {
            if (!parseGenericStatement()) break;
        }
    }

    private boolean parseGenericStatement() {
        skipSeparators();
        if (XML_END_TAG_START == token()) return false;
        boolean plainStatement = groovyParser.parseStatement(myBuilder, false);

        if (!plainStatement && !parseXmlStatement()) {
            error(GroovyBundle.message("wrong.statement"));
            /*assert builder.getTokenType() != GroovyTokenTypes.mLCURLY &&
                    builder.getTokenType() != GroovyTokenTypes.mRCURLY;*/
            advance();
        }
        return true;
    }

    private void parseReference() {
        if (token() == XML_CHAR_ENTITY_REF) {
            advance();
        }
        else if (token() == XML_ENTITY_REF_TOKEN) {
            final PsiBuilder.Marker ref = mark();
            advance();
            ref.done(XML_ENTITY_REF);
        }
        else {
            assert false : "Unexpected token";
        }
    }

    private void parseDoctype() {
        assert token() == XmlTokenType.XML_DOCTYPE_START : "Doctype start expected";
        final PsiBuilder.Marker doctype = mark();
        advance();

        while (token() != XmlTokenType.XML_DOCTYPE_END && !eof()) advance();
        if (eof()) {
            error(message("xml.parsing.unexpected.end.of.file"));
        }
        else {
            advance();
        }

        doctype.done(XmlElementType.XML_DOCTYPE);
    }

    private void parseCData() {
        assert token() == XmlTokenType.XML_CDATA_START;
        final PsiBuilder.Marker cdata = mark();
        while (token() != XmlTokenType.XML_CDATA_END && !eof()) {
            advance();
        }

        if (!eof()) {
            advance();
        }

        cdata.done(XmlElementType.XML_CDATA);
    }


    private boolean skipSeparators() {
        boolean hasSeparators = false;
        while (token() == GroovyTokenTypes.mSEMI || token() == GroovyTokenTypes.mNLS) {
            hasSeparators = true;
            advance();
        }
        return hasSeparators;
    }

    private void error(String msg) {
        myBuilder.error(msg);
    }
}
