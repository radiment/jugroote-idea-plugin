package com.epam.jugroote.plugin;

import com.intellij.lang.Language;

public class GroovyHtmlLanguage extends Language {

    public static final GroovyHtmlLanguage INSTANCE = new GroovyHtmlLanguage();

    protected GroovyHtmlLanguage() {
        super("ghtml");
    }
}
