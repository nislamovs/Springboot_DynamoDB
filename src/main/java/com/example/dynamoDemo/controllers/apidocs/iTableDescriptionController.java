package com.example.dynamoDemo.controllers.apidocs;

import com.example.dynamoDemo.domain.dtos.TableDescr;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;

public interface iTableDescriptionController {

    @Operation(summary = "Get table descriptions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns descriptions of all tables",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TableDescr.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tables were not found",
                    content = @Content)
    })
    Flux<?> getTableInfo();
}
