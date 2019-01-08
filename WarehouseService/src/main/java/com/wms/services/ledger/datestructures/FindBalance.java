package com.wms.services.ledger.datestructures;

import java.math.BigDecimal;

public class FindBalance {
    BigDecimal Balance=BigDecimal.ZERO;

    public BigDecimal getBalance() {
        return Balance;
    }

    public Boolean getExistBalance() {
        return ExistBalance;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }

    public void setExistBalance(Boolean existBalance) {
        ExistBalance = existBalance;
    }

    Boolean ExistBalance=false;
}
