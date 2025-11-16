package com.temptation.motorcyclerental.domain;

import java.io.Serializable;

public class ReservationDiscountId implements Serializable {
    private String reservation;
    private String discount;

    // constructors, getters, setters, equals, hashCode
    public ReservationDiscountId() {}

    public ReservationDiscountId(String reservation, String discount) {
        this.reservation = reservation;
        this.discount = discount;
    }

}