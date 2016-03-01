package com.epam.jugroote.plugin.parser;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.plugins.groovy.lang.parser.GroovyPsiCreator;

public class GroovyHtmlPsiCreator {
    public static PsiElement createElement(ASTNode node) {
        return GroovyPsiCreator.createElement(node);
    }
}
