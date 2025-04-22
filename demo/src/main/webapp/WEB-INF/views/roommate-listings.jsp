<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Roommate Finder - Roommate Listings</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        .card {
            transition: transform 0.3s;
            margin-bottom: 20px;
            border: none;
            border-radius: 12px;
            box-shadow: 0 6px 18px rgba(0,0,0,0.08);
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.12);
        }
        .card-header {
            background-color: #0d6efd;
            color: white;
            border-radius: 12px 12px 0 0 !important;
            padding: 15px 20px;
        }
        .current-user-card .card-header {
            background-color: #198754; /* Bootstrap success green */
        }
        .preferences-badge {
            margin-right: 5px;
            margin-bottom: 5px;
            font-size: 0.85rem;
        }
        .bio-text {
            font-size: 0.95rem;
            color: #555;
        }
        .profile-image {
            width: 120px;
            height: 120px;
            object-fit: cover;
            border-radius: 8px;
            border: 3px solid #f8f9fa;
        }
        .roommate-details {
            display: flex;
        }
        .profile-image-container {
            padding-right: 20px;
        }
        .detail-item {
            margin-bottom: 8px;
        }
        .detail-label {
            font-weight: 600;
            color: #495057;
        }
        .current-user-badge {
            position: absolute;
            top: -10px;
            right: -10px;
            background-color: #dc3545; /* Bootstrap danger red */
            color: white;
            font-size: 0.8rem;
            padding: 5px 10px;
            border-radius: 15px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
            z-index: 10;
        }
        /* Lazy loading styles */
        .loading-indicator {
            display: none;
            text-align: center;
            padding: 20px;
            margin: 20px 0;
        }
        .loading-spinner {
            width: 40px;
            height: 40px;
            border: 4px solid rgba(0, 0, 0, 0.1);
            border-radius: 50%;
            border-top-color: #0d6efd;
            animation: spin 1s ease-in-out infinite;
            margin: 0 auto;
        }
        @keyframes spin {
            to { transform: rotate(360deg); }
        }
        .skeleton {
            background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
            background-size: 200% 100%;
            animation: skeleton-loading 1.5s infinite;
            border-radius: 8px;
        }
        .skeleton-card {
            height: 350px;
            margin-bottom: 20px;
        }
        @keyframes skeleton-loading {
            0% { background-position: 200% 0; }
            100% { background-position: -200% 0; }
        }
        .no-more-results {
            text-align: center;
            padding: 15px;
            margin: 20px 0;
            color: #6c757d;
            font-style: italic;
            display: none;
        }
        .results-info {
            margin-bottom: 15px;
            font-size: 0.9rem;
            color: #6c757d;
        }
        .scroll-top-btn {
            position: fixed;
            bottom: 20px;
            right: 20px;
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background-color: #0d6efd;
            color: white;
            display: none;
            justify-content: center;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.2);
            cursor: pointer;
            z-index: 1000;
        }
        /* New filter UI styles */
        .filter-sidebar {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
            margin-bottom: 20px;
        }
        .filter-heading {
            font-weight: 600;
            margin-top: 5px;
            margin-bottom: 8px;
            color: #495057;
            font-size: 0.9rem;
        }
        .filter-section {
            border-bottom: 1px solid #e3e3e3;
            padding-bottom: 15px;
            margin-bottom: 15px;
        }
        .filter-toggle {
            cursor: pointer;
            font-weight: 500;
            color: #212529;
            padding: 5px 0;
        }
    </style>
</head>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="/home/listings">
                <img src="/images/logo.png" alt="Roommate Finder Logo" width="30" height="30" class="d-inline-block align-text-top me-2">
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
                            <li><a class="dropdown-item active" href="/home/roommate-listings">Roommate Listings</a></li>
                            <li><a class="dropdown-item" href="/home/apartment-listings">Apartment Listings</a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/messages/inbox">
                            Messages
                            <c:if test="${unreadMessageCount > 0}">
                                <span class="badge rounded-pill bg-danger">${unreadMessageCount}</span>
                            </c:if>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/users/profile">My Profile</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/users/my-listings">My Listings</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <a href="/auth/logout" class="btn btn-outline-light">Logout</a>
                </div>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h2 class="mb-4">Available Roommate Listings</h2>

        <!-- Results info -->
        <div class="results-info mb-3">
            Showing <span id="showing-count">${users.size()}</span> of <span id="total-count">${totalCount}</span> roommate listings
        </div>

        <!-- Filter Form -->
        <div class="row">
            <div class="col-12 mb-4">
                <div class="filter-sidebar">
                    <h4 class="mb-3">Filter Roommates</h4>
                    <form method="get" action="<c:url value='/home/roommate-listings'/>" id="filter-form">
                        <!-- Basic Filters Section -->
                        <div class="filter-section">
                            <h5 class="filter-toggle" data-bs-toggle="collapse" data-bs-target="#basicFilters">
                                <i class="bi bi-caret-down-fill"></i> Basic Filters
                            </h5>
                            <div id="basicFilters" class="collapse show">
                                <div class="row">
                                    <!-- University -->
                                    <div class="col-md-4 mb-3">
                                        <label for="university" class="filter-heading">University</label>
                                        <input type="text" id="university" name="university" class="form-control"
                                               placeholder="e.g. Northeastern" value="${universityFilter}"/>
                                    </div>

                                    <!-- Gender -->
                                    <div class="col-md-4 mb-3">
                                        <label for="gender" class="filter-heading">Gender</label>
                                        <select id="gender" name="gender" class="form-select">
                                            <option value="">Any</option>
                                            <option value="MALE" ${genderFilter=='MALE'? 'selected':''}>Male</option>
                                            <option value="FEMALE" ${genderFilter=='FEMALE'?'selected':''}>Female</option>
                                            <option value="OTHER" ${genderFilter=='OTHER'? 'selected':''}>Other</option>
                                        </select>
                                    </div>

                                    <!-- Age Range -->
                                    <div class="col-md-4 mb-3">
                                        <label class="filter-heading">Age Range</label>
                                        <div class="row">
                                            <div class="col-6">
                                                <input type="number" id="preferredMinAge" name="preferredMinAge" class="form-control" min="18"
                                                       placeholder="Min" value="${preferredMinAgeFilter}"/>
                                            </div>
                                            <div class="col-6">
                                                <input type="number" id="preferredMaxAge" name="preferredMaxAge" class="form-control" min="18"
                                                       placeholder="Max" value="${preferredMaxAgeFilter}"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <!-- Food Preference -->
                                    <div class="col-md-3 mb-3">
                                        <label for="foodPref" class="filter-heading">Food</label>
                                        <select id="foodPref" name="foodPref" class="form-select">
                                            <option value="">Any</option>
                                            <option value="VEGAN" ${foodPrefFilter=='VEGAN'? 'selected':''}>Vegan</option>
                                            <option value="VEGETARIAN" ${foodPrefFilter=='VEGETARIAN'? 'selected':''}>Vegetarian</option>
                                            <option value="NON_VEGETARIAN" ${foodPrefFilter=='NON_VEGETARIAN'?'selected':''}>Non‑veg</option>
                                            <option value="JAIN" ${foodPrefFilter=='JAIN'? 'selected':''}>Jain</option>
                                        </select>
                                    </div>

                                    <!-- Alcohol Preference -->
                                    <div class="col-md-3 mb-3">
                                        <label for="alcoholPref" class="filter-heading">Alcohol</label>
                                        <select id="alcoholPref" name="alcoholPref" class="form-select">
                                            <option value="">Any</option>
                                            <option value="YES" ${alcoholPrefFilter=='YES'? 'selected':''}>Yes</option>
                                            <option value="NO" ${alcoholPrefFilter=='NO'? 'selected':''}>No</option>
                                            <option value="OCCASIONALLY" ${alcoholPrefFilter=='OCCASIONALLY'?'selected':''}>Occasionally</option>
                                        </select>
                                    </div>

                                    <!-- Smoking -->
                                    <div class="col-md-3 mb-3">
                                        <label for="smoking" class="filter-heading">Smoking</label>
                                        <select id="smoking" name="smoking" class="form-select">
                                            <option value="">Any</option>
                                            <option value="true" ${smokingFilter==true? 'selected':''}>Yes</option>
                                            <option value="false" ${smokingFilter==false?'selected':''}>No</option>
                                        </select>
                                    </div>

                                    <!-- Pet Friendly -->
                                    <div class="col-md-3 mb-3">
                                        <label for="isPetFriendly" class="filter-heading">Pet Friendly</label>
                                        <select id="isPetFriendly" name="isPetFriendly" class="form-select">
                                            <option value="">Any</option>
                                            <option value="true" ${isPetFriendlyFilter==true? 'selected':''}>Yes</option>
                                            <option value="false" ${isPetFriendlyFilter==false?'selected':''}>No</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Advanced Filters Section -->
                        <div class="filter-section">
                            <h5 class="filter-toggle" data-bs-toggle="collapse" data-bs-target="#advancedFilters">
                                <i class="bi bi-caret-right-fill"></i> Advanced Filters
                            </h5>
                            <div id="advancedFilters" class="collapse">
                                <div class="row">
                                    <!-- Cleanliness Rating -->
                                    <div class="col-md-3 mb-3">
                                        <label for="cleanlinessRating" class="filter-heading">Cleanliness</label>
                                        <select id="cleanlinessRating" name="cleanlinessRating" class="form-select">
                                            <option value="">Any</option>
                                            <c:forEach begin="1" end="5" var="i">
                                                <option value="${i}" ${cleanlinessRatingFilter==i? 'selected':''}>${i}/5</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Sleep Schedule -->
                                    <div class="col-md-3 mb-3">
                                        <label for="sleepSchedule" class="filter-heading">Sleep Schedule</label>
                                        <input type="text" id="sleepSchedule" name="sleepSchedule" class="form-control"
                                               placeholder="e.g. 11 PM–7 AM" value="${sleepScheduleFilter}"/>
                                    </div>

                                    <!-- Lease Option -->
                                    <div class="col-md-3 mb-3">
                                        <label for="leaseOption" class="filter-heading">Lease</label>
                                        <select id="leaseOption" name="leaseOption" class="form-select">
                                            <option value="">Any</option>
                                            <option value="SHORT_TERM" ${leaseOptionFilter=='SHORT_TERM'? 'selected':''}>Short‑term</option>
                                            <option value="LONG_TERM" ${leaseOptionFilter=='LONG_TERM'? 'selected':''}>Long‑term</option>
                                        </select>
                                    </div>

                                    <!-- Max Roommates -->
                                    <div class="col-md-3 mb-3">
                                        <label for="maxRoommates" class="filter-heading">Max Roommates</label>
                                        <input type="number" id="maxRoommates" name="maxRoommates" class="form-control" min="1"
                                               value="${maxRoommatesFilter}"/>
                                    </div>
                                </div>

                                <div class="row">
                                    <!-- Occupation -->
                                    <div class="col-md-3 mb-3">
                                        <label for="occupation" class="filter-heading">Occupation</label>
                                        <select id="occupation" name="occupation" class="form-select">
                                            <option value="">Any</option>
                                            <option value="STUDENT" ${occupationFilter=='STUDENT'? 'selected':''}>Student</option>
                                            <option value="WORKING_PROFESSIONAL" ${occupationFilter=='WORKING_PROFESSIONAL'?'selected':''}>Working</option>
                                            <option value="OTHER" ${occupationFilter=='OTHER'? 'selected':''}>Other</option>
                                        </select>
                                    </div>

                                    <!-- Guest Frequency -->
                                    <div class="col-md-3 mb-3">
                                        <label for="guestFrequency" class="filter-heading">Guests</label>
                                        <select id="guestFrequency" name="guestFrequency" class="form-select">
                                            <option value="">Any</option>
                                            <option value="RARELY" ${guestFrequencyFilter=='RARELY'? 'selected':''}>Rarely</option>
                                            <option value="SOMETIMES" ${guestFrequencyFilter=='SOMETIMES'?'selected':''}>Sometimes</option>
                                            <option value="OFTEN" ${guestFrequencyFilter=='OFTEN'? 'selected':''}>Often</option>
                                        </select>
                                    </div>

                                    <!-- Noise Tolerance -->
                                    <div class="col-md-3 mb-3">
                                        <label for="noiseTolerance" class="filter-heading">Noise</label>
                                        <select id="noiseTolerance" name="noiseTolerance" class="form-select">
                                            <option value="">Any</option>
                                            <option value="LOW" ${noiseToleranceFilter=='LOW'? 'selected':''}>Low</option>
                                            <option value="MEDIUM" ${noiseToleranceFilter=='MEDIUM'? 'selected':''}>Medium</option>
                                            <option value="HIGH" ${noiseToleranceFilter=='HIGH'? 'selected':''}>High</option>
                                        </select>
                                    </div>

                                    <!-- Roommate Lifestyle -->
                                    <div class="col-md-3 mb-3">
                                        <label for="roommateLifestyle" class="filter-heading">Lifestyle</label>
                                        <select id="roommateLifestyle" name="roommateLifestyle" class="form-select">
                                            <option value="">Any</option>
                                            <option value="INTROVERT" ${roommateLifestyleFilter=='INTROVERT'? 'selected':''}>Introvert</option>
                                            <option value="EXTROVERT" ${roommateLifestyleFilter=='EXTROVERT'? 'selected':''}>Extrovert</option>
                                            <option value="BALANCED" ${roommateLifestyleFilter=='BALANCED'? 'selected':''}>Balanced</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="row">
                                    <!-- Room Type Preference -->
                                    <div class="col-md-3 mb-3">
                                        <label for="roomTypePreference" class="filter-heading">Room Type</label>
                                        <select id="roomTypePreference" name="roomTypePreference" class="form-select">
                                            <option value="">Any</option>
                                            <option value="PRIVATE" ${roomTypePreferenceFilter=='PRIVATE'? 'selected':''}>Private</option>
                                            <option value="SHARED" ${roomTypePreferenceFilter=='SHARED'? 'selected':''}>Shared</option>
                                            <option value="NO_PREFERENCE" ${roomTypePreferenceFilter=='NO_PREFERENCE'?'selected':''}>No Preference</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Hidden pagination fields -->
                        <input type="hidden" name="page" id="page-input" value="0">
                        <input type="hidden" name="size" id="size-input" value="${pageSize != null ? pageSize : 6}">

                        <!-- Filter Buttons -->
                        <div class="d-flex mt-4">
                            <button type="submit" class="btn btn-primary">Apply Filters</button>
                            <a href="<c:url value='/home/roommate-listings'/>" class="btn btn-outline-secondary ms-2">Reset</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Roommate listing container -->
        <div class="row" id="roommates-container">
            <c:forEach items="${users}" var="user">
                <div class="col-12 col-lg-6">
                    <div class="card position-relative ${user.email eq currentUserEmail ? 'current-user-card' : ''}">
                        <c:if test="${user.email eq currentUserEmail}">
                            <div class="current-user-badge">Your Listing</div>
                        </c:if>
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="mb-0">${user.firstName} ${user.lastName}</h5>
                            <span class="badge bg-light text-dark">
                                <c:choose>
                                    <c:when test="${not empty user.university}">
                                        ${user.university}
                                    </c:when>
                                    <c:otherwise>
                                        University not specified
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="card-body">
                            <div class="roommate-details">
                                <div class="profile-image-container">
                                    <c:choose>
                                        <c:when test="${not empty user.profileImageData}">
                                            <img src="data:${user.profileImageType};base64,${user.base64Image}" alt="${user.firstName}'s profile" class="profile-image">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="/images/default-avatar.png" alt="Default profile" class="profile-image">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="details-container">
                                    <div class="bio-text mb-3">
                                        <c:choose>
                                            <c:when test="${not empty user.bio}">
                                                ${user.bio}
                                            </c:when>
                                            <c:otherwise>
                                                <em>No bio provided</em>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="detail-item">
                                        <span class="detail-label">Email:</span> ${user.email}
                                    </div>

                                    <c:if test="${not empty user.phoneNumber}">
                                        <div class="detail-item">
                                            <span class="detail-label">Phone:</span> ${user.phoneNumber}
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty user.age}">
                                        <div class="detail-item">
                                            <span class="detail-label">Age:</span> ${user.age}
                                        </div>
                                    </c:if>

                                    <div class="detail-item">
                                        <span class="detail-label">Joined:</span>
                                        <c:choose>
                                            <c:when test="${not empty user.createdAt}">
                                                ${user.createdAt}
                                            </c:when>
                                            <c:otherwise>
                                                Not available
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <h6 class="mt-3">Preferences:</h6>
                                    <div class="mb-3">
                                        <c:set var="hasPreferences" value="false" />

                                        <c:forEach items="${userPreferences}" var="pref">
                                            <c:if test="${pref.userId eq user.id}">
                                                <c:set var="hasPreferences" value="true" />

                                                <c:if test="${not empty pref.gender}">
                                                    <span class="badge bg-secondary preferences-badge">Gender: ${pref.gender}</span>
                                                </c:if>

                                                <c:if test="${not empty pref.foodPref}">
                                                    <span class="badge bg-secondary preferences-badge">Food: ${pref.foodPref}</span>
                                                </c:if>

                                                <c:if test="${not empty pref.alcoholPref}">
                                                    <span class="badge bg-secondary preferences-badge">Alcohol: ${pref.alcoholPref}</span>
                                                </c:if>

                                                <c:if test="${pref.smoking != null}">
                                                    <span class="badge bg-secondary preferences-badge">Smoking: ${pref.smoking ? 'Yes' : 'No'}</span>
                                                </c:if>

                                                <c:if test="${pref.cleanlinessRating != null}">
                                                    <span class="badge bg-secondary preferences-badge">Cleanliness: ${pref.cleanlinessRating}/5</span>
                                                </c:if>

                                                <c:if test="${not empty pref.sleepSchedule}">
                                                    <span class="badge bg-secondary preferences-badge">Sleep: ${pref.sleepSchedule}</span>
                                                </c:if>

                                                <c:if test="${not empty pref.leaseOption}">
                                                    <span class="badge bg-secondary preferences-badge">Lease: ${pref.leaseOption}</span>
                                                </c:if>

                                                <c:if test="${pref.maxRoommates != null}">
                                                    <span class="badge bg-secondary preferences-badge">Max Roommates: ${pref.maxRoommates}</span>
                                                </c:if>
                                            </c:if>
                                        </c:forEach>

                                        <c:if test="${hasPreferences eq 'false'}">
                                            <em>No preferences specified</em>
                                        </c:if>
                                    </div>
                                </div>
                            </div>

                            <c:choose>
                                <c:when test="${user.email eq currentUserEmail}">
                                    <a href="/users/edit-profile" class="btn btn-success w-100 mt-3">Edit Your Listing</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="/messages/compose?recipient=${user.id}" class="btn btn-primary w-100 mt-3">Contact</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Loading indicator -->
        <div class="loading-indicator" id="loading-indicator">
            <div class="loading-spinner"></div>
            <p class="mt-2">Loading more roommates...</p>
        </div>

        <!-- Skeleton loading templates (hidden initially) -->
        <div class="row d-none" id="skeleton-templates">
            <div class="col-12 col-lg-6">
                <div class="card skeleton skeleton-card"></div>
            </div>
            <div class="col-12 col-lg-6">
                <div class="card skeleton skeleton-card"></div>
            </div>
        </div>

        <!-- No results message -->
        <c:if test="${empty users}">
            <div class="alert alert-info text-center">
                No active roommate listings found. Check back later!
            </div>
        </c:if>

        <!-- Scroll to top button -->
        <div class="scroll-top-btn" id="scroll-top-btn">
            <i class="bi bi-arrow-up"></i>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Lazy Loading Script -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Toggle filter sections
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

            // Constants
            const PAGE_SIZE = ${pageSize != null ? pageSize : 6};

            // DOM Elements
            const roommatesContainer = document.getElementById('roommates-container') || document.querySelector('.row');
            const loadingIndicator = document.getElementById('loading-indicator');
            const skeletonTemplates = document.getElementById('skeleton-templates');
            const showingCountEl = document.getElementById('showing-count');
            const totalCountEl = document.getElementById('total-count');
            const filterForm = document.getElementById('filter-form');
            const pageInput = document.getElementById('page-input');
            const scrollTopBtn = document.getElementById('scroll-top-btn');

            // State variables
            let currentPage = ${currentPage != null ? currentPage : 0};
            let isLoading = false;
            let hasMore = ${hasMore != null ? hasMore : (totalCount > users.size())};

            // Initialize scroll listener for lazy loading
            window.addEventListener('scroll', function() {
                // Check if we should load more
                if (isNearBottom() && hasMore && !isLoading) {
                    loadMoreRoommates();
                }

                // Show/hide scroll to top button
                if (window.scrollY > 500) {
                    scrollTopBtn.style.display = 'flex';
                } else {
                    scrollTopBtn.style.display = 'none';
                }
            });

            // Scroll to top button click handler
            if (scrollTopBtn) {
                scrollTopBtn.addEventListener('click', function() {
                    window.scrollTo({
                        top: 0,
                        behavior: 'smooth'
                    });
                });
            }

            // Check if user has scrolled near the bottom of the page
            function isNearBottom() {
                return (window.innerHeight + window.scrollY) >= (document.body.offsetHeight - 500);
            }

            // Load more roommates function
            function loadMoreRoommates() {
                if (isLoading || !hasMore) return;

                // Set loading state
                isLoading = true;
                loadingIndicator.style.display = 'block';

                // Show skeleton loaders
                showSkeletonLoaders();

                // Increment page
                currentPage++;

                // Get the current URL parameters
                const url = new URL(window.location.href);
                const queryParams = new URLSearchParams(url.search);

                // Update the page parameter
                queryParams.set('page', currentPage);

                // Make sure the size parameter is set
                if (!queryParams.has('size')) {
                    queryParams.set('size', PAGE_SIZE);
                }

                // Fetch more roommates
                fetch('/home/roommate-listings/load-more?' + queryParams.toString())
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        // Hide skeleton loaders
                        hideSkeletonLoaders();

                        // Process response
                        if (data.users && data.users.length > 0) {
                            // Append new roommates
                            appendRoommates(data.users);

                            // Update counters
                            const currentCount = parseInt(showingCountEl.textContent) + data.users.length;
                            showingCountEl.textContent = currentCount;

                            // Update state
                            hasMore = data.hasMore;
                        } else {
                            // No more results
                            hasMore = false;
                            const noMoreResults = document.createElement('div');
                            noMoreResults.className = 'col-12 text-center mt-3 mb-4';
                            noMoreResults.innerHTML = '<p class="text-muted">No more roommates to display</p>';
                            roommatesContainer.appendChild(noMoreResults);
                        }
                    })
                    .catch(error => {
                        console.error('Error loading more roommates:', error);
                        // Show error message
                        const errorMessage = document.createElement('div');
                        errorMessage.className = 'alert alert-danger mt-3';
                        errorMessage.textContent = 'Failed to load more roommates. Please try again.';
                        roommatesContainer.appendChild(errorMessage);
                    })
                    .finally(() => {
                        // Reset loading state
                        isLoading = false;
                        loadingIndicator.style.display = 'none';
                    });
            }

            // Show skeleton loaders
            function showSkeletonLoaders() {
                const template = skeletonTemplates.innerHTML;
                const tempContainer = document.createElement('div');
                tempContainer.innerHTML = template;

                // Remove d-none class from skeleton cards
                const skeletonCards = tempContainer.querySelectorAll('.col-12.col-lg-6');
                skeletonCards.forEach(card => {
                    card.classList.remove('d-none');
                    roommatesContainer.appendChild(card);
                });
            }

            // Hide skeleton loaders
            function hideSkeletonLoaders() {
                const skeletons = document.querySelectorAll('.skeleton-card');
                skeletons.forEach(skeleton => {
                    const parentCol = skeleton.closest('.col-12.col-lg-6');
                    if (parentCol) {
                        parentCol.remove();
                    }
                });
            }

            // Append roommates to container
            function appendRoommates(users) {
                users.forEach(user => {
                    const userHTML = createRoommateCard(user);
                    roommatesContainer.insertAdjacentHTML('beforeend', userHTML);
                });
            }

            // Create HTML for roommate card
            function createRoommateCard(user) {
                const isCurrentUser = user.isCurrentUser;
                const prefs = user.preferences || {};

                // Build profile image
                let profileImage = '/images/default-avatar.png';
                if (user.base64Image && user.profileImageType) {
                    profileImage = 'data:' + user.profileImageType + ';base64,' + user.base64Image;
                }

                // Build preferences display
                let preferencesHtml = '';
                if (prefs) {
                    if (prefs.gender) {
                        preferencesHtml += '<span class="badge bg-secondary preferences-badge">Gender: ' + prefs.gender + '</span>';
                    }
                    if (prefs.foodPref) {
                        preferencesHtml += '<span class="badge bg-secondary preferences-badge">Food: ' + prefs.foodPref + '</span>';
                    }
                    if (prefs.alcoholPref) {
                        preferencesHtml += '<span class="badge bg-secondary preferences-badge">Alcohol: ' + prefs.alcoholPref + '</span>';
                    }
                    if (prefs.smoking !== undefined) {
                        preferencesHtml += '<span class="badge bg-secondary preferences-badge">Smoking: ' + (prefs.smoking ? 'Yes' : 'No') + '</span>';
                    }
                    if (prefs.cleanlinessRating) {
                        preferencesHtml += '<span class="badge bg-secondary preferences-badge">Cleanliness: ' + prefs.cleanlinessRating + '/5</span>';
                    }
                    if (prefs.sleepSchedule) {
                        preferencesHtml += '<span class="badge bg-secondary preferences-badge">Sleep: ' + prefs.sleepSchedule + '</span>';
                    }
                    if (prefs.leaseOption) {
                        preferencesHtml += '<span class="badge bg-secondary preferences-badge">Lease: ' + prefs.leaseOption + '</span>';
                    }
                    if (prefs.maxRoommates) {
                        preferencesHtml += '<span class="badge bg-secondary preferences-badge">Max Roommates: ' + prefs.maxRoommates + '</span>';
                    }
                }

                if (!preferencesHtml) {
                    preferencesHtml = '<em>No preferences specified</em>';
                }

                // Handle optional fields
                const ageHtml = user.age ?
                    '<div class="detail-item">' +
                    '    <span class="detail-label">Age:</span> ' + user.age +
                    '</div>' : '';

                const phoneHtml = user.phoneNumber ?
                    '<div class="detail-item">' +
                    '    <span class="detail-label">Phone:</span> ' + user.phoneNumber +
                    '</div>' : '';

                // Build action button based on user status
                const actionButton = isCurrentUser ?
                    '<a href="/users/edit-profile" class="btn btn-success w-100 mt-3">Edit Your Listing</a>' :
                    '<a href="/messages/compose?recipient=' + user.id + '" class="btn btn-primary w-100 mt-3">Contact</a>';

                // Build the full HTML for the card
                return '<div class="col-12 col-lg-6">' +
                    '<div class="card position-relative ' + (isCurrentUser ? 'current-user-card' : '') + '">' +
                    (isCurrentUser ? '<div class="current-user-badge">Your Listing</div>' : '') +
                    '<div class="card-header d-flex justify-content-between align-items-center">' +
                    '<h5 class="mb-0">' + user.firstName + ' ' + user.lastName + '</h5>' +
                    '<span class="badge bg-light text-dark">' +
                    (user.university ? user.university : 'University not specified') +
                    '</span>' +
                    '</div>' +
                    '<div class="card-body">' +
                    '<div class="roommate-details">' +
                    '<div class="profile-image-container">' +
                    '<img src="' + profileImage + '" alt="' + user.firstName + '\'s profile" class="profile-image">' +
                    '</div>' +
                    '<div class="details-container">' +
                    '<div class="bio-text mb-3">' +
                    (user.bio ? user.bio : '<em>No bio provided</em>') +
                    '</div>' +
                    '<div class="detail-item">' +
                    '<span class="detail-label">Email:</span> ' + user.email +
                    '</div>' +
                    phoneHtml +
                    ageHtml +
                    '<div class="detail-item">' +
                    '<span class="detail-label">Joined:</span> ' +
                    (user.createdAt ? user.createdAt : 'Not available') +
                    '</div>' +
                    '<h6 class="mt-3">Preferences:</h6>' +
                    '<div class="mb-3">' +
                    preferencesHtml +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    actionButton +
                    '</div>' +
                    '</div>' +
                    '</div>';
            }
        });
    </script>
</body>
</html>