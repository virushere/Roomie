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

        <div class="row">
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

            <c:if test="${empty users}">
                <div class="col-12">
                    <div class="alert alert-info text-center">
                        No active roommate listings found. Check back later!
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>