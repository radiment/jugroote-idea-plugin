package com.epam.jugroote.plugin.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.lang.parser.GroovyElementTypes;
import org.jetbrains.plugins.groovy.lang.parser.GroovyParser;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.blocks.OpenOrClosableBlock;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.constructor.ConstructorBody;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.imports.ImportStatement;
import org.jetbrains.plugins.groovy.lang.parser.parsing.toplevel.CompilationUnit;

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

    @Override
    public boolean parseStatementWithImports(PsiBuilder builder) {
        return ImportStatement.parse(builder, this) || parseStatement(builder, false) || parseXmlStatement(builder);
    }

    @Override
    protected boolean parseExtendedStatement(PsiBuilder builder) {
        return parseXmlStatement(builder);
    }

    private boolean parseXmlStatement(PsiBuilder builder) {
        if (builder.getTokenType() == GroovyHtmlTokenTypes.TEMPLATE_TEXT) {
            PsiBuilder.Marker template = builder.mark();
            builder.advanceLexer();
            template.done(GroovyHtmlTypes.TEMPLATE);
            return true;
        }
        return false;
    }
}
