package com.example.dynamoDemo.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    private String id;
    private Boolean inPublication;
    private String isbn;
    private Integer pageCount;
    private Integer price;
    private List<String> authors;
    private String productCategory;
    private String title;
    private String dimensions;
    private String brand;
    private String description;
    private List<String> color;
    private String bicycleType;
}
