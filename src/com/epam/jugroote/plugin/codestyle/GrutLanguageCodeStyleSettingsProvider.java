package com.epam.jugroote.plugin.codestyle;

import com.epam.jugroote.plugin.GrutLanguage;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.codeStyle.GroovyLanguageCodeStyleSettingsProvider;

public class GrutLanguageCodeStyleSettingsProvider extends GroovyLanguageCodeStyleSettingsProvider {

    @NotNull
    @Override
    public Language getLanguage() {
        return GrutLanguage.INSTANCE;
    }

    @Override
    public CommonCodeStyleSettings getDefaultCommonSettings() {
        CommonCodeStyleSettings defaultSettings = new CommonCodeStyleSettings(GrutLanguage.INSTANCE);
        defaultSettings.initIndentOptions();
        defaultSettings.SPACE_WITHIN_BRACES = true;
        defaultSettings.KEEP_SIMPLE_CLASSES_IN_ONE_LINE = true;
        defaultSettings.KEEP_SIMPLE_METHODS_IN_ONE_LINE = true;
        return defaultSettings;
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType) {
        switch (settingsType) {
            case INDENT_SETTINGS: return INDENT_OPTIONS_SAMPLE;
            case SPACING_SETTINGS: return SPACING_SAMPLE;
            case WRAPPING_AND_BRACES_SETTINGS: return WRAPPING_CODE_SAMPLE;
            case BLANK_LINES_SETTINGS: return BLANK_LINE_SAMPLE;
            default:
                return "";
        }
    }

    private static final String INDENT_OPTIONS_SAMPLE =
    /*
    "topLevelLabel:\n" +
    "foo(42)\n" +
    */
            "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head class=\"too\">\n" +
                    "  <title>${\"ghtml\"}</title>\n" +
                    "</head>\n" +
                    "if (binding.hasVariable(\"body\") || binding.hasVariable(\"<test>\")) {\n" +
                    "<body class=\"${body}\">\n" +
                    "def length = \"test\".length()\n" +
                    "${length}\n" +
                    "def bla = \"bla\"\n" +
                    "if (body.length() > 0 && body.length() < 15) {\n" +
                    "${body}\n" +
                    "${\"test{$bla</test>\"} \n" +
                    "\n" +
                    "}\n" +
                    "<div class=\"test\">\n" +
                    "\n" +
                    "</div>\n" +
                    "<script type=\"application/javascript\">\n" +
                    "@{\n" +
                    "var a = ${body};\n" +
                    "for (var i = 0; i < 10; i++) {\n" +
                    "\n" +
                    "}\n" +
                    "}@\n" +
                    "${body}\n" +
                    "def a = 1;\n" +
                    "\n" +
                    "</script>\n" +
                    "</body>\n" +
                    "}\n" +
                    "</html>\n" +
                    "\n";

    private static final String SPACING_SAMPLE =
            "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head class=\"too\">\n" +
                    "    <title>${\"ghtml\"}</title>\n" +
                    "</head>\n" +
                    "if (binding.hasVariable(\"body\") || binding.hasVariable(\"<test>\")) {\n" +
                    "  <body class=\"${body}\">\n" +
                    "    def length = \"test\".length()\n" +
                    "    ${length}\n" +
                    "    def bla = \"bla\"\n" +
                    "    if (body.length() > 0 && body.length() < 15) {\n" +
                    "        ${body}\n" +
                    "        ${\"test{$bla</test>\"} \n" +
                    "\n" +
                    "    }\n" +
                    "  <div class=\"test\">\n" +
                    "    \n" +
                    "  </div>\n" +
                    "  <script type=\"application/javascript\">\n" +
                    "    @{\n" +
                    "      var a = ${body};\n" +
                    "      for (var i = 0; i < 10; i++) {\n" +
                    "\n" +
                    "      }\n" +
                    "    }@\n" +
                    "    ${body}\n" +
                    "    def a = 1;\n" +
                    "\n" +
                    "  </script>\n" +
                    "  </body>\n" +
                    "}\n" +
                    "</html>\n" +
                    "\n";
    private static final String WRAPPING_CODE_SAMPLE =
            "/*\n" +
                    " * This is a sample file.\n" +
                    " */\n" +
                    "\n" +
                    "public class ThisIsASampleClass extends C1 implements I1, I2, I3, I4, I5 {\n" +
                    "  private int f1 = 1\n" +
                    "  private String field2 = \"\"\n" +
                    "  public void foo1(int i1, int i2, int i3, int i4, int i5, int i6, int i7) {}\n" +
                    "  public static void longerMethod() throws Exception1, Exception2, Exception3 {\n" +
                    "// todo something\n" +
                    "    int\n" +
                    "i = 0\n" +
                    "    int var1 = 1; int var2 = 2\n" +
                    "    foo1(0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056, 0x0057)\n" +
                    "    int x = (3 + 4 + 5 + 6) * (7 + 8 + 9 + 10) * (11 + 12 + 13 + 14 + 0xFFFFFFFF)\n" +
                    "    String s1, s2, s3\n" +
                    "    s1 = s2 = s3 = \"012345678901456\"\n" +
                    "    assert i + j + k + l + n+ m <= 2 : \"assert description\"\n" +
                    "    int y = 2 > 3 ? 7 + 8 + 9 : 11 + 12 + 13\n" +
                    "    super.getFoo().foo().getBar().bar()\n" +
                    "\n" +
                    "    label: \n" +
                    "    if (2 < 3) return else if (2 > 3) return else return\n" +
                    "    for (int i = 0; i < 0xFFFFFF; i += 2) System.out.println(i)\n" +
                    "    print([\n" +
                    "       l1: expr1,\n" +
                    "       label2: expr2\n" +
                    "    ])\n" +
                    "    while (x < 50000) x++\n" +
                    "    switch (a) {\n" +
                    "    case 0:\n" +
                    "      doCase0()\n" +
                    "      break\n" +
                    "    default:\n" +
                    "      doDefault()\n" +
                    "    }\n" +
                    "    try {\n" +
                    "      doSomething()\n" +
                    "    } catch (Exception e) {\n" +
                    "      processException(e)\n" +
                    "    } finally {\n" +
                    "      processFinally()\n" +
                    "    }\n" +
                    "  }\n" +
                    "    public static void test() \n" +
                    "        throws Exception { \n" +
                    "        foo.foo().bar(\"arg1\", \n" +
                    "                      \"arg2\") \n" +
                    "        new Object() {}\n" +
                    "    } \n" +
                    "    class TestInnerClass {}\n" +
                    "    interface TestInnerInterface {}\n" +
                    "}\n" +
                    "\n" +
                    "enum Breed {\n" +
                    "    Dalmatian(), Labrador(), Dachshund()\n" +
                    "}\n" +
                    "\n" +
                    "@Annotation1 @Annotation2 @Annotation3(param1=\"value1\", param2=\"value2\") @Annotation4 class Foo {\n" +
                    "    @Annotation1 @Annotation3(param1=\"value1\", param2=\"value2\") public static void foo(){\n" +
                    "    }\n" +
                    "    @Annotation1 @Annotation3(param1=\"value1\", param2=\"value2\") public static int myFoo\n" +
                    "    public void method(@Annotation1 @Annotation3(param1=\"value1\", param2=\"value2\") final int param){\n" +
                    "        @Annotation1 @Annotation3(param1=\"value1\", param2=\"value2\") final int localVariable\n" +
                    "    }\n" +
                    "}";


    private static final String BLANK_LINE_SAMPLE =
            "/*\n" +
                    " * This is a sample file.\n" +
                    " */\n" +
                    "package com.intellij.samples\n" +
                    "\n" +
                    "import com.intellij.idea.Main\n" +
                    "\n" +
                    "import javax.swing.*\n" +
                    "import java.util.Vector\n" +
                    "\n" +
                    "public class Foo {\n" +
                    "  private int field1\n" +
                    "  private int field2\n" +
                    "\n" +
                    "  public void foo1() {\n" +
                    "      new Runnable() {\n" +
                    "          public void run() {\n" +
                    "          }\n" +
                    "      }\n" +
                    "  }\n" +
                    "\n" +
                    "  public class InnerClass {\n" +
                    "  }\n" +
                    "}\n" +
                    "class AnotherClass {\n" +
                    "}\n" +
                    "interface TestInterface {\n" +
                    "    int MAX = 10\n" +
                    "    int MIN = 1\n" +
                    "    def method1()\n" +
                    "    void method2()\n" +
                    "}";
}
