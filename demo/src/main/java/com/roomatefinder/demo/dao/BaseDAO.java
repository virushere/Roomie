//package com.roomatefinder.demo.dao;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import java.io.Serializable;
//import java.lang.reflect.ParameterizedType;
//import java.util.List;
//import java.util.Optional;
//
//public abstract class BaseDAO<T, ID extends Serializable> {
//
//    private final Class<T> entityClass;
//
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @SuppressWarnings("unchecked")
//    public BaseDAO() {
//        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
//                .getGenericSuperclass()).getActualTypeArguments()[0];
//    }
//
//    protected Session getCurrentSession() {
//        return sessionFactory.getCurrentSession();
//    }
//
//    protected EntityManager getEntityManager() {
//        return entityManager;
//    }
//
//    public T save(T entity) {
//        getCurrentSession().saveOrUpdate(entity);
//        return entity;
//    }
//
//    public Optional<T> findById(ID id) {
//        T entity = getCurrentSession().get(entityClass, id);
//        return Optional.ofNullable(entity);
//    }
//
//    public List<T> findAll() {
//        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
//        CriteriaQuery<T> cq = cb.createQuery(entityClass);
//        cq.from(entityClass);
//        return getCurrentSession().createQuery(cq).getResultList();
//    }
//
//    public void delete(T entity) {
//        getCurrentSession().delete(entity);
//    }
//
//    public void deleteById(ID id) {
//        T entity = getCurrentSession().get(entityClass, id);
//        if (entity != null) {
//            getCurrentSession().delete(entity);
//        }
//    }
//}