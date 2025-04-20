<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Roommate Finder - My Listings</title>
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
        .preferences-badge {
            margin-right: 5px;
            margin-bottom: 5px;
            font-size: 0.85rem;
        }
        .bio-text {
            font-size: 0.95rem;
            color: #555;
        }
        .listing-tabs {
            margin-bottom: 25px;
        }
        .listing-tabs .nav-link {
            font-size: 1.1rem;
            padding: 10px 25px;
        }
        .no-listings {
            padding: 30px;
            text-align: center;
            background: white;
            border-radius: 12px;
            box-shadow: 0 6px 18px rgba(0,0,0,0.08);
        }
        .create-listing-btn {
            margin-top: 20px;
        }
        .section-header {
            border-bottom: 2px solid #0d6efd;
            padding-bottom: 10px;
            margin-bottom: 20px;
            color: #0d6efd;
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
    <div class="container py-4">
        <!-- Preferences Section -->
        <div class="row mb-5">
            <div class="col-12">
                <h3 class="section-header">My Preferences</h3>

                <c:forEach items="${userPreferences}" var="pref">
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="row align-items-center">
                                <div class="col-md-8">
                                    <h5 class="card-title">
                                        <c:choose>
                                            <c:when test="${pref.foodPref == 'VEGAN'}">🌱 Vegan</c:when>
                                            <c:when test="${pref.foodPref == 'VEGETARIAN'}">🥦 Vegetarian</c:when>
                                            <c:when test="${pref.foodPref == 'NON_VEGETARIAN'}">🥩 Non-Vegetarian</c:when>
                                            <c:when test="${pref.foodPref == 'JAIN'}">🌿 Jain</c:when>
                                            <c:otherwise>${pref.foodPref}</c:otherwise>
                                        </c:choose>
                                        Preferences
                                    </h5>
                                    <div class="row">
                                        <div class="col-6">
                                            <p class="mb-1"><strong>Alcohol:</strong> ${pref.alcoholPref}</p>
                                            <p class="mb-1"><strong>Smoking:</strong> ${pref.smoking ? 'Allowed' : 'Not Allowed'}</p>
                                        </div>
                                        <div class="col-6">
                                            <p class="mb-1"><strong>Cleanliness:</strong> ${pref.cleanlinessRating}/5</p>
                                            <p class="mb-1"><strong>Areas:</strong>
                                                <c:forEach items="${pref.preferredAreas}" var="area" varStatus="status">
                                                    ${area}<c:if test="${!status.last}">, </c:if>
                                                </c:forEach>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="mt-2">
                                        <p class="mb-1"><strong>Languages:</strong>
                                            <c:forEach items="${pref.languagesKnown}" var="lang" varStatus="status">
                                                ${lang}<c:if test="${!status.last}">, </c:if>
                                            </c:forEach>
                                        </p>
                                        <c:if test="${not empty pref.sleepSchedule}">
                                            <p class="mb-1"><strong>Sleep Schedule:</strong> ${pref.sleepSchedule}</p>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="col-md-4 text-end">
                                    <a href="/users/edit-preference/${pref.id}"
                                       class="btn btn-sm btn-outline-primary me-2">Edit</a>
                                    <form action="/users/delete-preference/${pref.id}"
                                          method="POST"
                                          class="d-inline">
                                        <button type="submit"
                                                class="btn btn-sm btn-outline-danger"
                                                onclick="return confirm('Delete these preferences?')">
                                            Delete
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty userPreferences}">
                    <div class="alert alert-info">
                        <div class="d-flex justify-content-between align-items-center">
                            <span>No preferences found. Create your first preference set!</span>
                            <a href="/users/create-preference" class="btn btn-sm btn-success">
                                Add Preferences
                            </a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Room Listings Section -->
        <div class="row mb-5">
            <div class="col-12">
                <h3 class="section-header">My Room Listings</h3>

                <c:forEach items="${roomListings}" var="room">
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="row align-items-center">
                                <div class="col-md-8">
                                    <h5 class="card-title">
                                        Room listing with ${room.numBeds} beds - $${room.rent}/month
                                    </h5>
                                    <div class="row">
                                        <div class="col-6">
                                            <p class="mb-1"><strong>Rent:</strong> $${room.rent}/month
                                            <c:if test="${room.rentNegotiable}"> (Negotiable)</c:if>
                                            </p>
                                            <p class="mb-1"><strong>Utilities:</strong> $${room.utilitiesCost}/month</p>
                                            <p class="mb-1"><strong>Available from:</strong>
                                                <c:if test="${not empty room.availableFrom}">
                                                    ${room.availableFrom}
                                                </c:if>
                                            </p>
                                        </div>
                                        <div class="col-6">
                                            <p class="mb-1"><strong>Beds:</strong> ${room.numBeds}</p>
                                            <p class="mb-1"><strong>Current occupancy:</strong> ${room.currentOccupancy}/${room.totalCapacity}</p>
                                            <p class="mb-1"><strong>Sublet:</strong> ${room.subletAvailable ? 'Available' : 'Not available'}</p>
                                        </div>
                                    </div>
                                    <c:if test="${not empty room.transportOptions}">
                                        <p class="mt-2 bio-text"><strong>Transport options:</strong> ${room.transportOptions}</p>
                                    </c:if>
                                </div>
                                <div class="col-md-4 text-end">
                                    <a href="/rooms/edit/${room.id}" class="btn btn-sm btn-outline-primary me-2">Edit</a>
                                    <form action="/rooms/delete/${room.id}" method="POST" class="d-inline">
                                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                                onclick="return confirm('Are you sure you want to delete this room listing?')">
                                            Delete
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty roomListings}">
                    <div class="alert alert-info">
                        <div class="d-flex justify-content-between align-items-center">
                            <span>You haven't listed any rooms yet. Add your first room!</span>
                            <a href="/rooms/create" class="btn btn-sm btn-success">
                                Add Room
                            </a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Roommate Listings Section -->
        <div class="row mb-5">
            <div class="col-12">
                <h3 class="section-header">My Profile</h3>

                <c:forEach items="${roommateListings}" var="user">
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="row align-items-center">
                                <div class="col-md-8">
                                    <h5 class="card-title">${user.firstName} ${user.lastName}</h5>
                                    <div class="row">
                                        <div class="col-6">
                                            <p class="mb-1"><strong>Email:</strong> ${user.email}</p>
                                            <!-- Phone and gender references removed -->
                                        </div>
                                        <div class="col-6">
                                            <p class="mb-1"><strong>Active:</strong> ${user.isActive ? 'Yes' : 'No'}</p>
                                        </div>
                                    </div>
                                    <c:if test="${not empty user.bio}">
                                        <p class="mt-2 bio-text">${user.bio}</p>
                                    </c:if>
                                </div>
                                <div class="col-md-4 text-end">
                                    <a href="/users/profile" class="btn btn-sm btn-outline-primary">Edit Profile</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty roommateListings}">
                    <div class="alert alert-info">
                        <div class="d-flex justify-content-between align-items-center">
                            <span>Your profile is not active as a roommate listing.</span>
                            <a href="/users/profile" class="btn btn-sm btn-success">
                                Activate Profile
                            </a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>