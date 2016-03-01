package com.epam.jugroote.plugin.psi;

import com.epam.jugroote.plugin.GroovyHtmlFileType;
import com.epam.jugroote.plugin.GroovyHtmlLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GroovyHtmlFile extends PsiFileBase {
    public GroovyHtmlFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, GroovyHtmlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return GroovyHtmlFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Simple File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}
