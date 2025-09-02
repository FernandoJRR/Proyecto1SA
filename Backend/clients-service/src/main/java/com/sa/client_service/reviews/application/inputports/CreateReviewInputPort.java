package com.sa.client_service.reviews.application.inputports;

import com.sa.client_service.reviews.application.dtos.CreateReviewDTO;
import com.sa.client_service.reviews.domain.Review;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;

public interface CreateReviewInputPort {
    public Review handle(@Valid CreateReviewDTO createReviewDTO) throws NotFoundException;
}
