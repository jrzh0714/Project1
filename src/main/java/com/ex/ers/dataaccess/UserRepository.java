package com.ex.ers.dataaccess;

import com.ex.ers.connection.DBConnector;
import com.ex.ers.models.Request;
import com.ex.ers.models.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserRepository implements Repository<User,Integer> {
    private SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory){
        this.sessionFactory=sessionFactory;
    }
    /**
     * login and confirm credentials with database
     *<br>
     * @param obj the user logging in to the application
     * @return the logged-in user with user details
     */
    public User login(User obj){
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query q = session.createQuery("select user from User user where user.username = :username");
        q.setParameter("username", obj.getUsername());
        List userinfo = q.list();
        User user = (User) userinfo.get(0);
        System.out.println(user.getPassword());
        System.out.println(obj.getPassword());

        if (!(user.getPassword().equals(obj.getPassword()))){
            user=null;
        }
        tx.commit();
        System.out.println(user);

        return user;

    }

    /**
     * save a new user to database
     *<br>
     * @param obj new user object that will be saved to database
     * @return the id of the newly added user
     */
    @Override
    public Integer save(User obj) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(obj);
        tx.commit();

        return obj.getUser_id();
    }
    /**
     * get a user by the user id
     *<br>
     * @param id user id
     * @return user object with specified id
     */
    @Override
    public User getById(Integer id) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        User user = (User) session.get(User.class, id);

        tx.commit();
        return user;

    }

    /**
     * update a user in the database
     *<br>
     * @param obj user object with updated info
     * @return updated user retrieved from database
     */
    @Override
    public User update(User obj) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        User updateuser = new User();
        updateuser.setUser_id(obj.getUser_id());
        updateuser.setUsername(obj.getUsername());
        updateuser.setPassword(obj.getPassword());
        updateuser.setEmail(obj.getEmail());
        updateuser.setIsEmployee(obj.isEmployee());
        session.update(updateuser);
        tx.commit();
        session.close();
        return this.getById(obj.getUser_id());
    }

    /**
     * delete a user from database
     *<br>
     * @param obj user that will be deleted
     */
    @Override
    public void delete(User obj) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(obj);
        tx.commit();
    }
    /**
     * get all users from database, only accessible by manager
     *<br>
     * @param id user id of the manager
     * @return list of users
     */
    @Override
    public List<User> getAll(Integer id) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query q = session.createQuery("select user from User user order by id asc");
        List<User> list = q.list();
        tx.commit();
        return list;
    }
}
