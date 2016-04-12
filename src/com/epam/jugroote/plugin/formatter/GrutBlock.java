package com.epam.jugroote.plugin.formatter;

import com.epam.jugroote.plugin.parser.GrutParserDefinition;
import com.intellij.formatting.*;
import com.intellij.formatting.templateLanguages.BlockWithParent;
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper;
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock;
import com.intellij.formatting.templateLanguages.TemplateLanguageBlockFactory;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.PsiBasedFormattingModel;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.formatter.FormattingContext;
import org.jetbrains.plugins.groovy.formatter.blocks.GroovyBlock;
import org.jetbrains.plugins.groovy.formatter.processors.GroovySpacingProcessor;
import org.jetbrains.plugins.groovy.formatter.processors.GroovySpacingProcessorBasic;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;

import java.util.List;

import static com.epam.jugroote.plugin.parser.GrutElementTypes.INJECTION;
import static com.epam.jugroote.plugin.parser.GrutElementTypes.TEMPLATE;
import static com.epam.jugroote.plugin.parser.GrutTokenTypes.*;
import static org.jetbrains.plugins.groovy.lang.parser.GroovyElementTypes.*;

/**
 * Standard {@link PsiBasedFormattingModel} extension that handles the fact that groovy uses not single white space token type
 * ({@link TokenType#WHITE_SPACE}) but one additional token type as well: {@link GroovyTokenTypes#mNLS}. So, it allows to adjust
 * white space token type to use for calling existing common formatting stuff.
 */
class GrutBlock extends TemplateLanguageBlock {

    private static final Logger LOG = Logger.getInstance(GrutBlock.class);

    private TokenSet notGroovy = TokenSet.create(TEMPLATE, INJECTION, TEMPLATE_TEXT, INJECT_START, INJECT_END,
            LEFT_PASTE_BRACE, RIGHT_PASTE_BRACE, GrutParserDefinition.FILE);
    private TokenSet withoutIndent = TokenSet.create(ASSIGNMENT_EXPRESSION, VARIABLE_DEFINITION, PATH_METHOD_CALL);

    private final FormattingContext context;
    private final GroovyBlock groovyBlock;
    private final CodeStyleSettings settings;
    private final TemplateLanguageBlockFactory blockFactory;

    public GrutBlock(@NotNull ASTNode node, Indent indent, @Nullable Wrap wrap,
                     @NotNull TemplateLanguageBlockFactory blockFactory, CodeStyleSettings settings,
                     FormattingContext context, List<DataLanguageBlockWrapper> languageBlockWrappers) {
        super(node, wrap, context.getAlignmentProvider().getAlignment(
                node.getPsi()), blockFactory, settings, languageBlockWrappers);
        this.blockFactory = blockFactory;
        this.settings = settings;
        if (!notGroovy.contains(node.getElementType())) {
            groovyBlock = new GroovyBlock(node, indent, wrap, context);
        } else {
            groovyBlock = null;
        }
        this.context = context;
    }

    protected Wrap createChildWrap(ASTNode child) {
        return Wrap.createWrap(WrapType.NORMAL, false);
    }

    protected IElementType getTemplateTextElementType() {
        return TEMPLATE_TEXT;
    }

    public GroovyBlock getGroovyBlock() {
        return groovyBlock;
    }

    @Override
    public CodeStyleSettings getSettings() {
        return settings;
    }

    public FormattingContext getContext() {
        return context;
    }

    public TemplateLanguageBlockFactory getBlockFactory() {
        return blockFactory;
    }

    @Override
    protected List<Block> buildChildren() {
        return super.buildChildren();
    }

    @Override
    @Nullable
    public Wrap getWrap() {
        return myWrap;
    }

    @Override
    @Nullable
    public Indent getIndent() {
        BlockWithParent parent = this.getParent();
        if (parent == null) {
            System.out.println("parent == null");
        } else {
            System.out.println("parent = " + parent.getClass());
        }
        if (parent != null && parent.getParent() != null && !withoutIndent.contains(getNode().getElementType())) {
            System.out.println("normal [" + getNode() + "]");
            return Indent.getNormalIndent();
        } else {
            System.out.println(getNode());
            return Indent.getNoneIndent();
        }
    }

    /**
     * Returns spacing between neighbour elements
     *
     * @param child1 left element
     * @param child2 right element
     * @return
     */
    @Override
    @Nullable
    public Spacing getSpacing(Block child1, @NotNull Block child2) {
        if (child1 instanceof GrutBlock && child2 instanceof GrutBlock) {
            GrutBlock grutBlock1 = (GrutBlock) child1;
            GrutBlock grutBlock2 = (GrutBlock) child2;
            if (grutBlock1.getNode() ==  grutBlock2.getNode()) {
                return Spacing.getReadOnlySpacing();
            }
            GroovyBlock groovyBlock1 = grutBlock1.getGroovyBlock();
            GroovyBlock groovyBlock2 = grutBlock2.getGroovyBlock();
            if (groovyBlock1 == null || groovyBlock2 == null) {
                return super.getSpacing(child1, child2);
            }

            Spacing spacing = new GroovySpacingProcessor(groovyBlock1, groovyBlock2, context).getSpacing();
            if (spacing != null) {
                return spacing;
            }
            return GroovySpacingProcessorBasic.getSpacing(groovyBlock1, groovyBlock2, context);
        }
        return super.getSpacing(child1, child2);
    }




    @Override
    public boolean isIncomplete() {
        return isIncomplete(myNode);
    }

    /**
     * @param node Tree node
     * @return true if node is incomplete
     */
    public static boolean isIncomplete(@NotNull final ASTNode node) {
        if (node.getElementType() instanceof ILazyParseableElementType) return false;
        ASTNode lastChild = node.getLastChildNode();
        while (lastChild != null &&
                !(lastChild.getElementType() instanceof ILazyParseableElementType) &&
                (lastChild.getPsi() instanceof PsiWhiteSpace || lastChild.getPsi() instanceof PsiComment)) {
            lastChild = lastChild.getTreePrev();
        }
        return lastChild != null && (lastChild.getPsi() instanceof PsiErrorElement || isIncomplete(lastChild));
    }


    /*public Wrap substituteTemplateChildWrap(@NotNull DataLanguageBlockWrapper child, @Nullable Wrap childWrap) {
        return isStringLiteral(this.getNode())?Wrap.createWrap(WrapType.NONE, false):childWrap;
    }*/

    @Nullable
    public Spacing getRightNeighborSpacing(@NotNull Block rightNeighbor, @NotNull DataLanguageBlockWrapper parent,
                                           int thisBlockIndex) {
        List siblings = parent.getSubBlocks();

        int i;
        for (i = thisBlockIndex - 1; i >= 0 && !(siblings.get(i) instanceof DataLanguageBlockWrapper); --i) {
            ;
        }

        return parent.getSpacing(i < 0 ? null : (Block) siblings.get(i), rightNeighbor);
    }

}
