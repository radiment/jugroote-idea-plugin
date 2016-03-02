package com.epam.jugroote.plugin.parser;

import com.epam.jugroote.plugin.psi.impl.GrTag;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.plugins.groovy.lang.parser.GroovyPsiCreator;

public class GroovyHtmlPsiCreator {
    public static PsiElement createElement(ASTNode node) {
        IElementType elem = node.getElementType();
        if (elem == GroovyHtmlTypes.TAG) return new GrTag(node);
        return GroovyPsiCreator.createElement(node);
    }
}
