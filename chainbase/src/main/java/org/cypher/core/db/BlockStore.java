package org.tron.core.db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tron.common.error.CypherDBException;
import org.tron.common.utils.Sha256Hash;
import org.tron.core.capsule.BlockCapsule;
import org.tron.core.capsule.BlockCapsule.BlockId;
import org.tron.core.exception.BadItemException;

@Slf4j(topic = "DB")
@Component
public class BlockStore extends CypherStoreWithRevoking<BlockCapsule> {

  @Autowired
  private BlockStore(@Value("block") String dbName) {
    super(dbName);
  }

  public List<BlockCapsule> getLimitNumber(long startNumber, long limit) {
    BlockId startBlockId = new BlockId(Sha256Hash.ZERO_HASH, startNumber);
    return pack(revokingDB.getValuesNext(startBlockId.getBytes(), limit));
  }

  public List<BlockCapsule> getBlockByLatestNum(long getNum) {
    return pack(revokingDB.getlatestValues(getNum));
  }

  private List<BlockCapsule> pack(Set<byte[]> values) {
    List<BlockCapsule> blocks = new ArrayList<>();
    for (byte[] bytes : values) {
      try {
        blocks.add(new BlockCapsule(bytes));
      } catch (BadItemException e) {
        logger.error("Find bad item: {}", e.getMessage());
        // throw new CypherDBException(e);
      }
    }
    blocks.sort(Comparator.comparing(BlockCapsule::getNum));
    return blocks;
  }
}
