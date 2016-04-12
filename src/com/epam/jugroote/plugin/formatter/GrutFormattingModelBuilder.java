package com.epam.jugroote.plugin.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper;
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock;
import com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.GroovyLanguage;
import org.jetbrains.plugins.groovy.codeStyle.GroovyCodeStyleSettings;
import org.jetbrains.plugins.groovy.formatter.AlignmentProvider;
import org.jetbrains.plugins.groovy.formatter.FormattingContext;

import java.util.List;


public class GrutFormattingModelBuilder extends TemplateLanguageFormattingModelBuilder {

    @Override
    public TemplateLanguageBlock createTemplateLanguageBlock(@NotNull ASTNode node, @Nullable Wrap wrap,
                                                             @Nullable Alignment alignment, @Nullable
                                                             List<DataLanguageBlockWrapper> dataLanguageBlockWrappers,
                                                             @NotNull CodeStyleSettings settings) {
        CommonCodeStyleSettings groovySettings = settings.getCommonSettings(GroovyLanguage.INSTANCE);
        GroovyCodeStyleSettings customSettings = settings.getCustomSettings(GroovyCodeStyleSettings.class);
        final AlignmentProvider alignments = new AlignmentProvider();
        if (customSettings.USE_FLYING_GEESE_BRACES) {
            node.getPsi().accept(new PsiRecursiveElementVisitor() {
                @Override
                public void visitElement(PsiElement element) {
                    if (GeeseUtil.isClosureRBrace(element)) {
                        GeeseUtil.calculateRBraceAlignment(element, alignments);
                    }
                    else {
                        super.visitElement(element);
                    }
                }
            });
        }
        return new GrutBlock(node, Indent.getNormalIndent(), wrap, this, settings,
                new FormattingContext(groovySettings, alignments, customSettings, false), dataLanguageBlockWrappers);
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        return null;
    }

}
