//package com.roomatefinder.demo.repositories;
//
//import com.roomatefinder.demo.models.UserPreferences;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
//    Optional<UserPreferences> findByIdAndUserId(Long id, String userId);
//    List<UserPreferences> findByUserId(String userId);
//}
//
