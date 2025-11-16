package com.temptation.motorcyclerental.repocustom;

import com.temptation.motorcyclerental.domain.Reservations;
import com.temptation.motorcyclerental.objc.ReservationSearchCriteria;
import com.temptation.motorcyclerental.objc.ReportSearchCriteria;

import java.util.List;
import java.util.Map;

public interface ReservationCustomRepository {
    List<Reservations> searchReservations(ReservationSearchCriteria criteria);
    List<Object[]> getReservationStats(ReportSearchCriteria criteria);
    List<Object[]> getRevenueReport(ReportSearchCriteria criteria);
    Map<String, Long> getReservationStatusCounts();
}