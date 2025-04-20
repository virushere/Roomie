package com.roomatefinder.demo.dao;

import com.roomatefinder.demo.models.RoomInfo;
import java.util.List;
import java.util.Optional;

public interface RoomInfoDAO {
    RoomInfo save(RoomInfo roomInfo);
    Optional<RoomInfo> findById(Long id);
    boolean existsById(Long id);
    List<RoomInfo> findByUserId(String userId);
    void deleteById(Long id);
    List<RoomInfo> findAll();
    List<RoomInfo> findRoomsWithAvailableCapacity();
    List<RoomInfo> findByLocationAndPriceRange(String location, Double minPrice, Double maxPrice);
}