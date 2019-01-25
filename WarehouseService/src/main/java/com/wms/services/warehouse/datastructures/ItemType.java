package com.wms.services.warehouse.datastructures;

import java.security.PublicKey;

public class ItemType {
    //0入库单 1移库单 2出库单
   public static final int entryItem=0;
   public static final int transferItem=1;
   public static final int delierItem=2;
   public static final int stateForCreateTransferRecord=-1;
}
