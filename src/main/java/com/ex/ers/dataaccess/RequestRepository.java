package com.ex.ers.dataaccess;

import com.ex.ers.connection.DBConnector;
import com.ex.ers.models.Request;
import com.ex.ers.models.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class RequestRepository implements Repository<Request,Integer> {
    private SessionFactory sessionFactory;

    /**
     * create an instance of request repository
     *<br>
     * @param sessionFactory the sessionfactory injected by main method
     */
    public RequestRepository(SessionFactory sessionFactory){
        this.sessionFactory=sessionFactory;
    }

    /**
     * Save a new request to the database
     *<br>
     * @param obj new request object that will be saved to database
     * @return the id of the newly saved request
     */
    @Override
    public Integer save(Request obj) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(obj);
        tx.commit();

        return obj.getReq_id();
    }
    /**
     * get a request from database by its id
     *<br>
     * @param integer request id
     * @return the request with specified if
     */
    @Override
    public Request getById(Integer integer) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Request request = (Request) session.get(Request.class, integer);

        tx.commit();
        return request;
    }

    /**
     * update a request from the database with new info
     *<br>
     * @param obj request object with new data
     * @return the updated request
     */
    @Override
    public Request update(Request obj) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(obj);
        tx.commit();
        session.close();
        return obj;

    }
    /**
     * delete a request from database
     *<br>
     * @param obj request for deletion
     */
    @Override
    public void delete(Request obj) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(obj);
        tx.commit();

    }
    /**
     * get all requests for a user or employee from database
     *<br>
     * @param id user id, used to identify user type
     * @return list of requests based on the user that is retrieving
     */
    @Override
    public List<Request> getAll(Integer id) {
        //if id==0 is employee
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query q;
        if (id.equals(0)){
            q = session.createQuery("select req from Request req order by submitted_date desc");

        }else{
            q = session.createQuery("select req from Request req where requestor_id = :id or resolver_id = :id order by submitted_date desc");
            q.setParameter("id", id);

        }
        List<Request> list = q.list();
        tx.commit();
        return list;

    }

    @Override
    public Request login(Request obj) {
        return null;
    }

}
