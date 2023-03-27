package com.kraievskyi.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class DataLine {
    private String serviceId;
    private String variationId;
    private String questionTypeId;
    private String categoryId;
    private String subCategoryId;
    private String responseType;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Long time;
}
