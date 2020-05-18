package com.inventory.user_data_management;


import com.inventory.helper.ApiException;
import com.inventory.helper.DbConnection;
import com.inventory.helper.Enums;
import com.inventory.item_management.model.Item;
import com.inventory.item_management.model.Stock;
import com.inventory.item_request_processing.model.Request;
import com.inventory.user_data_management.model.Report;
import com.inventory.user_management.model.Employee;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import static com.inventory.helper.ApiException.checkException;
import static com.inventory.item_management.ItemResources.getCurrentStock;
import static com.inventory.item_management.ItemResources.parseItems;

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
    @ExternalDocumentation(url = "/inventory_control/report-documentation.html")
    public Response generateReport(@NotNull @QueryParam("type") int type, @QueryParam("userId") Integer userId) {
        try {
            // Check Authorization, if user has access to requested report or not
            // TODO: If time allows add this authorization

            Stock stock = getCurrentStock();
            Enums.ReportType reportType = Enums.ReportType.valueFromInt(type);
            Report report;
            switch (reportType) {
                case EMPLOYEE:
                    // Return items of the employee.
                    Employee employee = new Employee(userId);
                    Item[] items = getItemsOfEmployee(userId);

                    Map<Employee, List<Item>> employeeToItemsMap = new HashMap<>();
                    employeeToItemsMap.putIfAbsent(employee, Arrays.asList(items));

                    report = new Report(
                            employeeToItemsMap,
                            stock
                    );

                    return Response.ok().entity(report.getJson().toString()).build();
                case MANAGER:
                    // Return items of all of the employees.
                    report = getManagerReport(stock);


                    return Response.ok().entity(report.getJson().toString()).build();
                case COURIER:
                    // Return items to be delivered.
                    ArrayList<Request> requests = getNotDeliveredRequests();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("waiting-delivery", requests);
                    return Response.ok().entity(jsonObject.toString()).build();
                case ADMIN:
                    report = getManagerReport(stock);
                    ArrayList<Request> requests2 = getNotDeliveredRequests();
                    JSONObject jsonObject2 = report.getJson();
                    jsonObject2.put("waiting-delivery", requests2);
                    return Response.ok().entity(jsonObject2.toString()).build();
            }

            return Response.ok().entity(stock).build();
        } catch (Exception e) {
            return checkException(e);
        }
    }

    private ArrayList<Request> getNotDeliveredRequests() throws Exception {
        Employee[] employeesWaitingDelivery = getEmployeesWaitingDelivery();
        return getDeliveryWaitingRequests(employeesWaitingDelivery);
    }

    private Report getManagerReport(Stock stock) throws Exception {
        Report report;
        Employee[] employees = getEmployeesWithItem();

        Map<Employee, List<Item>> employeesToItemsMap = new HashMap<>();

        for (Employee currentEmployee : employees) {
            employeesToItemsMap.putIfAbsent(currentEmployee,
                    Arrays.asList(getItemsOfEmployee(currentEmployee.getId())));
        }

        report = new Report(
                employeesToItemsMap,
                stock
        );
        return report;
    }

    private Item[] getItemsOfEmployee(Integer employeeId) throws Exception {
        String query = "SELECT  item_id, name, manufacture_date, status, description, item_image_urls FROM `items_assigned_to_employee` where employee_id = ? ORDER BY `items_assigned_to_employee`.`assingment_date` DESC";
        ArrayList<Object[]> itemRows = DbConnection.selectFromDB(query, new Object[]{employeeId});
        return parseItems(itemRows);
    }

    private Employee[] getEmployeesWithItem() throws Exception {
        String query = "SELECT user_id, fullname, email, department, supervisor_id FROM `employees_with_items`\n";
        return getEmployees(query);
    }

    private Employee[] getEmployeesWaitingDelivery() throws Exception {
        String query = "SELECT user_id, fullname, email, department, supervisor_id FROM `employees_waiting_delivery`\n";
        return getEmployees(query);
    }

    private Employee[] getEmployees(String query) throws Exception {
        ArrayList<Object[]> idRows = DbConnection.selectFromDB(query, new Object[]{});
        Employee[] employees = new Employee[idRows.size()];
        for (int i = 0; i < employees.length; i++) {
            employees[i] = new Employee(idRows.get(i));
        }
        return employees;
    }

    private ArrayList<Request> getDeliveryWaitingRequests(Employee[] employees) throws Exception {
        ArrayList<Request> requests = new ArrayList<>();
        for (Employee employee : employees) {
            String query = "SELECT item_id, name, manufacture_date, status, description, item_image_urls," +
                    " request_id, request_date FROM `delivery_waiting_items`  where employee_id = ? ORDER BY `request_date`";
            ArrayList<Object[]> requestRows = DbConnection.selectFromDB(query, new Object[]{employee.getId()});
            for (Object[] requestRow : requestRows) {
                Item itemToDeliver = new Item(requestRow);
                int requestId = Integer.parseInt(requestRow[6].toString());
                String date = Objects.toString(requestRow[7], "");
                requests.add(new Request(requestId, employee, itemToDeliver, date, Enums.ItemStatus.WAITING_DELIVERY.getStatus()));
            }
        }
        return requests;
    }


}
