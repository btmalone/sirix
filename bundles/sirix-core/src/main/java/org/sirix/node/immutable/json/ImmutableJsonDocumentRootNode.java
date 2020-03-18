package org.sirix.node.immutable.json;

import org.sirix.api.visitor.JsonNodeVisitor;
import org.sirix.api.visitor.VisitResult;
import org.sirix.node.NodeKind;
import org.sirix.node.interfaces.StructNode;
import org.sirix.node.json.JsonDocumentRootNode;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Immutable document root node wrapper.
 *
 * @author Johannes Lichtenberger
 */
public final class ImmutableJsonDocumentRootNode extends AbstractImmutableJsonStructuralNode {

  /** Mutable {@link JsonDocumentRootNode} instance. */
  private final JsonDocumentRootNode node;

  /**
   * Private constructor.
   *
   * @param node mutable {@link JsonDocumentRootNode}
   */
  private ImmutableJsonDocumentRootNode(final JsonDocumentRootNode node) {
    this.node = checkNotNull(node);
  }

  /**
   * Get an immutable document root node instance.
   *
   * @param node the mutable {@link JsonDocumentRootNode} to wrap
   * @return immutable document root node instance
   */
  public static ImmutableJsonDocumentRootNode of(final JsonDocumentRootNode node) {
    return new ImmutableJsonDocumentRootNode(node);
  }

  @Override
  public VisitResult acceptVisitor(final JsonNodeVisitor visitor) {
    return visitor.visit(this);
  }

  @Override
  public StructNode structDelegate() {
    return node;
  }

  @Override
  public NodeKind getKind() {
    return NodeKind.XML_DOCUMENT;
  }

  @Override
  public BigInteger computeHash() {
    return node.computeHash();
  }
}
