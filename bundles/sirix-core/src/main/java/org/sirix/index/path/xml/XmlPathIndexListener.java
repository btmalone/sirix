package org.sirix.index.path.xml;

import org.sirix.access.trx.node.xml.XmlIndexController.ChangeType;
import org.sirix.index.ChangeListener;
import org.sirix.index.path.PathIndexListener;
import org.sirix.node.interfaces.NameNode;
import org.sirix.node.interfaces.immutable.ImmutableNode;

final class XmlPathIndexListener implements ChangeListener {

  private final PathIndexListener pathIndexListener;

  XmlPathIndexListener(final PathIndexListener pathIndexListenerDelegate) {
    pathIndexListener = pathIndexListenerDelegate;
  }

  @Override
  public void listen(final ChangeType type, final ImmutableNode node, final long pathNodeKey) {
    if (node instanceof NameNode) {
      pathIndexListener.listen(type, node, pathNodeKey);
    }
  }
}
