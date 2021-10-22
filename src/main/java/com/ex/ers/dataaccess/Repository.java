package com.ex.ers.dataaccess;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface Repository<U, ID>{
    /**
     * Save a new request or user to database
     *<br>
     * @param obj new request or user object
     * @return the id of the newly saved request or user
     */
    ID save(U obj);
    /**
     * get a user or request by its id
     *<br>
     * @param id the user or request id
     * @return an user or request with specified id
     */
    U getById(ID id);
    /**
     * update a user or request on the database
     *<br>
     * @param obj user or request object that will be updated on database
     * @return update user or request object
     */
    U update(U obj);
    /**
     * delete a user or request on the database
     *<br>
     * @param obj user or request object that will be deleted on database
     */
    void delete(U obj);
    /**
     * get all users or requests rows from the database
     *<br>
     * @param id user id
     * @return a list of all users or requests
     */
    List<U> getAll(ID id);
    /**
     * user login to the application and confirm credentials with database
     *<br>
     * @param obj user that is logging in
     * @return logged in user
     */
    U login(U obj);
}
