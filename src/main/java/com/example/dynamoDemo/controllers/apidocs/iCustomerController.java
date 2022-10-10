package com.example.dynamoDemo.controllers.apidocs;

import com.example.dynamoDemo.domain.dtos.CustomerDto;
import com.example.dynamoDemo.domain.exceptions.CustomerNotFoundException;
import com.example.dynamoDemo.domain.exceptions.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

public interface iCustomerController {

  @Operation(summary = "Get page of customers")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns page of customers",
          content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = CustomerDto.class)
              )
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Customers were not found",
          content = @Content)
  })
  Flux<?> getCustomers(@Parameter(description = "Page number of customers to be searched") long page,
                      @Parameter(description = "Page size of customers to be searched") long size);

  @Operation(summary = "Get page of customers")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns customer with email",
          content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = CustomerDto.class)
              )
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Email was not found",
          content = @Content)
  })
  Flux<?> getCustomerByEmail(@Parameter(description = "Email to be searched") final String email);

  @Operation(summary = "Get customer by id")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns customer by id",
          content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = CustomerDto.class)
              )
          }),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Customer was not found",
          content = @Content)
  })
  Mono<?> getCustomerById(@Parameter(description = "Id of customer to be searched") final String id) throws CustomerNotFoundException, ExecutionException, InterruptedException;

  @Operation(summary = "Get customer count")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns customer count",
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
          description = "Customer was not found",
          content = @Content)
  })
  Mono<?> getCustomerCount();

  @Operation(summary = "Delete customer by id")
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
          description = "Customer was not found",
          content = @Content)
  })
  Mono<?> deleteCustomer(@Parameter(description = "Customer Id") final String id) throws CustomerNotFoundException, ProductNotFoundException, ExecutionException, InterruptedException;

  @Operation(summary = "Update customer")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns acknowledge [customer updated]",
          content = @Content),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Customer was not found",
          content = @Content)
  })
  Mono<?> updateCustomer(@Parameter(description = "Customer Id") final String id,
                 @Parameter(description = "Customer data") final CustomerDto dto) throws CustomerNotFoundException, ExecutionException, InterruptedException;

  @Operation(summary = "Adds new customer")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Returns acknowledge [customer created]",
          content = @Content),
      @ApiResponse(
          responseCode = "400",
          description = "Bad request",
          content = @Content)
  })
  Mono<?> saveCustomer(@Parameter(description = "New customer") final CustomerDto dto);

  @Operation(summary = "Generate new customers")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "202",
                  description = "Returns acknowledge"),
          @ApiResponse(
                  responseCode = "400",
                  description = "Bad request",
                  content = @Content)
  })
  Mono<?> generateCustomers(@Parameter(description = "Number of new customers") final Integer count);
}
