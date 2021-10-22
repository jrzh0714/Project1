package com.ex.ers;

import com.ex.ers.connection.DBConnector;
import com.ex.ers.connection.PostgreSQLDBConnector;
import com.ex.ers.controllers.RequestController;
import com.ex.ers.controllers.UserController;
import com.ex.ers.dataaccess.Repository;
import com.ex.ers.dataaccess.RequestRepository;
import com.ex.ers.dataaccess.UserRepository;
import com.ex.ers.models.Request;
import com.ex.ers.models.User;
import com.ex.ers.services.RequestService;
import com.ex.ers.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Main {


    public static void main(String[] args){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.setDateFormat(dateFormat);

        SessionFactory sessionFactory = null;
        Configuration configuration = new Configuration().configure();

        if(configuration != null) {
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
        }

        Repository<User,Integer> userrepo = new UserRepository(sessionFactory);
        UserService userService = new UserService(userrepo);
        Repository<Request,Integer> reqrepo = new RequestRepository(sessionFactory);
        RequestService requestService = new RequestService(reqrepo);

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public", Location.CLASSPATH);

        }).start(8080);

        app.before(UserController::init);
        app.before(RequestController::init);

        app.attribute("userService",userService);
        app.attribute("requestService",requestService);
        app.attribute("objectMapper",objectMapper);

        app.get("/api/user/loginsuccess",UserController::Homepage);
        app.get("/api/user/getuser",UserController::getUser);
        app.get("/api/user/logout",UserController::Logout);

        app.post("/api/user/edit",UserController::editInfo);
        app.post("/api/user/changePassword",UserController::changePassword);
        app.post("/api/user/login", UserController::Login);

        app.post("/api/manager/allEmployees", UserController::allEmployees);
        app.post("/api/manager/newEmployee", UserController::newEmployee);

        app.post("/api/request/getall", RequestController::getAll);
        app.post("/api/request/add", RequestController::addRequest);
        app.post("/api/request/update", RequestController::updateRequest);


    }

}
