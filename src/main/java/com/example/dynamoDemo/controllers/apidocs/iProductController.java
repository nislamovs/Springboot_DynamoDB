package com.example.dynamoDemo.controllers.apidocs;

import com.example.dynamoDemo.domain.dtos.ProductDto;
import com.example.dynamoDemo.domain.exceptions.ProductNotFoundException;
import com.example.dynamoDemo.models.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface iProductController {

  @Operation(summary = "Get page of products")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns page of products",
          content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Product.class)
              )
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Products were not found",
          content = @Content)
  })
  Flux<?> getProducts(@Parameter(description = "Page number of products to be searched") long page,
                      @Parameter(description = "Page size of products to be searched") long size);

  @Operation(summary = "Get page of products")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns page of products",
          content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Product.class)
              )
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Category was not found",
          content = @Content)
  })
  Flux<?> getProductsByProductCategory(@Parameter(description = "Product category to be searched") final String productCategory);

  @Operation(summary = "Get product by id")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns product by id",
          content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Product.class)
              )
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Product was not found",
          content = @Content)
  })
  Mono<?> getProductById(@Parameter(description = "Id of product to be searched") final String id) throws ProductNotFoundException;

  @Operation(summary = "Get product count by category")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns product count, filtered by category",
          content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Long.class)
              )
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Category not found",
          content = @Content)
  })
  Mono<?> getCountByProductCategory(@Parameter(description = "Product category to be searched") final String productcat);

  @Operation(summary = "Get product count")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns product count",
          content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Long.class)
              )
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Product was not found",
          content = @Content)
  })
  Mono<?> getProductCount();

  @Operation(summary = "Delete product by id")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns acknowledge",
          content = @Content),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Product was not found",
          content = @Content)
  })
  Mono<?> deleteProduct(@Parameter(description = "Product Id") final String id) throws ProductNotFoundException;

  @Operation(summary = "Update product")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns acknowledge [product updated]",
          content = @Content),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Product was not found",
          content = @Content)
  })
  Mono<?> update(@Parameter(description = "Product Id") final String id,
                 @Parameter(description = "Product data") final ProductDto dto) throws ProductNotFoundException;

  @Operation(summary = "Adds new product")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns acknowledge [product created]",
          content = @Content),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content)
  })
  Mono<?> save(@Parameter(description = "New product") final ProductDto dto);
}
