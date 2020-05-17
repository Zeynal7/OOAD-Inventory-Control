package com.inventory.user_data_management;


import com.inventory.helper.ApiException;
import com.inventory.helper.Enums;
import com.inventory.item_management.model.Stock;
import com.inventory.user_data_management.model.Report;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.inventory.helper.ApiException.checkException;
import static com.inventory.item_management.ItemResources.getCurrentStock;

@Path("/report")
@Tag(name = "Reports")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ReportResources {

    @GET
    @Path("/generateReport")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Report.class)),
                    description = "Report of Specified Items"
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(schema = @Schema(implementation = ApiException.class)),
                    description = "Errors/Exceptions")
    }
    )
    public Response generateReport(@QueryParam("type") String type) {
        try {
            // Check Authorization, if user has access to requested report or not
            // TODO: If time allows add this authorization
            Enums.ReportType reportType = Enums.ReportType.valueOf(type);
            switch (reportType){
                case EMPLOYEE:
                    break;
                case MANAGER:
                    break;
                case COURIER:
                    break;
                case ADMIN:
                    break;
            }
            Stock stock = getCurrentStock();
            return Response.ok().entity(stock).build();
        } catch (Exception e) {
            return checkException(e);
        }
    }



}
