package org.sirix.node.immutable.xml;

import org.brackit.xquery.atomic.QNm;
import org.sirix.api.visitor.VisitResult;
import org.sirix.api.visitor.XmlNodeVisitor;
import org.sirix.node.NodeKind;
import org.sirix.node.SirixDeweyID;
import org.sirix.node.interfaces.Node;
import org.sirix.node.interfaces.immutable.ImmutableNameNode;
import org.sirix.node.interfaces.immutable.ImmutableXmlNode;
import org.sirix.node.xml.NamespaceNode;

import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Immutable namespace node wrapper.
 *
 * @author Johannes Lichtenberger
 *
 */
public class ImmutableNamespace implements ImmutableNameNode, ImmutableXmlNode {

  /** Mutable {@link NamespaceNode}. */
  private final NamespaceNode node;

  /**
   * Private constructor.
   *
   * @param node {@link NamespaceNode} to wrap
   */
  private ImmutableNamespace(final NamespaceNode node) {
    this.node = checkNotNull(node);
  }

  /**
   * Get an immutable namespace node instance.
   *
   * @param node the mutable {@link NamespaceNode} to wrap
   * @return immutable namespace node instance
   */
  public static ImmutableNamespace of(final NamespaceNode node) {
    return new ImmutableNamespace(node);
  }

  @Override
  public int getTypeKey() {
    return node.getTypeKey();
  }

  @Override
  public boolean isSameItem(final @Nullable Node pOther) {
    return node.isSameItem(pOther);
  }

  @Override
  public VisitResult acceptVisitor(final XmlNodeVisitor pVisitor) {
    return pVisitor.visit(this);
  }

  @Override
  public BigInteger getHash() {
    return node.getHash();
  }

  @Override
  public long getParentKey() {
    return node.getParentKey();
  }

  @Override
  public boolean hasParent() {
    return node.hasParent();
  }

  @Override
  public long getNodeKey() {
    return node.getNodeKey();
  }

  @Override
  public NodeKind getKind() {
    return node.getKind();
  }

  @Override
  public long getRevision() {
    return node.getRevision();
  }

  @Override
  public int getLocalNameKey() {
    return node.getLocalNameKey();
  }

  @Override
  public int getPrefixKey() {
    return node.getPrefixKey();
  }

  @Override
  public int getURIKey() {
    return node.getURIKey();
  }

  @Override
  public long getPathNodeKey() {
    return node.getPathNodeKey();
  }

  @Override
  public Optional<SirixDeweyID> getDeweyID() {
    return node.getDeweyID();
  }

  @Override
  public boolean equals(Object obj) {
    return node.equals(obj);
  }

  @Override
  public int hashCode() {
    return node.hashCode();
  }

  @Override
  public String toString() {
    return node.toString();
  }

  @Override
  public QNm getName() {
    return node.getName();
  }

  @Override
  public BigInteger computeHash() {
    return node.computeHash();
  }
}
