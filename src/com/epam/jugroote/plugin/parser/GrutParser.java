package com.epam.jugroote.plugin.parser;

import com.epam.jugroote.plugin.psi.GrutElementType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LighterASTNode;
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
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.expressions.ExpressionStatement;
import org.jetbrains.plugins.groovy.lang.parser.parsing.statements.imports.ImportStatement;
import org.jetbrains.plugins.groovy.lang.parser.parsing.toplevel.packaging.PackageDefinition;
import org.jetbrains.plugins.groovy.lang.parser.parsing.util.ParserUtils;

import static com.epam.jugroote.plugin.parser.GrutElementTypes.INJECTION;
import static com.epam.jugroote.plugin.parser.GrutElementTypes.TEMPLATE;
import static com.epam.jugroote.plugin.parser.GrutTokenTypes.*;

public class GrutParser extends GroovyParser {

    public static final TokenSet SEPARATORS = TokenSet.create(
            GroovyTokenTypes.mNLS, GroovyTokenTypes.mSEMI);

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
            assert root == GrutParserDefinition.FILE : root;

            PsiBuilder.Marker rootMarker = builder.mark();
            parseFile(builder, this);
            rootMarker.done(root);
        }

        return builder.getTreeBuilt();
    }

    public void parseFile(PsiBuilder builder, GroovyParser parser) {

        ParserUtils.getToken(builder, GroovyTokenTypes.mSH_COMMENT);
        ParserUtils.getToken(builder, GroovyTokenTypes.mNLS);

        if (!PackageDefinition.parse(builder, parser)) {
            parser.parseStatementWithImports(builder);
        }

        while (!builder.eof()) {
            if (!parseSeparators(builder)) {
                builder.error(GroovyBundle.message("separator.expected"));
            }
            if (builder.eof()) break;
            if (!parser.parseStatementWithImports(builder) && !parseExtStatement(builder)) {
                ParserUtils.wrapError(builder, GroovyBundle.message("unexpected.symbol"));
            }
        }
    }

    public boolean parseSeparators(PsiBuilder builder) {
        IElementType type = builder.getTokenType();
        if (SEPARATORS.contains(type)) {
            builder.advanceLexer();
            while (ParserUtils.getToken(builder, SEPARATORS)) {
                // Parse newLines
            }
            return true;
        }
        LighterASTNode marker = builder.getLatestDoneMarker();
        if (marker != null) {
            IElementType previous = marker.getTokenType();
            if (INJECTION.equals(previous) || TEMPLATE.equals(previous)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean parseStatementWithImports(PsiBuilder builder) {
        return ImportStatement.parse(builder, this) || parseStatement(builder, false) || parseExtStatement(builder);
    }

    @Override
    protected boolean parseExtendedStatement(PsiBuilder builder) {
        return parseExtStatement(builder);
    }

    private boolean parseExtStatement(PsiBuilder builder) {
        IElementType type = builder.getTokenType();
        if (type == LEFT_PASTE_BRACE) {
            PsiBuilder.Marker template = builder.mark();
            builder.advanceLexer();
            doWhileNot(builder, RIGHT_PASTE_BRACE);
            template.done(TEMPLATE);
            return true;
        } else if (type == TEMPLATE_TEXT) {
            PsiBuilder.Marker template = builder.mark();
            builder.advanceLexer();
            template.done(TEMPLATE);
            return true;
        } else if (type == INJECT_START) {
            PsiBuilder.Marker inject = builder.mark();
            builder.advanceLexer();
            ExpressionStatement.parse(builder, this);
            ParserUtils.getToken(builder, INJECT_END);
            inject.done(INJECTION);
            return true;
        }

        return false;
    }

    private void doWhileNot(PsiBuilder builder, GrutElementType endToken) {
        while (!builder.eof()) {
            if (builder.getTokenType() == endToken) {
                builder.advanceLexer();
                break;
            } else {
                if (!parseExtStatement(builder)) {
                    builder.error("Unexpected element");
                    break;
                }
            }
        }
    }
}
