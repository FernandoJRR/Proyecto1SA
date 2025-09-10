package com.sa.client_service.reviews.application.usecases;

import java.util.List;
import java.util.Optional;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.reviews.application.dtos.FindReviewsDTO;
import com.sa.client_service.reviews.application.inputports.FindReviewsInputPort;
import com.sa.client_service.reviews.application.outputports.FindReviewsOutputPort;
import com.sa.client_service.reviews.domain.Review;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindReviewsUseCase implements FindReviewsInputPort {

    private final FindReviewsOutputPort findReviewsOutputPort;
    private final FindClientByCuiOutputPort findClientByCuiOutputPort;

    @Override
    public List<Review> handle(FindReviewsDTO dto) {
        if (dto.getClientCui() != null) {
            Optional<Client> optionalClient = findClientByCuiOutputPort.findByCui(dto.getClientCui());
            if (optionalClient.isPresent()) {
                dto.setClientId(optionalClient.get().getId());
            }
        }
        return findReviewsOutputPort.findReviews(dto);
    }

}
