package com.ex.ers.controllers;

import com.ex.ers.models.Request;
import com.ex.ers.models.User;
import com.ex.ers.services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.javalin.http.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestController {
    private static ObjectMapper objectMapper;
    private static RequestService requestService;
    private static Logger logger = LogManager.getLogger(RequestController.class.getName());

    public static void init(Context context) {
        requestService = context.appAttribute("requestService");
        objectMapper = context.appAttribute("objectMapper");
        JavaTimeModule javaTimeModule=new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
    /**
     * get json data from api endpoint/context and calls requestservice to get all requests
     *<br>
     * send all requests as json
     * <br>
     * if user is manager, method returns all requests.
     * <br>
     * if user is employee, method returns requests made by this employee
     * @param context javalin app context and json content from front end, contains user info
     */
    public static void getAll(Context context) throws IOException {
        User json = objectMapper.readValue(context.bodyAsInputStream(), User.class);
        System.out.println("Read user "+json);

        List<Request> allRequests = new ArrayList<Request>();
        if(json.isEmployee()){
            allRequests = requestService.getAllRequests(0);

        }else{
            allRequests = requestService.getAllRequests(json.getUser_id());

        }

        String allRequestsJson = "";

        allRequests = allRequests.stream().collect(Collectors.toList());
        allRequestsJson = objectMapper.writeValueAsString(allRequests);
        context.status(200);
        context.contentType("application/json");
        context.json(allRequestsJson);


    }

    /**
     * get json data from api endpoint/context and calls requestservice to add new request
     *<br>
     * send success status code when request is added
     * @param context javalin app context and json content from front end, contains new request
     */
    public static void addRequest(Context context) {
        Request json = null;
        try {
            json = objectMapper.readValue(context.bodyAsInputStream(), Request.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int req_id = requestService.saveRequest(json);
        if (req_id!=0){
            context.status(201);
        }
        else{
            context.status(401);
        }
    }
    /**
     * get json data from api endpoint/context and calls requestservice to update an existing request
     *<br>
     * send success status code when request is update
     * @param context javalin app context and json content from front end, contains new request info
     */
    public static void updateRequest(Context context) {
        Request json = null;
        try {
            json = objectMapper.readValue(context.bodyAsInputStream(), Request.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("update request"+json.getReq_id());
        Request ref = requestService.getRequest(json.getReq_id());
        json.setSubmitted_date(ref.getSubmitted_date());
        Request updated = requestService.updateRequest(json);
        System.out.println("json"+json);

        if (!updated.getStatus().equals("Pending")){
            System.out.println("success");
            context.status(201);
        }
        else{
            context.status(401);

        }
    }
}
