package com.sa.client_service.reviews.infrastructure.restadapter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String roomId;
    private Integer Rating;
    private String comment;
}
