package com.ex.ers.controllers;

import com.ex.ers.models.Request;
import com.ex.ers.models.User;
import com.ex.ers.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {
    private static ObjectMapper objectMapper;
    private static UserService userService;
    private static User current;
    private static Logger logger = LogManager.getLogger(UserController.class.getName());

    /**
     * retrieve userService and objectmapper from main and inject into user controller
     *<br>
     * @param context javalin app context
     */
    public static void init(Context context) {

        userService = context.appAttribute("userService");
        objectMapper = context.appAttribute("objectMapper");
    }

    /**
     * get json data from api endpoint/context and calls userservice to login for user
     *<br>
     * @param context javalin app context and json content from front end
     */
    public static void Login(Context context) {
        User json = null;
        try {
            json = objectMapper.readValue(context.bodyAsInputStream(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(json.getUsername());
        System.out.println(json.getPassword());
        User login = userService.login(json);
        current = login;
        System.out.println(current);
        if (current.getUser_id()!=0){
            context.status(201);
            context.header("http://localhost:8080/api/user/loginsuccess");
        }
        else{
            context.status(401);
            context.header("http://localhost:8080/api/user/loginfailed");

        }

    }

    /**
     * tells front end to redirect to dashboard
     *<br>
     * @param context javalin app context and json content from front end
     */
    public static void Homepage(Context context) throws JsonProcessingException {
        context.redirect("/dashboard");

    }

    /**
     * log out user and redirect to login page
     *<br>
     * @param context javalin app context and json content from front end
     */
    public static void Logout(Context context) {
        logger.info("user logout ");

        context.redirect("/");

    }
    /**
     * get user info when user first log in
     *<br>
     * @param context javalin app context and json content from front end
     */
    public static void getUser(Context context) {
        String json = "";
        try{
             json = objectMapper.writeValueAsString(current);

        } catch (IOException e) {
        e.printStackTrace();
        }
        System.out.println("getuser "+json);
        context.status(200);
        context.contentType("application/json");
        context.json(json);
    }

    /**
     * get json data from api endpoint/context and calls userservice to edit info for user
     *<br>
     * @param context javalin app context and json content from front end
     */
    public static void editInfo(Context context) {
        User json = null;
        try {
            json = objectMapper.readValue(context.bodyAsInputStream(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("editinfo"+json.getUsername());
        System.out.println(json.getPassword());
        User updatedacc = userService.editInfo(json);
        current = updatedacc;
        System.out.println("current"+current);
        System.out.println("json"+json);

        if (current.getUsername().equals(json.getUsername())){
            System.out.println("success");
            context.status(201);
        }
        else{
            context.status(401);

        }
    }
    /**
     * get json data from api endpoint/context and calls userservice to change password for user
     *<br>
     * @param context javalin app context and json content from front end, contains new user password
     */
    public static void changePassword(Context context) throws IOException {
        String newpass = objectMapper.readValue(context.bodyAsInputStream(),String.class);
        System.out.println("new password = "+newpass);
        User newpassUser = current;
        newpassUser.setPassword(newpass);
        current = userService.changePassword(newpassUser);
        System.out.println("current "+current.getPassword());
        System.out.println("newpass "+newpass);

        if (current.getPassword().equals(newpass)){
            System.out.println("success");
            context.status(201);
        }
        else{
            context.status(401);

        }
    }

    /**
     * get json data from api endpoint/context and calls userservice
     *<br>
     * to get all users for employee, only manager can call.
     * <br>
     * This method returns json to frontend
     * @param context javalin app context and json content from front end
     */
    public static void allEmployees(Context context) throws JsonProcessingException {
        User json = null;
        try {
            json = objectMapper.readValue(context.bodyAsInputStream(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("is manager"+json.isEmployee());
        List<User> allusers = new ArrayList<User>();;
        if(json.isEmployee()){
            allusers = userService.getAllUsers(json.getUser_id());
            System.out.println("all users:"+allusers);
            System.out.println("json"+json);
        }


        if (!allusers.isEmpty()){
            String allusersJson = "";

            allusers = allusers.stream().collect(Collectors.toList());
            allusersJson = objectMapper.writeValueAsString(allusers);
            context.status(200);
            context.contentType("application/json");
            context.json(allusersJson);
            System.out.println("success");
            context.status(201);
        }
        else{
            context.status(401);

        }
    }
    /**
     * get json data from api endpoint/context and calls userservice to add new user
     *<br>
     * @param context javalin app context and json content from front end, contains new employee info
     */
    public static void newEmployee(Context context) throws IOException {
        User json = null;
        try {
            json = objectMapper.readValue(context.bodyAsInputStream(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int newEmployee = userService.saveUser(json);
        if (newEmployee != 0){
            context.status(201);
        }
        else{
            context.status(401);
        }
    }

}
