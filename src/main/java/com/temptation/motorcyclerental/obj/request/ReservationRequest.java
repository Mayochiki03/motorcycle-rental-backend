package com.temptation.motorcyclerental.obj.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequest {
    private String motorcycleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String returnLocation;
    private String specialRequests;
    private String discountCode; // สำหรับใส่โค้ดส่วนลด
}