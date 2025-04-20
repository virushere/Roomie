//package com.roomatefinder.demo.repositories;
//
//import com.roomatefinder.demo.models.RoomInfo;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface RoomInfoRepository extends JpaRepository<RoomInfo, Long> {
//
//    // Find rooms by user ID
//    List<RoomInfo> findByUserId(String userId);
//
//    // Find rooms where current occupancy is less than total capacity
//    @Query("SELECT r FROM RoomInfo r WHERE r.currentOccupancy < r.totalCapacity")
//    List<RoomInfo> findRoomsWithAvailableCapacity();
//}