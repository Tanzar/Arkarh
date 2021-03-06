/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tanzar.Arkarh.Database.DAO.Abstracts;

import com.tanzar.Arkarh.Exceptions.QueryException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Tanzar
 */
@Repository
@Transactional
public abstract class DAO<entityType, containerType> {
    
    @PersistenceContext	
    private EntityManager entityManager;
    
    private String entityName;
    
    public DAO(String entityName){
        this.entityName = entityName;
    }
    
    public containerType getAll() {
        String hql = "from " + entityName;
        List<entityType> result = executeSelect(hql);
        return convertResults(result);
    }
    
    public int add(entityType object) {
        int id = -1;
        Session ses = this.entityManager.unwrap(Session.class);
        id = (int) ses.save(object);
        return id;
    }
    
    public int[] add(entityType[] entities){
        int[] ids = new int[entities.length];
        for(int i = 0; i < ids.length; i++){
            ids[i] = this.add(entities[i]);
        }
        return ids;
    }
    
    public void remove(entityType object) {
        Session ses = this.entityManager.unwrap(Session.class);
        if(this.entityManager.contains(object)){
            ses.delete(object);
        }
        else{
            ses.delete(this.entityManager.merge(object));
        }
    }
    
    public void update(entityType object) {
        Session ses = this.entityManager.unwrap(Session.class);
        ses.update(object);
    }
    
    private List<entityType> executeSelect(String sqlQuery){
        Query query = entityManager.createQuery(sqlQuery);
        List<entityType> resultList = query.getResultList();
        return resultList;
    }
    
    protected containerType get(String conditions) throws QueryException{
        if(conditions.length() > 5 && conditions.substring(0, 5).equals("where")){
            String hql = "from " + this.entityName + " " + conditions;
            List<entityType> result = executeSelect(hql);
            return convertResults(result);
        }
        else{
            throw new QueryException("Query must start with word \"where\"");
        }
    }
    
    protected abstract containerType convertResults(List<entityType> list);
}
