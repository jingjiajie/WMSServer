package com.wms.services.ledger.datestructures;

import java.math.BigDecimal;

public class TaxCalculation {


    public int getTaxId() {
        return taxId;
    }


    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }


    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    BigDecimal moneyAmount;
    int taxId;
}
