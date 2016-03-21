package com.epam.jugroote.plugin.psi;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.FileViewProviderFactory;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

public class GroovyHtmlFileViewProviderFactory implements FileViewProviderFactory {
    @NotNull
    @Override
    public FileViewProvider createFileViewProvider(@NotNull VirtualFile file, Language language,
                                                   @NotNull PsiManager manager, boolean eventSystemEnabled) {
        return new GroovyHtmlFileViewProvider(manager, file, eventSystemEnabled);
    }
}
