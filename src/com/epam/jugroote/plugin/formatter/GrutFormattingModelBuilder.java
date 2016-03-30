package com.epam.jugroote.plugin.formatter;

import com.epam.jugroote.plugin.parser.GrutTokenTypes;
import com.intellij.formatting.*;
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper;
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock;
import com.intellij.formatting.templateLanguages.TemplateLanguageBlockFactory;
import com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.PsiBasedFormattingModel;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;

import java.util.List;


public class GrutFormattingModelBuilder extends TemplateLanguageFormattingModelBuilder {
    @Override
    public TemplateLanguageBlock createTemplateLanguageBlock(@NotNull ASTNode astNode, @Nullable Wrap wrap,
                                                             @Nullable Alignment alignment, @Nullable
                                                             List<DataLanguageBlockWrapper> dataLanguageBlockWrappers,
                                                             @NotNull CodeStyleSettings codeStyleSettings) {
        return new GrutBlock(astNode, wrap, alignment, this, codeStyleSettings, dataLanguageBlockWrappers);
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }

    /**
     * Standard {@link PsiBasedFormattingModel} extension that handles the fact that groovy uses not single white space token type
     * ({@link TokenType#WHITE_SPACE}) but one additional token type as well: {@link GroovyTokenTypes#mNLS}. So, it allows to adjust
     * white space token type to use for calling existing common formatting stuff.
     */
    private static class GrutBlock extends TemplateLanguageBlock {

        GrutBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment,
                  @NotNull TemplateLanguageBlockFactory blockFactory, @NotNull CodeStyleSettings settings,
                  @Nullable List<DataLanguageBlockWrapper> foreignChildren) {
            super(node, wrap, alignment, blockFactory, settings, foreignChildren);
        }

        protected Wrap createChildWrap(ASTNode child) {
            return Wrap.createWrap(WrapType.NORMAL, false);
        }

        public Indent getIndent() {
            if (this.getParent() != null && this.getParent().getParent() != null) {
                IElementType type = this.getNode().getElementType();
                return Indent.getNormalIndent();
            } else {
                return Indent.getNoneIndent();
            }
        }

        protected Indent getChildIndent() {
            return Indent.getNormalIndent();
        }

        protected IElementType getTemplateTextElementType() {
            return GrutTokenTypes.TEMPLATE_TEXT;
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
}
