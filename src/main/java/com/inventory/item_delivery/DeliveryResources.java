package com.inventory.item_delivery;

import com.inventory.helper.ApiException;
import com.inventory.helper.DbConnection;
import com.inventory.helper.Enums;
import com.inventory.item_request_processing.model.Request;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static com.inventory.helper.ApiException.checkException;
import static com.inventory.item_request_processing.RequestResources.getRequestFromDBWithId;

@Path("/delivery")
@Tag(name = "Delivery")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class DeliveryResources {



    @POST
    @Path("/deliverItemToEmployee")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description =  "Request gets verified"
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(schema = @Schema(implementation = ApiException.class)),
                    description = "Errors/Exceptions")
    }
    )
    public Response assignItemToEmployee(@NotNull @RequestBody(description = "Request ID to Deliver", required = true,
            content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "Request ID", value = "1")})) Integer id) { // Request Id
        try {
            Request request = getRequestFromDBWithId(id, Enums.ItemStatus.WAITING_DELIVERY);
            changeRequestToDelivered(request.getId());
            int itemAssignmentId = getItemAssignmentId(request);
            deliver(itemAssignmentId);
            return Response.ok().build();
        } catch (Exception e) {
            return checkException(e);
        }
    }

    private int getItemAssignmentId(Request request) throws Exception {
        String query = "SELECT assignment_id FROM `item_assignment` where item_id = ? and employee_id = ? ORDER BY `item_assignment`.`assignment_id` DESC limit 1";
        ArrayList<Object[]> queryResult = DbConnection.selectFromDB(
                query,
                new Object[]{request.getItem().getId(), request.getEmployee().getId()});
        if(queryResult.size() == 0) throw new ApiException(ApiException.ExceptionCodes.MISSING_FIELD);
        return Integer.parseInt(queryResult.get(0)[0].toString());
    }


    private void changeRequestToDelivered(Integer id) throws Exception {
        String query = "UPDATE `request` SET `status` = ? WHERE `request`.`request_id` = ?;";
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(Enums.ItemStatus.DELIVERED.getStatus());
        parameters.add(id);
        DbConnection.insertUpdateToDB(query, parameters, parameters.size());
    }

    private void deliver(Integer id) throws Exception {
        String query = "UPDATE `delivery` SET `status` = ? WHERE `delivery`.`assigment_id` = ?;";
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(Enums.ItemStatus.DELIVERED.getStatus());
        parameters.add(id);
        DbConnection.insertUpdateToDB(query, parameters, parameters.size());
    }

}
