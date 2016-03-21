package com.epam.jugroote.plugin.parser;

import com.epam.jugroote.plugin.psi.impl.GrTemplate;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.plugins.groovy.lang.parser.GroovyPsiCreator;

import static com.epam.jugroote.plugin.parser.GroovyHtmlTypes.*;

public class GroovyHtmlPsiCreator {
    public static PsiElement createElement(ASTNode node) {
        IElementType elem = node.getElementType();
        if (elem == INJECTION || elem == TEMPLATE) return new GrTemplate(node);
        return GroovyPsiCreator.createElement(node);
    }
}
