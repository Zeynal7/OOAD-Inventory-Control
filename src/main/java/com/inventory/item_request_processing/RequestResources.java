package com.inventory.item_request_processing;


import com.inventory.helper.ApiException;
import com.inventory.helper.DbConnection;
import com.inventory.helper.Enums;
import com.inventory.item_management.model.Item;
import com.inventory.item_request_processing.model.Request;
import com.inventory.user_management.model.Employee;
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

@Path("/request")
@Tag(name = "Requests")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RequestResources {

    @POST
    @Path("/postItemRequest")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description =  "Request an Item on DB"
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(schema = @Schema(implementation = ApiException.class)),
                    description = "Errors/Exceptions")
    }
    )
    public Response postItemRequest(@NotNull @RequestBody(description = "Item to request.", required = true,
            content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "Request", value = "{\n" +
                            "   \"employee\": {\n" +
                            "      \"id\": 1\n" +
                            "   },\n" +
                            "   \"item\": {\n" +
                            "      \"id\": 2\n" +
                            "   }\n" +
                            "}}")})) Request request) {
        try {
            insertRequestToDB(request);
            return Response.ok().build();
        } catch (Exception e) {
            return checkException(e);
        }
    }

    private void insertRequestToDB(Request request) throws Exception {
        String query = "INSERT INTO `request`(`item_id`, `employee_id`, `request_date`, `status`) VALUES (?, ?, ?, ?)";
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(request.getItem().getId());
        parameters.add(request.getEmployee().getId());
        parameters.add(request.getRequestDate());
        parameters.add(Enums.ItemStatus.WAITING_APPROVAL.getStatus());
        DbConnection.insertUpdateToDB(query, parameters, parameters.size());
    }


    @POST
    @Path("/postRequestReview")
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
    public Response postRequestReview(@NotNull @RequestBody(description = "Request id to verify/reject. Status can be Approved or Rejected", required = true,
            content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "Request", value = "{\n" +
                            "   \"id\":1,\n" +
                            "   \"status\":\"Approved\"\n" +
                            "}")})) Request request) {
        try {
            String query = "UPDATE `request` SET `status` =  ? WHERE `request`.`request_id` = ?;";
            ArrayList<Object> parameters = new ArrayList<>();
            Enums.ItemStatus itemStatus = Enums.ItemStatus.valueFromString(request.getStatus());
            if(itemStatus != Enums.ItemStatus.REJECTED && itemStatus != Enums.ItemStatus.APPROVED){
                throw new ApiException(ApiException.ExceptionCodes.MISSING_FIELD);
            }
            parameters.add(itemStatus.getStatus());
            parameters.add(request.getId());
            DbConnection.insertUpdateToDB(query, parameters, parameters.size());
            return Response.ok().build();
        } catch (Exception e) {
            return checkException(e);
        }
    }


    @POST
    @Path("/assignItemToEmployee")
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
    public Response assignItemToEmployee(@NotNull @RequestBody(description = "Assign Item To An Employee", required = true,
            content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "Request", value = "{\n" +
                            "   \"id\":1\n" +
                            "}")})) Request request) {
        try {
            Request requestObjectFromDB = getRequestFromDBWithId(request.getId(), Enums.ItemStatus.APPROVED);
            int availableAssignmentId = checkIfItemIsAvailable(requestObjectFromDB.getItem().getId());
            assignItem(availableAssignmentId, requestObjectFromDB);
            changeRequestToWaitingDelivery(requestObjectFromDB.getId());
            insertDeliveryToDB(availableAssignmentId, requestObjectFromDB);
            return Response.ok().build();
        } catch (Exception e) {
            return checkException(e);
        }
    }

    public static Request getRequestFromDBWithId(Integer id, Enums.ItemStatus itemStatus) throws Exception {
        String query = "SELECT * FROM `request` where request_id = ? and status = ?";
        ArrayList<Object[]> queryResult = DbConnection.selectFromDB(query, new Object[]{id, itemStatus.getStatus()});
        if(queryResult.size() == 0) throw new ApiException(ApiException.ExceptionCodes.MISSING_FIELD);
        int employeeId = Integer.parseInt(queryResult.get(0)[2].toString());
        int itemId = Integer.parseInt(queryResult.get(0)[1].toString());
        String requestDate = queryResult.get(0)[3].toString();
        String status = queryResult.get(0)[4].toString();
        return new Request(
                id,
                new Employee(employeeId),
                new Item(itemId),
                requestDate,
                status
        );
    }



    private void insertDeliveryToDB(int assignmentId, Request requestObjectFromDB) throws Exception {
        String query = "INSERT INTO `delivery`(`assigment_id`, `employee_id`, `courier_id`, `status`) VALUES (?,?,?,?)";
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(assignmentId);
        parameters.add(requestObjectFromDB.getEmployee().getId());
        parameters.add(1);
        parameters.add(Enums.ItemStatus.WAITING_DELIVERY.getStatus());
        DbConnection.insertUpdateToDB(query, parameters, parameters.size());
    }

    private void changeRequestToWaitingDelivery(Integer id) throws Exception {
        String query = "UPDATE `request` SET `status` = ? WHERE `request`.`request_id` = ?;";
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(Enums.ItemStatus.WAITING_DELIVERY.getStatus());
        parameters.add(id);
        DbConnection.insertUpdateToDB(query, parameters, parameters.size());
    }


    private void assignItem(int availableItemId, Request request) throws Exception {
        String query = "UPDATE `item_assignment` SET `employee_id` = ?, `assignment_date` = ?, `manager_id` = ? WHERE `item_assignment`.`assignment_id` = ?;";
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(request.getEmployee().getId());
        parameters.add(request.getRequestDate());
        parameters.add(1); // Default 1 for now.
        parameters.add(availableItemId);
        DbConnection.insertUpdateToDB(query, parameters, parameters.size());
    }

    private int checkIfItemIsAvailable(Integer id) throws Exception {
        String query = "SELECT assignment_id FROM `item_assignment` where employee_id is null and item_id = ?";
        ArrayList<Object[]> queryResult = DbConnection.selectFromDB(query, new Object[]{id});
        if(queryResult.size() == 0){
            throw new ApiException(ApiException.ExceptionCodes.ITEM_IS_NOT_AVAILABLE_FOR_REQUEST);
        }
        return Integer.parseInt(queryResult.get(0)[0].toString());
    }

}
