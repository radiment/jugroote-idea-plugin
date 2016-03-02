package com.epam.jugroote.plugin.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrStatement;
import org.jetbrains.plugins.groovy.lang.psi.impl.GroovyPsiElementImpl;

public class GrTag extends GroovyPsiElementImpl implements GrStatement {
    public GrTag(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String toString() {
        return "XmlTag statement";
    }

}
