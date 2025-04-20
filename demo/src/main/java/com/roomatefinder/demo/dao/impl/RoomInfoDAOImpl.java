//package com.roomatefinder.demo.dao.impl;
//
//import com.roomatefinder.demo.dao.RoomInfoDAO;
//import com.roomatefinder.demo.models.RoomInfo;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Root;
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//@Slf4j
//public class RoomInfoDAOImpl implements RoomInfoDAO {
//    private final SessionFactory sessionFactory;
//
//    @Autowired
//    public RoomInfoDAOImpl(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Override
//    public RoomInfo save(RoomInfo roomInfo) {
//        Session session = sessionFactory.openSession();
//        try {
//            session.beginTransaction();
//
//            // Log boolean values before saving
//            log.debug("BEFORE SAVE - Room ID: {}, Boolean values: isFurnished={}, hasParking={}, hasLaundry={}, petsAllowed={}",
//                    roomInfo.getId(),
//                    roomInfo.getIsFurnished(),
//                    roomInfo.getHasParking(),
//                    roomInfo.getHasLaundry(),
//                    roomInfo.getPetsAllowed());
//
//            if (roomInfo.getId() == null) {
//                // Persist as new entity
//                session.persist(roomInfo);
//                log.debug("New entity persisted");
//            } else {
//                // For updates, simply merge the entity
//                roomInfo = (RoomInfo) session.merge(roomInfo);
//                log.debug("Entity merged for update");
//            }
//
//            session.getTransaction().commit();
//            log.debug("Transaction committed");
//
//            // Log boolean values after saving
//            log.debug("AFTER SAVE - Room ID: {}, Boolean values: isFurnished={}, hasParking={}, hasLaundry={}, petsAllowed={}",
//                    roomInfo.getId(),
//                    roomInfo.getIsFurnished(),
//                    roomInfo.getHasParking(),
//                    roomInfo.getHasLaundry(),
//                    roomInfo.getPetsAllowed());
//
//            return roomInfo;
//        } catch (Exception e) {
//            log.error("Error saving RoomInfo entity", e);
//            session.getTransaction().rollback();
//            throw e;
//        } finally {
//            session.close();
//        }
//    }
//
//    @Override
//    public boolean existsById(Long id) {
//        try (Session session = sessionFactory.openSession()) {
//            RoomInfo roomInfo = session.get(RoomInfo.class, id);
//            return roomInfo != null;
//        } catch (Exception e) {
//            log.error("Error checking if RoomInfo exists by ID: " + id, e);
//            throw e;
//        }
//    }
//
//    @Override
//    public Optional<RoomInfo> findById(Long id) {
//        try (Session session = sessionFactory.openSession()) {
//            RoomInfo roomInfo = session.get(RoomInfo.class, id);
//            if (roomInfo != null) {
//                log.debug("Found RoomInfo - ID: {}, Boolean fields: isFurnished={}, hasParking={}, hasLaundry={}, petsAllowed={}",
//                        roomInfo.getId(),
//                        roomInfo.getIsFurnished(),
//                        roomInfo.getHasParking(),
//                        roomInfo.getHasLaundry(),
//                        roomInfo.getPetsAllowed());
//            }
//            return Optional.ofNullable(roomInfo);
//        } catch (Exception e) {
//            log.error("Error finding RoomInfo by ID: " + id, e);
//            throw e;
//        }
//    }
//
//    @Override
//    public List<RoomInfo> findByUserId(String userId) {
//        try (Session session = sessionFactory.openSession()) {
//            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//            CriteriaQuery<RoomInfo> criteriaQuery = criteriaBuilder.createQuery(RoomInfo.class);
//            Root<RoomInfo> root = criteriaQuery.from(RoomInfo.class);
//            criteriaQuery.where(criteriaBuilder.equal(root.get("userId"), userId));
//            List<RoomInfo> results = session.createQuery(criteriaQuery).getResultList();
//            log.debug("Found {} rooms for user ID: {}", results.size(), userId);
//            return results;
//        } catch (Exception e) {
//            log.error("Error finding RoomInfo by userId: " + userId, e);
//            throw e;
//        }
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        Session session = sessionFactory.openSession();
//        try {
//            session.beginTransaction();
//            RoomInfo roomInfo = session.get(RoomInfo.class, id);
//            if (roomInfo != null) {
//                log.debug("Deleting RoomInfo - ID: {}", id);
//                session.remove(roomInfo);
//            } else {
//                log.debug("RoomInfo not found for deletion - ID: {}", id);
//            }
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            log.error("Error deleting RoomInfo by ID: " + id, e);
//            session.getTransaction().rollback();
//            throw e;
//        } finally {
//            session.close();
//        }
//    }
//
//    @Override
//    public List<RoomInfo> findAll() {
//        try (Session session = sessionFactory.openSession()) {
//            List<RoomInfo> results = session.createQuery("FROM RoomInfo", RoomInfo.class).list();
//            log.debug("Found {} rooms in total", results.size());
//            return results;
//        } catch (Exception e) {
//            log.error("Error finding all RoomInfo entities", e);
//            throw e;
//        }
//    }
//
//    @Override
//    public List<RoomInfo> findRoomsWithAvailableCapacity() {
//        try (Session session = sessionFactory.openSession()) {
//            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//            CriteriaQuery<RoomInfo> criteriaQuery = criteriaBuilder.createQuery(RoomInfo.class);
//            Root<RoomInfo> root = criteriaQuery.from(RoomInfo.class);
//
//            // Rooms with available capacity have total_capacity > current_occupancy
//            criteriaQuery.where(
//                    criteriaBuilder.greaterThan(root.get("totalCapacity"), root.get("currentOccupancy"))
//            );
//
//            List<RoomInfo> results = session.createQuery(criteriaQuery).getResultList();
//            log.debug("Found {} rooms with available capacity", results.size());
//            return results;
//        } catch (Exception e) {
//            log.error("Error finding rooms with available capacity", e);
//            throw e;
//        }
//    }
//
//    @Override
//    public List<RoomInfo> findByLocationAndPriceRange(String location, Double minPrice, Double maxPrice) {
//        try (Session session = sessionFactory.openSession()) {
//            StringBuilder hql = new StringBuilder("FROM RoomInfo WHERE 1=1");
//
//            if (location != null && !location.trim().isEmpty()) {
//                hql.append(" AND (LOWER(city) LIKE LOWER(:location) OR LOWER(state) LIKE LOWER(:location) OR LOWER(address) LIKE LOWER(:location))");
//            }
//
//            if (minPrice != null) {
//                hql.append(" AND rent >= :minPrice");
//            }
//
//            if (maxPrice != null) {
//                hql.append(" AND rent <= :maxPrice");
//            }
//
//            Query<RoomInfo> query = session.createQuery(hql.toString(), RoomInfo.class);
//
//            if (location != null && !location.trim().isEmpty()) {
//                query.setParameter("location", "%" + location + "%");
//            }
//
//            if (minPrice != null) {
//                query.setParameter("minPrice", minPrice.intValue());
//            }
//
//            if (maxPrice != null) {
//                query.setParameter("maxPrice", maxPrice.intValue());
//            }
//
//            List<RoomInfo> results = query.list();
//            log.debug("Found {} rooms matching location: {} and price range: {}-{}",
//                    results.size(), location, minPrice, maxPrice);
//            return results;
//        } catch (Exception e) {
//            log.error("Error finding rooms by location and price range", e);
//            throw e;
//        }
//    }
//}

package com.roomatefinder.demo.dao.impl;

import com.roomatefinder.demo.dao.RoomInfoDAO;
import com.roomatefinder.demo.models.RoomInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class RoomInfoDAOImpl implements RoomInfoDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public RoomInfoDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public RoomInfo save(RoomInfo roomInfo) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            if (roomInfo.getId() == null) {
                session.persist(roomInfo);
                log.debug("New RoomInfo persisted with booleans: isFurnished={}, hasParking={}, hasLaundry={}, petsAllowed={}",
                        roomInfo.getIsFurnished(),
                        roomInfo.getHasParking(),
                        roomInfo.getHasLaundry(),
                        roomInfo.getPetsAllowed());
            } else {
                // Simply merge the entity; Hibernate will convert Boolean to tinyint(1) correctly.
                roomInfo = (RoomInfo) session.merge(roomInfo);
                log.debug("RoomInfo merged with booleans: isFurnished={}, hasParking={}, hasLaundry={}, petsAllowed={}",
                        roomInfo.getIsFurnished(),
                        roomInfo.getHasParking(),
                        roomInfo.getHasLaundry(),
                        roomInfo.getPetsAllowed());
            }
            session.getTransaction().commit();
            log.debug("Transaction committed for RoomInfo ID: {}", roomInfo.getId());
            return roomInfo;
        } catch (Exception e) {
            log.error("Error saving RoomInfo entity", e);
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean existsById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            RoomInfo roomInfo = session.get(RoomInfo.class, id);
            return roomInfo != null;
        } catch (Exception e) {
            log.error("Error checking if RoomInfo exists by ID: " + id, e);
            throw e;
        }
    }

    @Override
    public Optional<RoomInfo> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            RoomInfo roomInfo = session.get(RoomInfo.class, id);
            if (roomInfo != null) {
                log.debug("Found RoomInfo - ID: {}, booleans: isFurnished={}, hasParking={}, hasLaundry={}, petsAllowed={}",
                        roomInfo.getId(),
                        roomInfo.getIsFurnished(),
                        roomInfo.getHasParking(),
                        roomInfo.getHasLaundry(),
                        roomInfo.getPetsAllowed());
            }
            return Optional.ofNullable(roomInfo);
        } catch (Exception e) {
            log.error("Error finding RoomInfo by ID: " + id, e);
            throw e;
        }
    }

    @Override
    public List<RoomInfo> findByUserId(String userId) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<RoomInfo> criteriaQuery = criteriaBuilder.createQuery(RoomInfo.class);
            Root<RoomInfo> root = criteriaQuery.from(RoomInfo.class);
            criteriaQuery.where(criteriaBuilder.equal(root.get("userId"), userId));
            List<RoomInfo> results = session.createQuery(criteriaQuery).getResultList();
            log.debug("Found {} rooms for user ID: {}", results.size(), userId);
            return results;
        } catch (Exception e) {
            log.error("Error finding RoomInfo by userId: " + userId, e);
            throw e;
        }
    }

    @Override
    public void deleteById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            RoomInfo roomInfo = session.get(RoomInfo.class, id);
            if (roomInfo != null) {
                log.debug("Deleting RoomInfo - ID: {}", id);
                session.remove(roomInfo);
            } else {
                log.debug("RoomInfo not found for deletion - ID: {}", id);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error deleting RoomInfo by ID: " + id, e);
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<RoomInfo> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<RoomInfo> results = session.createQuery("FROM RoomInfo", RoomInfo.class).list();
            log.debug("Found {} rooms in total", results.size());
            return results;
        } catch (Exception e) {
            log.error("Error finding all RoomInfo entities", e);
            throw e;
        }
    }

    @Override
    public List<RoomInfo> findRoomsWithAvailableCapacity() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<RoomInfo> criteriaQuery = criteriaBuilder.createQuery(RoomInfo.class);
            Root<RoomInfo> root = criteriaQuery.from(RoomInfo.class);
            criteriaQuery.where(
                    criteriaBuilder.greaterThan(
                            root.get("totalCapacity"),
                            root.get("currentOccupancy")
                    )
            );
            List<RoomInfo> results = session.createQuery(criteriaQuery).getResultList();
            log.debug("Found {} rooms with available capacity", results.size());
            return results;
        } catch (Exception e) {
            log.error("Error finding rooms with available capacity", e);
            throw e;
        }
    }

    @Override
    public List<RoomInfo> findByLocationAndPriceRange(String location, Double minPrice, Double maxPrice) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM RoomInfo WHERE 1=1");
            if (location != null && !location.trim().isEmpty()) {
                hql.append(" AND (LOWER(city) LIKE LOWER(:location) OR LOWER(state) LIKE LOWER(:location) OR LOWER(address) LIKE LOWER(:location))");
            }
            if (minPrice != null) {
                hql.append(" AND rent >= :minPrice");
            }
            if (maxPrice != null) {
                hql.append(" AND rent <= :maxPrice");
            }
            Query<RoomInfo> query = session.createQuery(hql.toString(), RoomInfo.class);
            if (location != null && !location.trim().isEmpty()) {
                query.setParameter("location", "%" + location + "%");
            }
            if (minPrice != null) {
                query.setParameter("minPrice", minPrice.intValue());
            }
            if (maxPrice != null) {
                query.setParameter("maxPrice", maxPrice.intValue());
            }
            List<RoomInfo> results = query.list();
            log.debug("Found {} rooms matching location: {} and price range: {}-{}",
                    results.size(), location, minPrice, maxPrice);
            return results;
        } catch (Exception e) {
            log.error("Error finding rooms by location and price range", e);
            throw e;
        }
    }
}
