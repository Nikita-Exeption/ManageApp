package org.nikita.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayLoad(
        @NotNull(message = "{product_title_is_invalid}")
        @Size(min = 3, max = 50, message = "{product_title_is_invalid}")
        String title,

        @NotNull(message = "{product_details_is_invalid}")
        @Size(min = 1, max = 1000, message = "{product_details_is_invalid}")
        String details) {
}
