package com.epam.jugroote.plugin.parser;

import com.epam.jugroote.plugin.psi.impl.GrTemplate;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.plugins.groovy.lang.parser.GroovyPsiCreator;

public class GroovyHtmlPsiCreator {
    public static PsiElement createElement(ASTNode node) {
        IElementType elem = node.getElementType();
        if (elem == GroovyHtmlTypes.TEMPLATE || elem == GroovyHtmlTokenTypes.TEMPLATE_TEXT) return new GrTemplate(node);
        return GroovyPsiCreator.createElement(node);
    }
}
