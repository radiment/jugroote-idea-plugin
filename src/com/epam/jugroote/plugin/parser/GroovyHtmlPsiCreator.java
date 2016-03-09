package com.epam.jugroote.plugin.parser;

import com.epam.jugroote.plugin.psi.impl.GrTag;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.plugins.groovy.lang.parser.GroovyPsiCreator;

import static com.intellij.psi.xml.XmlElementType.XML_DOCTYPE;
import static com.intellij.psi.xml.XmlElementType.XML_TAG;

public class GroovyHtmlPsiCreator {
    public static PsiElement createElement(ASTNode node) {
        IElementType elem = node.getElementType();
        if (elem == XML_TAG || elem == XML_DOCTYPE) return new GrTag(node);
        return GroovyPsiCreator.createElement(node);
    }
}
