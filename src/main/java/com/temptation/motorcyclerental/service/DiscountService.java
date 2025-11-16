package com.temptation.motorcyclerental.service;

import com.temptation.motorcyclerental.domain.Discounts;
import com.temptation.motorcyclerental.obj.request.DiscountRequest;
import com.temptation.motorcyclerental.obj.response.DiscountResponse;


import java.time.LocalDate;
import java.util.List;

public interface DiscountService {
    DiscountResponse createDiscount(DiscountRequest request, String createdBy);
    List<DiscountResponse> getAllActiveDiscounts();
    DiscountResponse getDiscountByCode(String code);
    DiscountResponse updateDiscount(String id, DiscountRequest request);
    void deactivateDiscount(String id);
    boolean validateDiscount(String discountCode, LocalDate startDate, LocalDate endDate, int rentalDays);
}