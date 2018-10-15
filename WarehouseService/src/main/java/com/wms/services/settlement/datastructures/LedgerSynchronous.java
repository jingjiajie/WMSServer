package com.wms.services.settlement.datastructures;

import java.util.List;

public class LedgerSynchronous {
    private List<Integer> settlementNoteIds;
    private int personId;
    private Integer accountPeriodId;

    public List<Integer> getSettlementNoteIds() {
        return settlementNoteIds;
    }

    public int getPersonId() {
        return personId;
    }

    public Integer getAccountPeriodId() {
        return accountPeriodId;
    }

    public void setSettlementNoteIds(List<Integer> settlementNoteIds) {
        this.settlementNoteIds = settlementNoteIds;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setAccountPeriodId(Integer accountPeriodId) {
        this.accountPeriodId = accountPeriodId;
    }

}
