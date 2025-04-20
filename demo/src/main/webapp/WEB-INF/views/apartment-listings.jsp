<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apartment Listings</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .filter-sidebar {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
        }
        .room-card {
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            transition: transform 0.2s;
            margin-bottom: 20px;
        }
        .room-card:hover {
            transform: translateY(-5px);
        }
        .room-image {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
        }
        .feature-badge {
            margin-right: 5px;
            margin-bottom: 5px;
        }
        .filter-heading {
            font-weight: 600;
            margin-top: 15px;
            margin-bottom: 8px;
        }
        .filter-section {
            border-bottom: 1px solid #e3e3e3;
            padding-bottom: 15px;
            margin-bottom: 15px;
        }
        .filter-toggle {
            cursor: pointer;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home/listings">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="Roommate Finder Logo" width="30" height="30" class="d-inline-block align-text-top me-2">
                Roommate Finder
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="listingsDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            Listings
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="listingsDropdown">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/home/roommate-listings">Roommate Listings</a></li>
                            <li><a class="dropdown-item active" href="${pageContext.request.contextPath}/home/apartment-listings">Apartment Listings</a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/messages/inbox">
                            Messages
                            <c:if test="${unreadMessageCount > 0}">
                                <span class="badge rounded-pill bg-danger">${unreadMessageCount}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/users/profile">My Profile</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/users/my-listings">My Listings</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <a href="${pageContext.request.contextPath}/auth/logout" class="btn btn-outline-light">Logout</a>
                </div>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h1 class="mb-4">Find Your Perfect Apartment</h1>

        <div class="row">
            <!-- Filter Sidebar -->
            <div class="col-md-3 mb-4">
                <div class="filter-sidebar">
                    <h4 class="mb-3">Filter Apartments</h4>
                    <form id="filterForm" method="get" action="${pageContext.request.contextPath}/home/apartment-listings">
                        <!-- Basic Filters Section -->
                        <div class="filter-section">
                            <h5 class="filter-toggle" data-bs-toggle="collapse" data-bs-target="#basicFilters">
                                <i class="bi bi-caret-down-fill"></i> Basic Filters
                            </h5>
                            <div id="basicFilters" class="collapse show">
                                <!-- Rent Range Filter -->
                                <div class="mb-3">
                                    <label class="filter-heading">Rent Range ($)</label>
                                    <div class="row">
                                        <div class="col-6">
                                            <div class="input-group mb-2">
                                                <span class="input-group-text">$</span>
                                                <input type="number" class="form-control" name="minRent"
                                                       value="${minRentFilter}" placeholder="Min">
                                            </div>
                                        </div>
                                        <div class="col-6">
                                            <div class="input-group mb-2">
                                                <span class="input-group-text">$</span>
                                                <input type="number" class="form-control" name="maxRent"
                                                       value="${maxRentFilter}" placeholder="Max">
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Bedrooms Filter -->
                                <div class="mb-3">
                                    <label class="filter-heading">Bedrooms</label>
                                    <div class="row">
                                        <div class="col-6">
                                            <select class="form-select" name="minBeds">
                                                <option value="">Min</option>
                                                <option value="1" ${minBedsFilter == 1 ? 'selected' : ''}>1+</option>
                                                <option value="2" ${minBedsFilter == 2 ? 'selected' : ''}>2+</option>
                                                <option value="3" ${minBedsFilter == 3 ? 'selected' : ''}>3+</option>
                                                <option value="4" ${minBedsFilter == 4 ? 'selected' : ''}>4+</option>
                                                <option value="5" ${minBedsFilter == 5 ? 'selected' : ''}>5+</option>
                                            </select>
                                        </div>
                                        <div class="col-6">
                                            <select class="form-select" name="maxBeds">
                                                <option value="">Max</option>
                                                <option value="1" ${maxBedsFilter == 1 ? 'selected' : ''}>1</option>
                                                <option value="2" ${maxBedsFilter == 2 ? 'selected' : ''}>2</option>
                                                <option value="3" ${maxBedsFilter == 3 ? 'selected' : ''}>3</option>
                                                <option value="4" ${maxBedsFilter == 4 ? 'selected' : ''}>4</option>
                                                <option value="5" ${maxBedsFilter == 5 ? 'selected' : ''}>5</option>
                                                <option value="6" ${maxBedsFilter == 6 ? 'selected' : ''}>6+</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <!-- Bathrooms Filter -->
                                <div class="mb-3">
                                    <label class="filter-heading">Bathrooms</label>
                                    <div class="row">
                                        <div class="col-6">
                                            <select class="form-select" name="minBaths">
                                                <option value="">Min</option>
                                                <option value="1" ${minBathsFilter == 1 ? 'selected' : ''}>1+</option>
                                                <option value="1.5" ${minBathsFilter == 1.5 ? 'selected' : ''}>1.5+</option>
                                                <option value="2" ${minBathsFilter == 2 ? 'selected' : ''}>2+</option>
                                                <option value="2.5" ${minBathsFilter == 2.5 ? 'selected' : ''}>2.5+</option>
                                                <option value="3" ${minBathsFilter == 3 ? 'selected' : ''}>3+</option>
                                            </select>
                                        </div>
                                        <div class="col-6">
                                            <select class="form-select" name="maxBaths">
                                                <option value="">Max</option>
                                                <option value="1" ${maxBathsFilter == 1 ? 'selected' : ''}>1</option>
                                                <option value="1.5" ${maxBathsFilter == 1.5 ? 'selected' : ''}>1.5</option>
                                                <option value="2" ${maxBathsFilter == 2 ? 'selected' : ''}>2</option>
                                                <option value="2.5" ${maxBathsFilter == 2.5 ? 'selected' : ''}>2.5</option>
                                                <option value="3" ${maxBathsFilter == 3 ? 'selected' : ''}>3</option>
                                                <option value="3.5" ${maxBathsFilter == 3.5 ? 'selected' : ''}>3.5+</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <!-- Location Filter -->
                                <div class="mb-3">
                                    <label for="locationKeyword" class="filter-heading">Location</label>
                                    <input type="text" class="form-control" id="locationKeyword" name="locationKeyword"
                                           value="${locationFilter}" placeholder="City, State, or Zip">
                                </div>
                            </div>
                        </div>

                        <!-- Features & Amenities Section -->
                        <div class="filter-section">
                            <h5 class="filter-toggle" data-bs-toggle="collapse" data-bs-target="#featuresFilters">
                                <i class="bi bi-caret-down-fill"></i> Features & Amenities
                            </h5>
                            <div id="featuresFilters" class="collapse">
                                <div class="form-check mb-2">
                                    <input class="form-check-input" type="checkbox" id="isFurnished"
                                           name="isFurnished" value="true" ${isFurnishedFilter ? 'checked' : ''}>
                                    <label class="form-check-label" for="isFurnished">
                                        Furnished
                                    </label>
                                </div>

                                <div class="form-check mb-2">
                                    <input class="form-check-input" type="checkbox" id="hasParking"
                                           name="hasParking" value="true" ${hasParkingFilter ? 'checked' : ''}>
                                    <label class="form-check-label" for="hasParking">
                                        Parking Available
                                    </label>
                                </div>

                                <div class="form-check mb-2">
                                    <input class="form-check-input" type="checkbox" id="hasLaundry"
                                           name="hasLaundry" value="true" ${hasLaundryFilter ? 'checked' : ''}>
                                    <label class="form-check-label" for="hasLaundry">
                                        In-unit Laundry
                                    </label>
                                </div>

                                <div class="form-check mb-2">
                                    <input class="form-check-input" type="checkbox" id="petsAllowed"
                                           name="petsAllowed" value="true" ${petsAllowedFilter ? 'checked' : ''}>
                                    <label class="form-check-label" for="petsAllowed">
                                        Pets Allowed
                                    </label>
                                </div>

                                <div class="form-check mb-2">
                                    <input class="form-check-input" type="checkbox" id="subletAvailable"
                                           name="subletAvailable" value="true" ${subletAvailableFilter ? 'checked' : ''}>
                                    <label class="form-check-label" for="subletAvailable">
                                        Sublet Available
                                    </label>
                                </div>

                                <div class="mb-3 mt-3">
                                    <label for="transportOptions" class="filter-heading">Transport Options</label>
                                    <input type="text" class="form-control" id="transportOptions"
                                           name="transportOptions" value="${transportOptionsFilter}"
                                           placeholder="e.g., Bus, Train, Subway">
                                </div>
                            </div>
                        </div>

                        <!-- Advanced Filters Section -->
                        <div class="filter-section">
                            <h5 class="filter-toggle" data-bs-toggle="collapse" data-bs-target="#advancedFilters">
                                <i class="bi bi-caret-right-fill"></i> Advanced Filters
                            </h5>
                            <div id="advancedFilters" class="collapse">
                                <!-- Square Footage -->
                                <div class="mb-3">
                                    <label class="filter-heading">Square Footage</label>
                                    <div class="row">
                                        <div class="col-6">
                                            <input type="number" class="form-control" name="minSquareFeet"
                                                   value="${minSquareFeetFilter}" placeholder="Min sq ft">
                                        </div>
                                        <div class="col-6">
                                            <input type="number" class="form-control" name="maxSquareFeet"
                                                   value="${maxSquareFeetFilter}" placeholder="Max sq ft">
                                        </div>
                                    </div>
                                </div>

                                <!-- Available Date Range -->
                                <div class="mb-3">
                                    <label class="filter-heading">Available Date Range</label>
                                    <div class="row">
                                        <div class="col-6">
                                            <label class="form-label small">From</label>
                                            <input type="date" class="form-control" name="availableFrom"
                                                   value="${availableFromFilter}">
                                        </div>
                                        <div class="col-6">
                                            <label class="form-label small">To</label>
                                            <input type="date" class="form-control" name="availableTo"
                                                   value="${availableToFilter}">
                                        </div>
                                    </div>
                                </div>

                                <!-- Sort By -->
                                <div class="mb-3">
                                    <label for="sortBy" class="filter-heading">Sort By</label>
                                    <select class="form-select" id="sortBy" name="sortBy">
                                        <option value="">Default</option>
                                        <option value="priceAsc" ${sortByFilter == 'priceAsc' ? 'selected' : ''}>Price: Low to High</option>
                                        <option value="priceDesc" ${sortByFilter == 'priceDesc' ? 'selected' : ''}>Price: High to Low</option>
                                        <option value="bedsDesc" ${sortByFilter == 'bedsDesc' ? 'selected' : ''}>Most Bedrooms</option>
                                        <option value="dateAsc" ${sortByFilter == 'dateAsc' ? 'selected' : ''}>Available Soonest</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- Filter Buttons -->
                        <div class="d-grid gap-2 mt-4">
                            <button type="submit" class="btn btn-primary">Apply Filters</button>
                            <a href="${pageContext.request.contextPath}/home/apartment-listings" class="btn btn-outline-secondary">Reset Filters</a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Apartment Listings -->
            <div class="col-md-9">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <c:if test="${not empty rooms}">
                        <p class="mb-0 text-muted">Found ${fn:length(rooms)} listings</p>
                    </c:if>
                    <div class="dropdown">
                        <button class="btn btn-outline-secondary dropdown-toggle" type="button" id="sortDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                            Sort By
                        </button>
                        <ul class="dropdown-menu" aria-labelledby="sortDropdown">
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="applySorting('priceAsc')">Price: Low to High</a></li>
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="applySorting('priceDesc')">Price: High to Low</a></li>
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="applySorting('bedsDesc')">Most Bedrooms</a></li>
                            <li><a class="dropdown-item" href="javascript:void(0)" onclick="applySorting('dateAsc')">Available Soonest</a></li>
                        </ul>
                    </div>
                </div>

                <div class="row">
                    <c:if test="${not empty rooms}">
                        <c:forEach items="${rooms}" var="room">
                            <div class="col-md-6 col-lg-4 mb-4">
                                <div class="card room-card h-100">
                                    <!-- Room Image -->
                                    <c:choose>
                                        <%-- Updated: Use hasImage() instead of hasRoomImage() --%>
                                        <c:when test="${room.hasImage()}">
                                            <img src="data:${room.imageType};base64,${room.base64Image}"
                                                 class="room-image" alt="Room Picture">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/img/default-apartment.jpg"
                                                 class="room-image" alt="Default Room">
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="card-body">
                                        <div class="d-flex justify-content-between">
                                            <h5 class="card-title">
                                                ${not empty room.numBeds ? room.numBeds : '1'} Bed Apartment
                                            </h5>
                                            <h6 class="card-subtitle text-success">${room.rent}/mo</h6>
                                        </div>

                                        <p class="card-text text-muted mb-2">
                                            <i class="bi bi-geo-alt"></i>
                                            <c:if test="${not empty room.address}">${room.address}, </c:if>
                                            <c:if test="${not empty room.city}">${room.city}, </c:if>
                                            <c:if test="${not empty room.state}">${room.state} </c:if>
                                            <c:if test="${not empty room.zipCode}">${room.zipCode}</c:if>
                                        </p>

                                        <p class="card-text mb-2">
                                            <span class="badge bg-secondary me-1">
                                                <i class="bi bi-door-open"></i> ${room.numBeds} bed<c:if test="${room.numBeds > 1}">s</c:if>
                                            </span>
                                            <c:if test="${not empty room.numBaths}">
                                                <span class="badge bg-secondary me-1">
                                                    <i class="bi bi-droplet"></i> ${room.numBaths} bath<c:if test="${room.numBaths > 1}">s</c:if>
                                                </span>
                                            </c:if>
                                            <c:if test="${not empty room.squareFeet}">
                                                <span class="badge bg-secondary">
                                                    <i class="bi bi-rulers"></i> ${room.squareFeet} sq ft
                                                </span>
                                            </c:if>
                                        </p>

                                        <p class="card-text text-muted small">
                                            <i class="bi bi-calendar"></i> Available:
                                            <c:if test="${not empty room.availableFrom}">
                                                ${room.availableFrom}
                                            </c:if>
                                        </p>

                                        <c:if test="${not empty room.description}">
                                            <p class="card-text">
                                                <c:choose>
                                                    <c:when test="${fn:length(room.description) > 100}">
                                                        ${fn:substring(room.description, 0, 97)}...
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${room.description}
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </c:if>

                                        <!-- Room Features -->
                                        <div class="mt-3">
                                            <div class="d-flex flex-wrap">
                                                <c:if test="${room.isFurnished}">
                                                    <span class="badge bg-info feature-badge">
                                                        <i class="bi bi-box-seam"></i> Furnished
                                                    </span>
                                                </c:if>

                                                <c:if test="${room.hasParking}">
                                                    <span class="badge bg-info feature-badge">
                                                        <i class="bi bi-p-square"></i> Parking
                                                    </span>
                                                </c:if>

                                                <c:if test="${room.hasLaundry}">
                                                    <span class="badge bg-info feature-badge">
                                                        <i class="bi bi-wind"></i> Laundry
                                                    </span>
                                                </c:if>

                                                <c:if test="${room.petsAllowed}">
                                                    <span class="badge bg-info feature-badge">
                                                        <i class="bi bi-piggy-bank"></i> Pets allowed
                                                    </span>
                                                </c:if>

                                                <c:if test="${room.subletAvailable}">
                                                    <span class="badge bg-info feature-badge">
                                                        <i class="bi bi-people"></i> Sublet avail.
                                                    </span>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="card-footer">
                                        <a href="${pageContext.request.contextPath}/apartments/details/${room.id}"
                                           class="btn btn-outline-primary btn-sm w-100">
                                            View Details
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>

                    <!-- No Results Message -->
                    <c:if test="${empty rooms}">
                        <div class="text-center p-5">
                            <i class="bi bi-house-slash" style="font-size: 3rem;"></i>
                            <h3 class="mt-3">No apartments found</h3>
                            <p class="text-muted">Try adjusting your filters or check back later for new listings.</p>
                            <a href="${pageContext.request.contextPath}/home/apartment-listings" class="btn btn-primary mt-2">Reset Filters</a>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Toggle filter sections
        document.addEventListener('DOMContentLoaded', function() {
            const filterToggles = document.querySelectorAll('.filter-toggle');

            filterToggles.forEach(toggle => {
                toggle.addEventListener('click', function() {
                    const icon = this.querySelector('i');
                    if (icon.classList.contains('bi-caret-down-fill')) {
                        icon.classList.remove('bi-caret-down-fill');
                        icon.classList.add('bi-caret-right-fill');
                    } else {
                        icon.classList.remove('bi-caret-right-fill');
                        icon.classList.add('bi-caret-down-fill');
                    }
                });
            });

            // Form elements
            const filterForm = document.getElementById('filterForm');

            // Clear empty form fields on submit to keep URL clean
            filterForm.addEventListener('submit', function(event) {
                const formElements = Array.from(filterForm.elements);

                formElements.forEach(element => {
                    if (element.type !== 'submit' && element.type !== 'button') {
                        if (!element.value || element.value.trim() === '') {
                            element.disabled = true;
                        }
                    }
                });

                // Re-enable all form elements after form submission
                setTimeout(() => {
                    formElements.forEach(element => {
                        element.disabled = false;
                    });
                }, 100);
            });
        });

        // Apply sorting
        function applySorting(sortOption) {
            // Get current URL and parameters
            const url = new URL(window.location.href);
            const params = new URLSearchParams(url.search);
            // Update or add the sortBy parameter
            params.set('sortBy', sortOption);
            // Redirect to the new URL with the sort parameter
            window.location.href = `${url.pathname}?${params.toString()}`;
        }
    </script>
</body>
</html>
