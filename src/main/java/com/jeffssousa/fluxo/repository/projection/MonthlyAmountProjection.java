package com.jeffssousa.fluxo.repository.projection;

import java.math.BigDecimal;

public interface MonthlyAmountProjection {

    Integer getMes();
    BigDecimal getTotal();
}
