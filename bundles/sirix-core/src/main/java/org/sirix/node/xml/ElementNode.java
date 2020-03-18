/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sirix.node.xml;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import org.brackit.xquery.atomic.QNm;
import org.sirix.api.visitor.VisitResult;
import org.sirix.api.visitor.XmlNodeVisitor;
import org.sirix.node.NodeKind;
import org.sirix.node.SirixDeweyID;
import org.sirix.node.delegates.NameNodeDelegate;
import org.sirix.node.delegates.NodeDelegate;
import org.sirix.node.delegates.StructNodeDelegate;
import org.sirix.node.immutable.xml.ImmutableElement;
import org.sirix.node.interfaces.NameNode;
import org.sirix.node.interfaces.Node;
import org.sirix.node.interfaces.immutable.ImmutableXmlNode;
import org.sirix.settings.Fixed;
import org.sirix.utils.NamePageHash;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Node representing an XML element.
 *
 * <strong>This class is not part of the public API and might change.</strong>
 */
public final class ElementNode extends AbstractStructForwardingNode implements NameNode, ImmutableXmlNode {

  /** Delegate for name node information. */
  private final NameNodeDelegate nameNodeDelegate;

  /** Mapping names/keys. */
  private final BiMap<Long, Long> attributes;

  /** Keys of attributes. */
  private final List<Long> attributeKeys;

  /** Keys of namespace declarations. */
  private final List<Long> namespaceKeys;

  /** {@link StructNodeDelegate} reference. */
  private final StructNodeDelegate structNodeDelegate;

  /** The qualified name. */
  private final QNm name;

  private BigInteger hash;

  /**
   * Constructor
   *
   * @param structNodeDelegate {@link StructNodeDelegate} to be set
   * @param nameNodeDelegate {@link NameNodeDelegate} to be set
   * @param attributeKeys list of attribute keys
   * @param attributes attribute nameKey / nodeKey mapping in both directions
   * @param namespaceKeys keys of namespaces to be set
   * @param
   */
  public ElementNode(final BigInteger hashCode, final StructNodeDelegate structNodeDelegate,
      final NameNodeDelegate nameNodeDelegate, final List<Long> attributeKeys, final BiMap<Long, Long> attributes,
      final List<Long> namespaceKeys, final QNm name) {
    hash = hashCode;
    assert structNodeDelegate != null;
    this.structNodeDelegate = structNodeDelegate;
    assert nameNodeDelegate != null;
    this.nameNodeDelegate = nameNodeDelegate;
    assert attributeKeys != null;
    this.attributeKeys = attributeKeys;
    assert attributes != null;
    this.attributes = attributes;
    assert namespaceKeys != null;
    this.namespaceKeys = namespaceKeys;
    assert name != null;
    this.name = name;
  }

  /**
   * Constructor
   *
   * @param structNodeDelegate {@link StructNodeDelegate} to be set
   * @param nameNodeDelegate {@link NameNodeDelegate} to be set
   * @param attributeKeys list of attribute keys
   * @param attributes attribute nameKey / nodeKey mapping in both directions
   * @param namespaceKeys keys of namespaces to be set
   * @param
   */
  public ElementNode(final StructNodeDelegate structNodeDelegate, final NameNodeDelegate nameNodeDelegate, final List<Long> attributeKeys,
      final BiMap<Long, Long> attributes, final List<Long> namespaceKeys, final QNm name) {
    assert structNodeDelegate != null;
    this.structNodeDelegate = structNodeDelegate;
    assert nameNodeDelegate != null;
    this.nameNodeDelegate = nameNodeDelegate;
    assert attributeKeys != null;
    this.attributeKeys = attributeKeys;
    assert attributes != null;
    this.attributes = attributes;
    assert namespaceKeys != null;
    this.namespaceKeys = namespaceKeys;
    assert name != null;
    this.name = name;
  }

  /**
   * Getting the count of attributes.
   *
   * @return the count of attributes
   */
  public int getAttributeCount() {
    return attributeKeys.size();
  }

  /**
   * Getting the attribute key for an given index.
   *
   * @param index index of the attribute
   * @return the attribute key
   */
  public long getAttributeKey(final @Nonnegative int index) {
    if (attributeKeys.size() <= index) {
      return Fixed.NULL_NODE_KEY.getStandardProperty();
    }
    return attributeKeys.get(index);
  }

  /**
   * Getting the attribute key by name (from the dictionary).
   *
   * @param name the attribute-name to lookup
   * @return the attribute key associated with the name
   */
  public Optional<Long> getAttributeKeyByName(final QNm name) {
    final int prefixIndex = name.getPrefix() != null && !name.getPrefix().isEmpty()
        ? NamePageHash.generateHashForString(name.getPrefix())
        : -1;
    final int localNameIndex = NamePageHash.generateHashForString(name.getLocalName());
    return Optional.ofNullable(attributes.get((long) (prefixIndex + localNameIndex)));
  }

  /**
   * Get name key (prefixKey+localNameKey) by node key.
   *
   * @param key node key
   * @return optional name key
   */
  public Optional<Long> getAttributeNameKey(final @Nonnegative long key) {
    return Optional.ofNullable(attributes.inverse().get(key));
  }

  /**
   * Inserting an attribute.
   *
   * @param attrKey the new attribute key
   * @param nameIndex index mapping to name string
   */
  public void insertAttribute(final @Nonnegative long attrKey, final long nameIndex) {
    attributeKeys.add(attrKey);
    attributes.put(nameIndex, attrKey);
  }

  /**
   * Removing an attribute.
   *
   * @param attrKey the key of the attribute to be removed@Nonnegative@Nonnegative
   */
  public void removeAttribute(final @Nonnegative long attrKey) {
    attributeKeys.remove(attrKey);
    attributes.inverse().remove(attrKey);
  }

  /**
   * Getting the count of namespaces.
   *
   * @return the count of namespaces
   */
  public int getNamespaceCount() {
    return namespaceKeys.size();
  }

  /**
   * Getting the namespace key for a given index.
   *
   * @param namespaceKey index of the namespace
   * @return the namespace key
   */
  public long getNamespaceKey(final @Nonnegative int namespaceKey) {
    if (namespaceKeys.size() <= namespaceKey) {
      return Fixed.NULL_NODE_KEY.getStandardProperty();
    }
    return namespaceKeys.get(namespaceKey);
  }

  /**
   * Inserting a namespace.
   *
   * @param namespaceKey new namespace key
   */
  public void insertNamespace(final long namespaceKey) {
    namespaceKeys.add(namespaceKey);
  }

  /**
   * Removing a namepsace.
   *
   * @param namespaceKey the key of the namespace to be removed
   */
  public void removeNamespace(final long namespaceKey) {
    namespaceKeys.remove(namespaceKey);
  }

  @Override
  public int getPrefixKey() {
    return nameNodeDelegate.getPrefixKey();
  }

  @Override
  public int getLocalNameKey() {
    return nameNodeDelegate.getLocalNameKey();
  }

  @Override
  public int getURIKey() {
    return nameNodeDelegate.getURIKey();
  }

  @Override
  public void setPrefixKey(final int prefixKey) {
    nameNodeDelegate.setPrefixKey(prefixKey);
  }

  @Override
  public void setLocalNameKey(final int localNameKey) {
    nameNodeDelegate.setLocalNameKey(localNameKey);
  }

  @Override
  public void setURIKey(final int uriKey) {
    nameNodeDelegate.setURIKey(uriKey);
  }

  @Override
  public NodeKind getKind() {
    return NodeKind.ELEMENT;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
                      .add("nameDelegate", nameNodeDelegate)
                      .add("nameSpaceKeys", namespaceKeys)
                      .add("attributeKeys", attributeKeys)
                      .add("structDelegate", structNodeDelegate)
                      .toString();
  }

  @Override
  public VisitResult acceptVisitor(final XmlNodeVisitor visitor) {
    return visitor.visit(ImmutableElement.of(this));
  }

  @Override
  public BigInteger computeHash() {
    BigInteger result = BigInteger.ONE;

    result = BigInteger.valueOf(31).multiply(result).add(structNodeDelegate.getNodeDelegate().computeHash());
    result = BigInteger.valueOf(31).multiply(result).add(structNodeDelegate.computeHash());
    result = BigInteger.valueOf(31).multiply(result).add(nameNodeDelegate.computeHash());

    return Node.to128BitsAtMaximumBigInteger(result);
  }

  @Override
  public void setHash(final BigInteger hash) {
    this.hash = Node.to128BitsAtMaximumBigInteger(hash);

    assert this.hash.toByteArray().length <= 17;
  }

  @Override
  public BigInteger getHash() {
    return hash;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(delegate(), nameNodeDelegate);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof ElementNode) {
      final ElementNode other = (ElementNode) obj;
      return Objects.equal(delegate(), other.delegate()) && Objects.equal(nameNodeDelegate, other.nameNodeDelegate);
    }
    return false;
  }

  /**
   * Get a {@link List} with all attribute keys.
   *
   * @return unmodifiable view of {@link List} with all attribute keys
   */
  public List<Long> getAttributeKeys() {
    return Collections.unmodifiableList(attributeKeys);
  }

  /**
   * Get a {@link List} with all namespace keys.
   *
   * @return unmodifiable view of {@link List} with all namespace keys
   */
  public List<Long> getNamespaceKeys() {
    return Collections.unmodifiableList(namespaceKeys);
  }

  @Override
  protected NodeDelegate delegate() {
    return structNodeDelegate.getNodeDelegate();
  }

  @Override
  protected StructNodeDelegate structDelegate() {
    return structNodeDelegate;
  }

  /**
   * Get name node delegate.
   *
   * @return snapshot of the name node delegate (new instance)
   */
  @Nonnull
  public NameNodeDelegate getNameNodeDelegate() {
    return new NameNodeDelegate(nameNodeDelegate);
  }

  @Override
  public void setPathNodeKey(final @Nonnegative long pathNodeKey) {
    nameNodeDelegate.setPathNodeKey(pathNodeKey);
  }

  @Override
  public long getPathNodeKey() {
    return nameNodeDelegate.getPathNodeKey();
  }

  @Override
  public QNm getName() {
    return name;
  }

  @Override
  public Optional<SirixDeweyID> getDeweyID() {
    return structNodeDelegate.getNodeDelegate().getDeweyID();
  }

  @Override
  public int getTypeKey() {
    return structNodeDelegate.getNodeDelegate().getTypeKey();
  }
}
