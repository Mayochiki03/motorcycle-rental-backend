package com.temptation.motorcyclerental.repocustom;

import com.temptation.motorcyclerental.domain.Payments;
import com.temptation.motorcyclerental.objc.PaymentSearchCriteria;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PaymentCustomRepository {
    List<Payments> searchPayments(PaymentSearchCriteria criteria);
    Map<String, Object> getPaymentSummary(LocalDate startDate, LocalDate endDate);
    List<Object[]> getPaymentMethodStats(LocalDate startDate, LocalDate endDate);
}