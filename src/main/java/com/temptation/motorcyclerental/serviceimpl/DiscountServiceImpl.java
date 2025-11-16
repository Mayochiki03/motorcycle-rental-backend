package com.temptation.motorcyclerental.serviceimpl;

import com.temptation.motorcyclerental.domain.Discounts;
import com.temptation.motorcyclerental.obj.request.DiscountRequest;
import com.temptation.motorcyclerental.obj.response.DiscountResponse;
import com.temptation.motorcyclerental.repo.DiscountRepository;
import com.temptation.motorcyclerental.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Override
    public DiscountResponse createDiscount(DiscountRequest request, String createdBy) {
        if (discountRepository.existsByDiscountCode(request.getDiscountCode())) {
            throw new RuntimeException("รหัสส่วนลดนี้ใช้งานแล้ว");
        }

        Discounts discount = new Discounts();
        discount.setDiscountId("DISC_" + UUID.randomUUID().toString().substring(0, 8));
        discount.setDiscountCode(request.getDiscountCode());
        discount.setDiscountType(com.temptation.motorcyclerental.domain.DiscountType.valueOf(request.getDiscountType()));
        discount.setDiscountValue(request.getDiscountValue());
        discount.setMinRentalDays(request.getMinRentalDays());
        discount.setMaxDiscountAmount(request.getMaxDiscountAmount());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setUsageLimit(request.getUsageLimit());
        discount.setCreatedBy(createdBy);

        Discounts savedDiscount = discountRepository.save(discount);
        return convertToResponse(savedDiscount);
    }

    @Override
    public List<DiscountResponse> getAllActiveDiscounts() {
        return discountRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DiscountResponse getDiscountByCode(String code) {
        Discounts discount = discountRepository.findByDiscountCode(code)
                .orElseThrow(() -> new RuntimeException("ไม่พบส่วนลด"));
        return convertToResponse(discount);
    }

    @Override
    public DiscountResponse updateDiscount(String id, DiscountRequest request) {
        Discounts discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบส่วนลด"));

        discount.setDiscountCode(request.getDiscountCode());
        discount.setDiscountType(com.temptation.motorcyclerental.domain.DiscountType.valueOf(request.getDiscountType()));
        discount.setDiscountValue(request.getDiscountValue());
        discount.setMinRentalDays(request.getMinRentalDays());
        discount.setMaxDiscountAmount(request.getMaxDiscountAmount());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setUsageLimit(request.getUsageLimit());

        Discounts updatedDiscount = discountRepository.save(discount);
        return convertToResponse(updatedDiscount);
    }

    @Override
    public void deactivateDiscount(String id) {
        Discounts discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบส่วนลด"));
        discount.setIsActive(false);
        discountRepository.save(discount);
    }

    @Override
    public boolean validateDiscount(String discountCode, LocalDate startDate, LocalDate endDate, int rentalDays) {
        return discountRepository.findValidDiscountByCode(discountCode, LocalDate.now())
                .map(discount -> {
                    // Check if rental days meet minimum requirement
                    if (discount.getMinRentalDays() != null && rentalDays < discount.getMinRentalDays()) {
                        return false;
                    }
                    // Check if discount is still valid for the booking dates
                    return !startDate.isBefore(discount.getStartDate()) && !endDate.isAfter(discount.getEndDate());
                })
                .orElse(false);
    }

    private DiscountResponse convertToResponse(Discounts discount) {
        DiscountResponse response = new DiscountResponse();
        response.setDiscountId(discount.getDiscountId());
        response.setDiscountCode(discount.getDiscountCode());
        response.setDiscountType(discount.getDiscountType().name());
        response.setDiscountValue(discount.getDiscountValue());
        response.setMinRentalDays(discount.getMinRentalDays());
        response.setMaxDiscountAmount(discount.getMaxDiscountAmount());
        response.setStartDate(discount.getStartDate());
        response.setEndDate(discount.getEndDate());
        response.setIsActive(discount.getIsActive());
        response.setCreatedBy(discount.getCreatedBy());
        response.setUsageLimit(discount.getUsageLimit());
        response.setUsedCount(discount.getUsedCount());
        response.setCreatedAt(discount.getCreatedAt());
        return response;
    }
}