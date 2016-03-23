package com.epam.jugroote.plugin.psi;

import com.epam.jugroote.plugin.GrutLanguage;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.LanguageSubstitutors;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;

public class GrutFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider
        implements ConfigurableTemplateLanguageFileViewProvider {
    private final Language myTemplateDataLanguage;

    public GrutFileViewProvider(PsiManager manager,
                                VirtualFile virtualFile, boolean eventSystemEnabled) {
        super(manager, virtualFile, eventSystemEnabled);
        Language language = getTemplateDataLanguage(virtualFile, manager.getProject());
        this.myTemplateDataLanguage = LanguageSubstitutors.INSTANCE.substituteLanguage(language, virtualFile,
                manager.getProject());
    }

    public GrutFileViewProvider(PsiManager manager, VirtualFile fileCopy, boolean eventSystemEndabled,
                                Language myTemplateDataLanguage) {
        super(manager, fileCopy, eventSystemEndabled);
        this.myTemplateDataLanguage = myTemplateDataLanguage;
    }

    @NotNull
    static Language getTemplateDataLanguage(@NotNull VirtualFile virtualFile, @NotNull Project project) {
        Language language = TemplateDataLanguageMappings.getInstance(project).getMapping(virtualFile);
        return language == null? getTemplateDataLanguageByExtension(virtualFile):language;
    }

    @Nullable
    @Override
    protected PsiFile createFile(@NotNull Language lang) {
        if (lang == getBaseLanguage()) {
            return createFileInner(lang);
        }
        if (lang == getTemplateDataLanguage()) {
            final PsiFileImpl file = (PsiFileImpl) createFileInner(lang);
            file.setContentElementType(GrutFileElementTypes.TEMPLATE_DATA);
            return file;
        }
        return null;
    }

    private PsiFile createFileInner(Language lang) {
        return LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this);
    }

    @NotNull
    private static Language getTemplateDataLanguageByExtension(VirtualFile virtualFile) {
        String name = virtualFile.getName();
        int separateInd = name.lastIndexOf('.');
        if(separateInd < 3) {
            return StdLanguages.HTML;
        } else {
            int secondInd = 1 + name.lastIndexOf('.', separateInd - 1);
            if(secondInd < 1) {
                return StdLanguages.HTML;
            } else {
                String dataLanguageFileExtension = name.substring(secondInd, separateInd);
                FileType fileType = FileTypeManager.getInstance().getFileTypeByExtension(dataLanguageFileExtension);
                if(fileType instanceof LanguageFileType) {
                    return ((LanguageFileType)fileType).getLanguage();
                } else {
                    return StdLanguages.HTML;
                }
            }
        }
    }

    @NotNull
    @Override
    public Language getBaseLanguage() {
        return GrutLanguage.INSTANCE;
    }

    @NotNull
    @Override
    public Language getTemplateDataLanguage() {
        return myTemplateDataLanguage;
    }

    @NotNull
    @Override
    public Set<Language> getLanguages() {
        return new THashSet<>(Arrays.asList(GrutLanguage.INSTANCE, getTemplateDataLanguage()));
    }

    @Override
    protected MultiplePsiFilesPerDocumentFileViewProvider cloneInner(VirtualFile fileCopy) {
        return new GrutFileViewProvider(getManager(), fileCopy, false, myTemplateDataLanguage);
    }
}
