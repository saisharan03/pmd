/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.ast;

import java.util.Iterator;

import net.sourceforge.pmd.lang.ast.Node;


/**
 * Groups a variable ID and its initializer if it exists.
 * May be found as a child of {@linkplain ASTFieldDeclaration field declarations} and
 * {@linkplain ASTLocalVariableDeclaration local variable declarations}.
 *
 * <p>The {@linkplain #getInitializer() initializer} is the only place
 * {@linkplain ASTArrayInitializer array initializer expressions} can be found.
 *
 * <pre class="grammar">
 *
 * VariableDeclarator ::= {@linkplain ASTVariableDeclaratorId VariableDeclaratorId} ( "=" {@linkplain ASTExpression Expression} )?
 *
 * </pre>
 */
public class ASTVariableDeclarator extends AbstractJavaTypeNode {

    ASTVariableDeclarator(int id) {
        super(id);
    }


    ASTVariableDeclarator(JavaParser p, int id) {
        super(p, id);
    }


    @Override
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }


    @Override
    public <T> void jjtAccept(SideEffectingVisitor<T> visitor, T data) {
        visitor.visit(this, data);
    }


    /**
     * Returns the name of the declared variable.
     */
    public String getName() {
        // first child will be VariableDeclaratorId
        return jjtGetChild(0).getImage();
    }


    /**
     * Returns the id of the declared variable.
     */
    public ASTVariableDeclaratorId getVariableId() {
        return (ASTVariableDeclaratorId) jjtGetChild(0);
    }


    /**
     * Returns true if the declared variable is initialized.
     * Otherwise, {@link #getInitializer()} returns null.
     */
    public boolean hasInitializer() {
        return jjtGetNumChildren() > 1;
    }


    /**
     * Returns the initializer, of the variable, or null if it doesn't exist.
     */
    public ASTExpression getInitializer() {
        return hasInitializer() ? (ASTExpression) jjtGetChild(1) : null;
    }


    /* only for LocalVarDeclaration and FieldDeclaration */
    static Iterator<ASTVariableDeclaratorId> iterateIds(Node parent) {
        // TODO this can be made clearer with iterator mapping (Java 8)
        final Iterator<ASTVariableDeclarator> declarators = parent.children(ASTVariableDeclarator.class).iterator();

        return new Iterator<ASTVariableDeclaratorId>() {
            @Override
            public boolean hasNext() {
                return declarators.hasNext();
            }


            @Override
            public ASTVariableDeclaratorId next() {
                return declarators.next().getVariableId();
            }


            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

}
