package com.epam.jugroote.plugin;

import com.intellij.lang.Language;

public class GrutLanguage extends Language {

    public static final GrutLanguage INSTANCE = new GrutLanguage();

    protected GrutLanguage() {
        super("grut");
    }
}
