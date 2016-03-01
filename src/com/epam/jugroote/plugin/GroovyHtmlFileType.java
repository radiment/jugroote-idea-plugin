package com.epam.jugroote.plugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GroovyHtmlFileType extends LanguageFileType {

    public static final GroovyHtmlFileType INSTANCE = new GroovyHtmlFileType();

    private GroovyHtmlFileType() {
        super(GroovyHtmlLanguage.INSTANCE);
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
        return "ghtml";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return GroovyHtmlIcons.FILE;
    }
}
