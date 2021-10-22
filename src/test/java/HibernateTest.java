import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import com.ex.ers.dataaccess.RequestRepository;
import com.ex.ers.dataaccess.UserRepository;
import com.ex.ers.models.Request;
import com.ex.ers.models.User;
import com.ex.ers.services.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HibernateTest {

    private static SessionFactory sessionFactory;

    @Before
    public void setup() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.setDateFormat(dateFormat);

        Configuration configuration = new Configuration().configure();

        if(configuration != null) {
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
        }
        System.out.println("SessionFactory created");
    }

    @After
    public void tearDown() {
        if (sessionFactory != null) sessionFactory.close();
        System.out.println("SessionFactory destroyed");
    }

    @Test
    public void testLogin() {
        System.out.println("Running testLogin...");
        UserRepository testrepo = new UserRepository(sessionFactory);
        User testUser = new User();
        testUser.setUsername("newuser");
        testUser.setPassword("KPUc46w(5");
        User loggeduser = testrepo.login(testUser);
        System.out.println("logged in "+ loggeduser.getUser_id());
        System.out.println("testuser "+ testUser.getUser_id());

        assertNotEquals("failed login test: ",loggeduser.getUser_id(), testUser.getUser_id());
    }

    @Test
    public void testRemoveUser() {
        System.out.println("Running testSaveUser...");
        UserRepository testrepo = new UserRepository(sessionFactory);
        User obj = new User();
        obj.setUsername("testingremove");
        obj.setPassword("123456");
        obj.setIsEmployee(false);
        obj.setEmail("lo1l@gmail.com");
        int userid = testrepo.save(obj);
        testrepo.delete(obj);
        assertEquals("failed testSaveUser test: ",testrepo.getById(userid), null);
    }

    @Test
    public void testGetUser() {
        System.out.println("Running testGetUser...");
        UserRepository testrepo = new UserRepository(sessionFactory);
        User loggeduser = testrepo.getById(8);
        System.out.println("logged in "+ loggeduser.getUser_id());
        assertEquals("failed get test: ",loggeduser.getUsername(), "newuser");
    }

    @Test
    public void testUpdateUser() {
        System.out.println("Running testUpdateUser...");
        UserRepository testrepo = new UserRepository(sessionFactory);
        UserService testService = new UserService(testrepo);
        User loggeduser = testService.getUser(9);

        String generatedpw = testService.generateCommonLangPassword();
        loggeduser.setPassword(generatedpw);
        System.out.println("generated"+ generatedpw);

        testService.changePassword(loggeduser);
        assertEquals("failed get test: ",testService.getUser(9).getPassword(), generatedpw);
    }

    @Test
    public void testGetRequest() {
        System.out.println("Running testGetRequest...");
        RequestRepository testrepo = new RequestRepository(sessionFactory);
        Request testReq = testrepo.getById(13);
        System.out.println("request = "+ testReq);
        assertEquals("failed get request test: ",testReq.getStatus(), "Denied");
    }

    @Test
    public void testDeleteRequest() {
        System.out.println("Running testSaveUser...");
        RequestRepository testrepo = new RequestRepository(sessionFactory);
        Request obj = new Request();
        obj.setDescription("123456");
        obj.setAmount(12344);
        obj.setRequestor_id(1);
        obj.setStatus("Pending");

        int reqid = testrepo.save(obj);
        testrepo.delete(obj);
        assertEquals("failed testSaveUser test: ",testrepo.getById(reqid), null);
    }


}