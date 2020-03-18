package org.sirix.index.name.json;

import org.sirix.api.PageTrx;
import org.sirix.index.IndexDef;
import org.sirix.index.name.NameIndexBuilderFactory;
import org.sirix.index.name.NameIndexListenerFactory;
import org.sirix.node.interfaces.Record;
import org.sirix.page.UnorderedKeyValuePage;

public final class JsonNameIndexImpl implements JsonNameIndex {

  private final NameIndexBuilderFactory nameIndexBuilderFactory;

  private final NameIndexListenerFactory nameIndexListenerFactory;

  public JsonNameIndexImpl() {
    nameIndexBuilderFactory = new NameIndexBuilderFactory();
    nameIndexListenerFactory = new NameIndexListenerFactory();
  }

  @Override
  public JsonNameIndexBuilder createBuilder(final PageTrx<Long, Record, UnorderedKeyValuePage> pageWriteTrx,
      final IndexDef indexDef) {
    final var nameIndexBuilderDelegate = nameIndexBuilderFactory.create(pageWriteTrx, indexDef);
    return new JsonNameIndexBuilder(nameIndexBuilderDelegate);
  }

  @Override
  public JsonNameIndexListener createListener(final PageTrx<Long, Record, UnorderedKeyValuePage> pageWriteTrx,
      final IndexDef indexDef) {
    final var nameIndexListenerDelegate = nameIndexListenerFactory.create(pageWriteTrx, indexDef);
    return new JsonNameIndexListener(nameIndexListenerDelegate);
  }
}
