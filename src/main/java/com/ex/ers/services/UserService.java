package com.ex.ers.services;

import com.ex.ers.dataaccess.Repository;
import com.ex.ers.models.Request;
import com.ex.ers.models.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class UserService {

    private Repository<User, Integer> userRepository;
    private Logger logger = LogManager.getLogger(UserService.class.getName());


    /**
     * The UserRepository dependency will be injected to the service.
     *<br>
     * Because the service shouldn't have to be concerned with how the Repository is created.
     * @param userRepository requestrepository
     */
    public UserService(Repository<User, Integer> userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @see com.ex.ers.dataaccess.UserRepository#save(User)
     */
    public Integer saveUser(User u) throws IOException {
        logger.info("Saving User", u);
        u.setPassword(this.generateCommonLangPassword());
        SendEmail(u);

        return userRepository.save(u);
    }
    /**
     * @see com.ex.ers.dataaccess.UserRepository#getById(Integer)
     */
    public User getUser(int id) {
        return userRepository.getById(id);
    }
    /**
     * @see com.ex.ers.dataaccess.UserRepository#getAll(Integer)
     */
    public List<User> getAllUsers(Integer id) {
        return (List<User>)userRepository.getAll(id);
    }
    /**
     * @see com.ex.ers.dataaccess.UserRepository#update(User)
     */
    public void deleteUser(User t) {
        if(t == null) return;
        this.userRepository.delete(t);
    }
    /**
     * @see com.ex.ers.dataaccess.UserRepository#update(User)
     */
    public void updateUser(User obj) {
        this.userRepository.update(obj);
    }
    /**
     * @see com.ex.ers.dataaccess.UserRepository#login(User)
     */
    public User login(User u){
        logger.info("New User Login: "+ u);
        return this.userRepository.login(u);
    }
    /**
     * @see com.ex.ers.dataaccess.UserRepository#update(User)
     */
    public User editInfo(User json) {
        logger.info("Editing user info: "+ json);

        return this.userRepository.update(json);

    }
    /**
     * @see com.ex.ers.dataaccess.UserRepository#update(User)
     */
    public User changePassword(User user) {
        logger.info("User changing password: "+ user);

        return this.userRepository.update(user);

    }
    /**
     * Generates a new password for new user registration
     *<br>
     * @return a random password
     */
    public String generateCommonLangPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        String password = pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }

    /**
     * send an email to new user after user registration
     *<br>
     * @param employee new user object with new information
     */
    public void SendEmail(User employee) throws IOException {
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        String username = getPropEmail().get(0);
        String password = getPropEmail().get(1);

        try{
            Session session = Session.getDefaultInstance(props,
                    new Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }});

            // -- Create a new message --
            Message msg = new MimeMessage(session);

            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress("jrzhjavatest@gmail.com"));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(employee.getEmail(),false));
            msg.setSubject("New User Registration");
            msg.setText("Your username:"+employee.getUsername()+", password:"+ employee.getPassword());
            msg.setSentDate(new Date());
            Transport.send(msg);
            System.out.println("Message sent.");
        }catch (MessagingException e){
            System.out.println("error cause: " + e);
        }
    }

    /**
     * get properties for email service,
     *<br>
     * retrieves username and password from prop file
     * @return a list containing email and password for email services
     */
    public List<String> getPropEmail() throws IOException {
        String username = "";
        String pass = "";


            Properties prop = new Properties();
            prop.load(ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties"));


            // get the property value and print it out
            username=prop.getProperty("emailuser");
            pass=prop.getProperty("emailpass");
            System.out.println(username);
            System.out.println(pass);


        List<String> props = new ArrayList<>();
        props.add(username);
        props.add(pass);
        return props;
    }
}

