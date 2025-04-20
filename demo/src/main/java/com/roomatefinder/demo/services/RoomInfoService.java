//package com.roomatefinder.demo.services;
//
//import com.roomatefinder.demo.dao.RoomInfoDAO;
//import com.roomatefinder.demo.models.RoomInfo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class RoomInfoService {
//
//    private final RoomInfoDAO roomInfoDAO;
//
//    @Autowired
//    public RoomInfoService(RoomInfoDAO roomInfoDAO) {
//        this.roomInfoDAO = roomInfoDAO;
//    }
//
//    public RoomInfo createRoom(RoomInfo roomInfo) {
//        return roomInfoDAO.save(roomInfo);
//    }
//
//    public RoomInfo updateRoom(RoomInfo roomInfo) {
//        if (roomInfo.getId() == null) {
//            throw new RuntimeException("Cannot update non-existent room");
//        }
//        return roomInfoDAO.save(roomInfo);
//    }
//
//    public RoomInfo saveRoom(RoomInfo roomInfo) {
//        return roomInfoDAO.save(roomInfo);
//    }
//
//    public RoomInfo getRoomById(Long id) {
//        return roomInfoDAO.findById(id).orElse(null);
//    }
//
//    public Optional<RoomInfo> findRoomById(Long id) {
//        return roomInfoDAO.findById(id);
//    }
//
//    public List<RoomInfo> getRoomsByUserId(String userId) {
//        return roomInfoDAO.findByUserId(userId);
//    }
//
//    public List<RoomInfo> findAvailableRooms() {
//        List<RoomInfo> roomsWithCapacity = roomInfoDAO.findRoomsWithAvailableCapacity();
//        LocalDate threeMonthsFromNow = LocalDate.now().plusMonths(3);
//
//        return roomsWithCapacity.stream()
//                .filter(room -> room.getAvailableFrom().isBefore(threeMonthsFromNow))
//                .collect(Collectors.toList());
//    }
//
//    public List<RoomInfo> findAvailableRoomsByCriteria(
//            Integer minBeds,
//            Integer maxRent,
//            Boolean subletAvailable,
//            String transportOptions
//    ) {
//        return findAvailableRooms().stream()
//                .filter(room ->
//                        (minBeds == null || room.getNumBeds() >= minBeds) &&
//                                (maxRent == null || room.getRent() <= maxRent) &&
//                                (subletAvailable == null ||
//                                        Boolean.TRUE.equals(room.getSubletAvailable()) == subletAvailable) &&
//                                (transportOptions == null ||
//                                        room.getTransportOptions().toLowerCase().contains(transportOptions.toLowerCase()))
//                )
//                .collect(Collectors.toList());
//    }
//
//    public void deleteRoom(Long id) {
//        roomInfoDAO.deleteById(id);
//    }
//
//    public List<RoomInfo> getAllRooms() {
//        return roomInfoDAO.findAll();
//    }
//}
package com.roomatefinder.demo.services;

import com.roomatefinder.demo.dao.RoomInfoDAO;
import com.roomatefinder.demo.models.RoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomInfoService {

    private final RoomInfoDAO roomInfoDao;

    @Autowired
    public RoomInfoService(RoomInfoDAO roomInfoDao) {
        this.roomInfoDao = roomInfoDao;
    }

    // Create a new room listing
    @Transactional
    public RoomInfo createRoom(RoomInfo roomInfo) {
        return roomInfoDao.save(roomInfo);
    }

    // Get all room listings
    public List<RoomInfo> getAllRooms() {
        return roomInfoDao.findAll();
    }

    // Get rooms by user ID
    public List<RoomInfo> getRoomsByUserId(String userId) {
        return roomInfoDao.findByUserId(userId);
    }

    // Get a room by ID
    public RoomInfo getRoomById(Long id) {
        return roomInfoDao.findById(id)
                .orElse(null); // Changed from orElseThrow to orElse(null) to match your error handling approach
    }

    // Update a room
    @Transactional
    public RoomInfo updateRoom(RoomInfo room) {
        if (room.getId() == null || !roomInfoDao.existsById(room.getId())) {
            throw new RuntimeException("Cannot update non-existent room");
        }
        return roomInfoDao.save(room);
    }

    // Delete a room
    @Transactional
    public void deleteRoom(Long id) {
        if (!roomInfoDao.existsById(id)) {
            throw new RuntimeException("Cannot delete non-existent room");
        }
        roomInfoDao.deleteById(id);
    }

    // Find available rooms with advanced filtering
    public List<RoomInfo> findAvailableRooms() {
        List<RoomInfo> roomsWithCapacity = roomInfoDao.findRoomsWithAvailableCapacity();
        LocalDate threeMonthsFromNow = LocalDate.now().plusMonths(3);

        return roomsWithCapacity.stream()
                .filter(room -> room.getAvailableFrom() != null && // Added null check
                        room.getAvailableFrom().isBefore(threeMonthsFromNow))
                .collect(Collectors.toList());
    }

    // Find available rooms by specific criteria
    public List<RoomInfo> findAvailableRoomsByCriteria(
            Integer minBeds,
            Integer maxRent,
            Boolean subletAvailable,
            String transportOptions
    ) {
        return findAvailableRooms().stream()
                .filter(room ->
                        (minBeds == null || room.getNumBeds() >= minBeds) &&
                                (maxRent == null || room.getRent() <= maxRent) &&
                                (subletAvailable == null ||
                                        (room.getSubletAvailable() != null && // Added null check
                                                Boolean.TRUE.equals(room.getSubletAvailable()) == subletAvailable)) &&
                                (transportOptions == null || transportOptions.isEmpty() || // Improved check
                                        (room.getTransportOptions() != null && // Added null check
                                                room.getTransportOptions().toLowerCase().contains(
                                                        transportOptions.toLowerCase())))
                )
                .collect(Collectors.toList());
    }

    // Enhanced filter method with additional criteria for the UI filter form
    public List<RoomInfo> findRoomsByAdvancedFilter(
            Integer minRent,
            Integer maxRent,
            Integer minBeds,
            Integer maxBeds,
            Float minBaths,
            Float maxBaths,
            String location,
            Boolean isFurnished,
            Boolean hasParking,
            Boolean hasLaundry,
            Boolean petsAllowed,
            Integer minSquareFeet,
            Integer maxSquareFeet,
            Boolean subletAvailable,
            String transportOptions,
            LocalDate availableFrom,
            LocalDate availableTo) {

        try {
            // Start with all available rooms
            List<RoomInfo> filteredRooms = findAvailableRooms();

            // Add debug logging to track the filtering process
            System.out.println("Initial room count: " + filteredRooms.size());

            // Apply each filter conditionally with better null safety
            if (minRent != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getRent() >= minRent)
                        .collect(Collectors.toList());
                System.out.println("After minRent filter: " + filteredRooms.size());
            }

            if (maxRent != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getRent() <= maxRent)
                        .collect(Collectors.toList());
                System.out.println("After maxRent filter: " + filteredRooms.size());
            }

            if (minBeds != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getNumBeds() >= minBeds)
                        .collect(Collectors.toList());
                System.out.println("After minBeds filter: " + filteredRooms.size());
            }

            if (maxBeds != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getNumBeds() <= maxBeds)
                        .collect(Collectors.toList());
                System.out.println("After maxBeds filter: " + filteredRooms.size());
            }

            if (minBaths != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getNumBaths() != null && room.getNumBaths() >= minBaths)
                        .collect(Collectors.toList());
                System.out.println("After minBaths filter: " + filteredRooms.size());
            }

            if (maxBaths != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getNumBaths() != null && room.getNumBaths() <= maxBaths)
                        .collect(Collectors.toList());
                System.out.println("After maxBaths filter: " + filteredRooms.size());
            }

            if (location != null && !location.trim().isEmpty()) {
                String searchTerm = location.toLowerCase().trim();
                filteredRooms = filteredRooms.stream()
                        .filter(room ->
                                (room.getCity() != null && room.getCity().toLowerCase().contains(searchTerm)) ||
                                        (room.getState() != null && room.getState().toLowerCase().contains(searchTerm)) ||
                                        (room.getZipCode() != null && room.getZipCode().toLowerCase().contains(searchTerm)) ||
                                        (room.getAddress() != null && room.getAddress().toLowerCase().contains(searchTerm)))
                        .collect(Collectors.toList());
                System.out.println("After location filter: " + filteredRooms.size());
            }

            if (isFurnished != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getIsFurnished() != null && Boolean.TRUE.equals(room.getIsFurnished()) == isFurnished)
                        .collect(Collectors.toList());
                System.out.println("After isFurnished filter: " + filteredRooms.size());
            }

            if (hasParking != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getHasParking() != null && Boolean.TRUE.equals(room.getHasParking()) == hasParking)
                        .collect(Collectors.toList());
                System.out.println("After hasParking filter: " + filteredRooms.size());
            }

            if (hasLaundry != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getHasLaundry() != null && Boolean.TRUE.equals(room.getHasLaundry()) == hasLaundry)
                        .collect(Collectors.toList());
                System.out.println("After hasLaundry filter: " + filteredRooms.size());
            }

            if (petsAllowed != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getPetsAllowed() != null && Boolean.TRUE.equals(room.getPetsAllowed()) == petsAllowed)
                        .collect(Collectors.toList());
                System.out.println("After petsAllowed filter: " + filteredRooms.size());
            }

            if (minSquareFeet != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getSquareFeet() != null && room.getSquareFeet() >= minSquareFeet)
                        .collect(Collectors.toList());
                System.out.println("After minSquareFeet filter: " + filteredRooms.size());
            }

            if (maxSquareFeet != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getSquareFeet() != null && room.getSquareFeet() <= maxSquareFeet)
                        .collect(Collectors.toList());
                System.out.println("After maxSquareFeet filter: " + filteredRooms.size());
            }

            if (subletAvailable != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getSubletAvailable() != null && Boolean.TRUE.equals(room.getSubletAvailable()) == subletAvailable)
                        .collect(Collectors.toList());
                System.out.println("After subletAvailable filter: " + filteredRooms.size());
            }

            if (transportOptions != null && !transportOptions.trim().isEmpty()) {
                String transportSearch = transportOptions.toLowerCase().trim();
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getTransportOptions() != null &&
                                room.getTransportOptions().toLowerCase().contains(transportSearch))
                        .collect(Collectors.toList());
                System.out.println("After transportOptions filter: " + filteredRooms.size());
            }

            if (availableFrom != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getAvailableFrom() != null &&
                                !room.getAvailableFrom().isBefore(availableFrom))
                        .collect(Collectors.toList());
                System.out.println("After availableFrom filter: " + filteredRooms.size());
            }

            if (availableTo != null) {
                filteredRooms = filteredRooms.stream()
                        .filter(room -> room.getAvailableFrom() != null &&
                                room.getAvailableFrom().isBefore(availableTo.plusDays(1)))
                        .collect(Collectors.toList());
                System.out.println("After availableTo filter: " + filteredRooms.size());
            }

            return filteredRooms;
        } catch (Exception e) {
            // Log exception
            System.err.println("Error in findRoomsByAdvancedFilter: " + e.getMessage());
            e.printStackTrace();
            // Return empty list rather than propagating the exception
            return List.of();
        }
    }

    // Find rooms by university proximity
    public List<RoomInfo> findRoomsByUniversityProximity(String universityName, Integer maxDistanceInMiles) {
        if (universityName == null || universityName.trim().isEmpty()) {
            return findAvailableRooms();
        }

        String university = universityName.toLowerCase().trim();

        return findAvailableRooms().stream()
                .filter(room -> room.getNearbyUniversities() != null &&
                        room.getNearbyUniversities().toLowerCase().contains(university) &&
                        (maxDistanceInMiles == null ||
                                (room.getDistanceToUniversity() != null &&
                                        room.getDistanceToUniversity() <= maxDistanceInMiles)))
                .collect(Collectors.toList());
    }

    // Find rooms by price range and sort by price
    public List<RoomInfo> findRoomsByPriceRange(Integer minPrice, Integer maxPrice, String sortOrder) {
        List<RoomInfo> filteredRooms = findAvailableRooms();

        if (minPrice != null) {
            filteredRooms = filteredRooms.stream()
                    .filter(room -> room.getRent() >= minPrice)
                    .collect(Collectors.toList());
        }

        if (maxPrice != null) {
            filteredRooms = filteredRooms.stream()
                    .filter(room -> room.getRent() <= maxPrice)
                    .collect(Collectors.toList());
        }

        // Sort by price
        if ("asc".equalsIgnoreCase(sortOrder)) {
            return filteredRooms.stream()
                    .sorted(Comparator.comparing(RoomInfo::getRent, Comparator.nullsLast(Integer::compareTo)))
                    .collect(Collectors.toList());
        } else if ("desc".equalsIgnoreCase(sortOrder)) {
            return filteredRooms.stream()
                    .sorted(Comparator.comparing(RoomInfo::getRent, Comparator.nullsLast(Integer::compareTo)).reversed())
                    .collect(Collectors.toList());
        }

        return filteredRooms;
    }
}