package com.epam.jugroote.plugin.highlighter;

import com.epam.jugroote.plugin.parser.GroovyHtmlTokenTypes;
import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.templateLanguages.TemplateDataHighlighterWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GroovyHtmlHighlighter extends LayeredLexerEditorHighlighter {

    public GroovyHtmlHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile,
                                 @NotNull EditorColorsScheme colors) {
        super(new GroovyHtmlHSyntaxHighlighter(), colors);
        SyntaxHighlighter highlighter = getTemplateDataLanguageHighlighter(project, virtualFile);
        this.registerLayer(GroovyHtmlTokenTypes.TEMPLATE_TEXT,
                new LayerDescriptor(new TemplateDataHighlighterWrapper(highlighter), ""));
    }


    @NotNull
    private static SyntaxHighlighter getTemplateDataLanguageHighlighter(Project project, VirtualFile virtualFile) {
        LanguageFileType fileType = HtmlFileType.INSTANCE;
        SyntaxHighlighter highlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(fileType, project, virtualFile);

        assert highlighter != null;

        return highlighter;
    }
}
