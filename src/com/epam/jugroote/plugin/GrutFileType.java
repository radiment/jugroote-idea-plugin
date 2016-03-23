package com.epam.jugroote.plugin;

import com.epam.jugroote.plugin.highlighter.GrutHighlighter;
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GrutFileType extends LanguageFileType {

    public static final GrutFileType INSTANCE = new GrutFileType();

    private GrutFileType() {
        super(GrutLanguage.INSTANCE);
        FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this,
                (project, fileType, virtualFile, colors) -> new GrutHighlighter(project, virtualFile, colors));
    }

    @NotNull
    @Override
    public String getName() {
        return "Groovy html file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Groovy Html language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "gr";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return GrutIcons.FILE;
    }
}
