package org.tron.core.net.service.nodepersist;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.common.es.ExecutorServiceManager;
import org.tron.common.parameter.CommonParameter;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.JsonUtil;
import org.tron.core.capsule.BytesCapsule;
import org.tron.core.db.CommonStore;
import org.tron.core.net.CypherNetService;
import org.tron.p2p.discover.Node;

@Slf4j(topic = "net")
@Component
public class NodePersistService {
  private static final byte[] DB_KEY_PEERS = "peers".getBytes();
  private static final long DB_COMMIT_RATE = 60 * 1000L;
  private static final int MAX_NODES_WRITE_TO_DB = 30;
  private final boolean isNodePersist = CommonParameter.getInstance().isNodeDiscoveryPersist();
  @Autowired
  private CommonStore commonStore;

  private ScheduledExecutorService nodePersistExecutor;

  private final String name = "NodePersistTask";

  public void init() {
    if (isNodePersist) {
      nodePersistExecutor = ExecutorServiceManager.newSingleThreadScheduledExecutor(name);
      nodePersistExecutor.scheduleAtFixedRate(this::dbWrite, DB_COMMIT_RATE, DB_COMMIT_RATE,
          TimeUnit.MILLISECONDS);
    }
  }

  public void close() {
    if (isNodePersist) {
      ExecutorServiceManager.shutdownAndAwaitTermination(nodePersistExecutor, name);
    }
  }

  public List<InetSocketAddress> dbRead() {
    List<InetSocketAddress> nodes = new ArrayList<>();
    try {
      byte[] nodeBytes = commonStore.get(DB_KEY_PEERS).getData();
      if (ByteArray.isEmpty(nodeBytes)) {
        return nodes;
      }
      DBNodes dbNodes = JsonUtil.json2Obj(new String(nodeBytes), DBNodes.class);
      logger.info("Read node from store: {} nodes", dbNodes.getNodes().size());
      dbNodes.getNodes().forEach(n -> nodes.add(new InetSocketAddress(n.getHost(), n.getPort())));
    } catch (Exception e) {
      logger.warn("DB read nodes failed, {}", e.getMessage());
    }
    return nodes;
  }

  private void dbWrite() {
    try {
      List<DBNode> batch = new ArrayList<>();
      List<Node> tableNodes = CypherNetService.getP2pService().getTableNodes();
      tableNodes.sort(Comparator.comparingLong(value -> -value.getUpdateTime()));
      for (Node n : tableNodes) {
        batch.add(
            new DBNode(n.getPreferInetSocketAddress().getAddress().getHostAddress(), n.getPort()));
      }

      if (batch.size() > MAX_NODES_WRITE_TO_DB) {
        batch = batch.subList(0, MAX_NODES_WRITE_TO_DB);
      }

      DBNodes dbNodes = new DBNodes();
      dbNodes.setNodes(batch);

      logger.info("Write nodes to store: {}/{} nodes", batch.size(), tableNodes.size());

      commonStore.put(DB_KEY_PEERS, new BytesCapsule(JsonUtil.obj2Json(dbNodes).getBytes()));
    } catch (Exception e) {
      logger.warn("DB write nodes failed, {}", e.getMessage());
    }
  }
}
