package com.inventory.item_management;

import com.inventory.helper.ApiException;
import com.inventory.helper.DbConnection;
import com.inventory.item_management.model.Item;
import com.inventory.item_management.model.Stock;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static com.inventory.helper.ApiException.checkException;

@Path("/item")
@Tag(name = "Items")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ItemResources {

    @GET
    @Path("/getAvailableItems")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Stock.class)),
                    description = "Current Available Items"
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(schema = @Schema(implementation = ApiException.class)),
                    description = "Errors/Exceptions")
    }
    )
    public Response getAvailableItems() {
        try {
            Stock stock = getCurrentStock();
            return Response.ok(stock.getJson().toString()).build();
        } catch (Exception e) {
            return checkException(e);
        }
    }

    public static Stock getCurrentStock() throws Exception {
        ArrayList<Object[]> allItemRows = getItemsFromDB();
        Item[] items = parseItems(allItemRows);
        return mapAvailabilityToItems(items, allItemRows);
    }


    private static ArrayList<Object[]> getItemsFromDB() throws Exception {
        String query = "Select item_id, name, manufacture_date, status, description, item_image_urls, availability from available_items";
        return DbConnection.selectFromDB(query, new Object[]{});
    }

    private static Item[] parseItems(ArrayList<Object[]> allItemRows) {
        Item[] items = new Item[allItemRows.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new Item(allItemRows.get(i));
        }
        return items;
    }

    private static Stock mapAvailabilityToItems(Item[] items, ArrayList<Object[]> allItemRows) {
        Stock currentStock = new Stock();
        for (int i = 0; i < items.length; i++) {
            int itemAvailability = Integer.parseInt(allItemRows.get(i)[6].toString());
            currentStock.setItemsAvailability(items[i], itemAvailability);
        }
        return currentStock;
    }

    @POST
    @Path("/postItem")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Integer.class)),
                    description = "Added an Item to DB, returned its id"
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(schema = @Schema(implementation = ApiException.class)),
                    description = "Errors/Exceptions")
    }
    )
    public Response postItem(@NotNull @RequestBody(description = "Item to add.", required = true,
            content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "Item", value = "{\n" +
                            "    \"name\": \"LG Monitor 15\\\"\",\n" +
                            "    \"description\": \"lg monitor\",\n" +
                            "    \"manufactureDate\": \"2020-02-05\",\n" +
                            "    \"status\": \"available\"" +
                            "  }")})) Item item) {
        try {
            insertItemToDB(item);
            int insertedId = getInsertedId();
            return Response.ok(insertedId).build();
        } catch (Exception e) {
            return checkException(e);
        }
    }

    private void insertItemToDB(Item item) throws Exception {
        String query = "INSERT INTO `item` (`name`, `description`, `manufacture_date`, `status`) VALUES (?, ?, ?, ?);";
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(item.getName());
        parameters.add(item.getDescription());
        parameters.add(item.getManufactureDate());
        parameters.add(item.getStatus());
        DbConnection.insertUpdateToDB(query, parameters, parameters.size());
    }

    private int getInsertedId() throws Exception {
        String query = "SELECT item_id FROM `item` order by item_id desc limit 1";
        String itemId = DbConnection.selectFromDB(query, new Object[]{}).get(0)[0].toString();
        return Integer.parseInt(itemId);
    }

    @DELETE
    @Path("/deleteItem")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Delete an Item from DB"
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(schema = @Schema(implementation = ApiException.class)),
                    description = "Errors/Exceptions")
    }
    )
    public Response deleteItem(@NotNull @QueryParam("id") int id) {
        try {
            checkIfItemIsAssigned(id);
            deleteItemFromDB(id);
            return Response.ok().build();
        } catch (Exception e) {
            return checkException(e);
        }
    }

    private void deleteItemFromDB(int id) throws Exception {
        String query = "DELETE FROM `item` WHERE `item`.`item_id` = ?";
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(id);
        DbConnection.insertUpdateToDB(query, parameters, parameters.size());
    }

    private void checkIfItemIsAssigned(int id) throws Exception {
        String query = "SELECT assignment_id FROM `items_assigned_to_employee` where item_id = ?";
        ArrayList<Object[]> rows= DbConnection.selectFromDB(query, new Object[]{id});
        if(rows.size() > 0){
            throw new ApiException(ApiException.ExceptionCodes.ITEM_HAS_ASSIGNED_EMPLOYEE);
        }
    }

    @POST
    @Path("/setAvailability")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Change Availability of an Item on DB"
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(schema = @Schema(implementation = ApiException.class)),
                    description = "Errors/Exceptions")
    }
    )
    public Response setAvailability(@NotNull  @QueryParam("id") int id, @NotNull @QueryParam("availability") int availability) {
        try {
            if(availability < 0) throw new ApiException(ApiException.ExceptionCodes.UNKNOWN);
            int currentlyAvailable = getAvailabilityOfGivenID(id);
            int itemCountToAdd = availability - currentlyAvailable;
            if(itemCountToAdd != 0){
                changeAvailabilityBy(itemCountToAdd, id);
            }
            return Response.ok().build();
        } catch (Exception e) {
            return checkException(e);
        }
    }

    private void changeAvailabilityBy(int itemCountToAdd, int id) throws Exception {
        if(itemCountToAdd > 0){
            String query = "INSERT INTO `item_assignment` (`item_id`) VALUES (?)";
            ArrayList<Object> parameters = new ArrayList<>();
            for(int i = 0; i< itemCountToAdd; i++){
                parameters.add(id);
            }
            DbConnection.insertUpdateToDB(query, parameters, 1);
        }else{
            String query = "DELETE FROM `item_assignment` WHERE `item_assignment`.`item_id` = ? and `item_assignment`.`employee_id` is null LIMIT 1";
            ArrayList<Object> parameters = new ArrayList<>();
            for(int i = 0; i< (-itemCountToAdd); i++){
                parameters.add(id);
            }
            DbConnection.insertUpdateToDB(query, parameters, 1);
        }
    }

    private int getAvailabilityOfGivenID(int id) throws Exception {
        String query = "SELECT availability FROM `available_items` where item_id = ?";
        ArrayList<Object[]> availabilityRow= DbConnection.selectFromDB(query, new Object[]{id});
        if(availabilityRow.size() == 0) return 0;
        return Integer.parseInt(availabilityRow.get(0)[0].toString());
    }




}
