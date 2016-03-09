package com.epam.jugroote.plugin.psi;

import com.epam.jugroote.plugin.GroovyHtmlFileType;
import com.epam.jugroote.plugin.GroovyHtmlLanguage;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes;
import org.jetbrains.plugins.groovy.lang.parser.GroovyElementTypes;
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile;
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory;
import org.jetbrains.plugins.groovy.lang.psi.api.toplevel.imports.GrImportStatement;
import org.jetbrains.plugins.groovy.lang.psi.api.toplevel.packaging.GrPackageDefinition;
import org.jetbrains.plugins.groovy.lang.psi.api.types.GrCodeReferenceElement;
import org.jetbrains.plugins.groovy.lang.psi.impl.GroovyCodeStyleManager;
import org.jetbrains.plugins.groovy.lang.psi.impl.GroovyFileBaseImpl;
import org.jetbrains.plugins.groovy.lang.psi.impl.synthetic.GroovyScriptClass;
import org.jetbrains.plugins.groovy.lang.psi.stubs.GrPackageDefinitionStub;

import javax.swing.*;

public class GroovyHtmlFile extends GroovyFileBaseImpl implements GroovyFile {

    private static final Logger LOG = Logger.getInstance(GroovyHtmlFile.class);
    private GroovyScriptClass myScriptClass;

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
        return "GroovyHtmlFile:" + getName();
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }

    @Override
    public GrImportStatement[] getImportStatements() {
        final StubElement<?> stub = getStub();
        if (stub != null) {
            return stub.getChildrenByType(GroovyElementTypes.IMPORT_STATEMENT, GrImportStatement.ARRAY_FACTORY);
        }

        return calcTreeElement().getChildrenAsPsiElements(GroovyElementTypes.IMPORT_STATEMENT, GrImportStatement.ARRAY_FACTORY);
    }

    @Override
    public boolean isTopControlFlowOwner() {
        return true;
    }

    @Nullable
    @Override
    public GrPackageDefinition getPackageDefinition() {
        final StubElement<?> stub = getStub();
        if (stub != null) {
            for (StubElement element : stub.getChildrenStubs()) {
                if (element instanceof GrPackageDefinitionStub) return (GrPackageDefinition)element.getPsi();
            }
            return null;
        }

        ASTNode node = calcTreeElement().findChildByType(GroovyElementTypes.PACKAGE_DEFINITION);
        return node != null ? (GrPackageDefinition)node.getPsi() : null;
    }

    @Nullable
    @Override
    public GrPackageDefinition setPackage(GrPackageDefinition newPackage) {
        final GrPackageDefinition oldPackage = getPackageDefinition();
        if (oldPackage == null) {
            if (newPackage != null) {
                final GrPackageDefinition result = (GrPackageDefinition)addAfter(newPackage, null);
                getNode().addLeaf(GroovyTokenTypes.mNLS, "\n", result.getNode().getTreeNext());
                return result;
            }
        }
        else {
            if (newPackage != null) {
                return (GrPackageDefinition)oldPackage.replace(newPackage);
            }
            else {
                oldPackage.delete();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public PsiType getInferredScriptReturnType() {
        return PsiType.VOID;
    }

    @Nullable
    @Override
    public GrImportStatement addImportForClass(@NotNull PsiClass aClass) throws IncorrectOperationException {
        try {
            // Calculating position
            GroovyPsiElementFactory factory = GroovyPsiElementFactory.getInstance(getProject());

            String qname = aClass.getQualifiedName();
            if (qname != null) {
                GrImportStatement importStatement = factory.createImportStatementFromText(qname, false, false, null);
                return addImport(importStatement);
            }
        }
        catch (IncorrectOperationException e) {
            LOG.error(e);
        }
        return null;
    }

    @NotNull
    @Override
    public GrImportStatement addImport(@NotNull GrImportStatement statement) throws IncorrectOperationException {
        return GroovyCodeStyleManager.getInstance(getProject()).addImport(this, statement);
    }

    @Override
    public boolean isScript() {
        return true;
    }

    @Nullable
    @Override
    public PsiClass getScriptClass() {
        GroovyScriptClass aClass = myScriptClass;
        if (aClass == null) {
            aClass = new GroovyScriptClass(this);
            myScriptClass = aClass;
        }

        return aClass;
    }

    @NotNull
    @Override
    public String getPackageName() {
        GrPackageDefinition packageDef = getPackageDefinition();
        if (packageDef != null) {
            final String name = packageDef.getPackageName();
            if (name != null) {
                return name;
            }
        }
        return "";
    }

    @Override
    public void setPackageName(String packageName) {
        final ASTNode fileNode = getNode();
        final GrPackageDefinition currentPackage = getPackageDefinition();
        if (packageName == null || packageName.isEmpty()) {
            if (currentPackage != null) {
                final ASTNode currNode = currentPackage.getNode();
                fileNode.removeChild(currNode);
            }
            return;
        }

        final GroovyPsiElementFactory factory = GroovyPsiElementFactory.getInstance(getProject());
        final GrPackageDefinition newPackage = (GrPackageDefinition)factory.createTopElementFromText("package " + packageName);

        if (currentPackage != null) {
            final GrCodeReferenceElement packageReference = currentPackage.getPackageReference();
            if (packageReference != null) {
                GrCodeReferenceElement ref = newPackage.getPackageReference();
                if (ref != null) {
                    packageReference.replace(ref);
                }
                return;
            }
        }

        final ASTNode newNode = newPackage.getNode();
        if (currentPackage != null) {
            final ASTNode currNode = currentPackage.getNode();
            fileNode.replaceChild(currNode, newNode);
        } else {
            ASTNode anchor = fileNode.getFirstChildNode();
            if (anchor != null && anchor.getElementType() == GroovyTokenTypes.mSH_COMMENT) {
                anchor = anchor.getTreeNext();
                fileNode.addLeaf(GroovyTokenTypes.mNLS, "\n", anchor);
            }
            fileNode.addChild(newNode, anchor);
            if (anchor != null && !anchor.getText().startsWith("\n\n")) {
                fileNode.addLeaf(GroovyTokenTypes.mNLS, "\n", anchor);
            }
        }
    }
}
