package com.ex.ers.services;

import com.ex.ers.dataaccess.Repository;
import com.ex.ers.models.Request;
import com.ex.ers.models.User;
import io.javalin.http.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RequestService {
    private Repository<Request, Integer> requestRepository;
    private Logger logger = LogManager.getLogger(RequestService.class.getName());



    /**
     * The RequestRepository dependency will be injected to the service.
     *<br>
     * Because the service shouldn't have to be concerned with how the Repository is created.
     * @param requestRepository requestrepository
     */
    public RequestService(Repository<Request, Integer> requestRepository) {
        this.requestRepository = requestRepository;
    }

    /**
     * @see com.ex.ers.dataaccess.RequestRepository#save(Request)
     */
    public Integer saveRequest(Request r) {
        logger.info("Saving Request", r);
        return requestRepository.save(r);
    }
    /**
     * @see com.ex.ers.dataaccess.RequestRepository#getById(Integer)
     */
    public Request getRequest(int id) {
        return requestRepository.getById(id);
    }
    /**
     * @see com.ex.ers.dataaccess.RequestRepository#getAll(Integer)
     */
    public List<Request> getAllRequests(Integer id) {
        return (List<Request>)requestRepository.getAll(id);
    }
    /**
     * @see com.ex.ers.dataaccess.RequestRepository#delete(Request)
     */
    public void deleteRequest(Request r) {
        if(r == null) return;
        this.requestRepository.delete(r);
    }
    /**
     * @see com.ex.ers.dataaccess.RequestRepository#update(Request)
     */
    public Request updateRequest(Request r) {
        logger.info("updating Request", r);

        return this.requestRepository.update(r);
    }


}
