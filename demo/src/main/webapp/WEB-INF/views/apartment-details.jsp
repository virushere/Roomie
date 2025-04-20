<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apartment Details - Roommate Finder</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .property-image {
            width: 100%;
            max-height: 400px;
            object-fit: cover;
            border-radius: 8px;
        }

        .detail-section {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .detail-label {
            font-weight: 600;
            color: #0d6efd;
        }

        .amenity-badge {
            margin-right: 8px;
            margin-bottom: 8px;
            font-size: 0.9rem;
        }

        .contact-box {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 20px;
            background-color: #fff;
        }

        .map-container {
            height: 300px;
            border-radius: 8px;
            overflow: hidden;
            margin-bottom: 20px;
        }

        .address-box {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .video-container {
            position: relative;
            padding-bottom: 56.25%; /* 16:9 Aspect Ratio */
            height: 0;
            overflow: hidden;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .video-container iframe {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
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
                            <li><a class="dropdown-item" href="/home/roommate-listings">Roommate Listings</a></li>
                            <li><a class="dropdown-item" href="/home/apartment-listings">Apartment Listings</a></li>
                        </ul>
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

    <div class="container py-5">
        <div class="row mb-4">
            <div class="col-md-8">
                <a href="/home/apartment-listings" class="btn btn-sm btn-outline-secondary mb-3">
                    <i class="bi bi-arrow-left"></i> Back to Listings
                </a>
                <h2 class="mb-0">
                    <c:choose>
                        <c:when test="${not empty room.address}">
                            ${room.address}
                        </c:when>
                        <c:otherwise>
                            Room #${room.id}
                        </c:otherwise>
                    </c:choose>
                </h2>
                <p class="text-muted">
                    <c:if test="${not empty room.city}">
                        ${room.city}<c:if test="${not empty room.state}">, ${room.state}</c:if>
                        <c:if test="${not empty room.zipCode}"> ${room.zipCode}</c:if>
                    </c:if>
                </p>
            </div>
            <div class="col-md-4 text-md-end">
                <span class="badge bg-success fs-5 p-2">$${room.rent}/month</span>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-8">
                <!-- Property Image -->
                <c:choose>
                    <c:when test="${room.hasImage()}">
                        <img src="data:${room.imageType};base64,${room.base64Image}" alt="Room Photo" class="property-image mb-4">
                    </c:when>
                    <c:otherwise>
                        <div class="bg-secondary text-white d-flex align-items-center justify-content-center property-image mb-4">
                            <p class="m-0 fs-4">No Image Available</p>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Property Description -->
                <div class="detail-section">
                    <h4 class="mb-3">Description</h4>
                    <p>
                        <c:choose>
                            <c:when test="${not empty room.description}">
                                ${room.description}
                            </c:when>
                            <c:otherwise>
                                <em>No description provided</em>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <!-- Property Details -->
                <div class="detail-section">
                    <h4 class="mb-3">Property Details</h4>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <p><span class="detail-label">Available From:</span> ${room.availableFrom}</p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <p><span class="detail-label">Occupancy:</span> ${room.currentOccupancy}/${room.totalCapacity} occupied</p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <p><span class="detail-label">Number of Beds:</span> ${room.numBeds}</p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <p><span class="detail-label">Rent:</span> $${room.rent}/month</p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <p><span class="detail-label">Utilities:</span> $${room.utilitiesCost}/month</p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <p><span class="detail-label">Rent Negotiable:</span> ${room.rentNegotiable ? 'Yes' : 'No'}</p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <p><span class="detail-label">Sublet Available:</span> ${room.subletAvailable ? 'Yes' : 'No'}</p>
                        </div>
                    </div>
                </div>

                <!-- Transportation Options -->
                <div class="detail-section">
                    <h4 class="mb-3">Transportation</h4>
                    <p>
                        <c:choose>
                            <c:when test="${not empty room.transportOptions}">
                                ${room.transportOptions}
                            </c:when>
                            <c:otherwise>
                                <em>No transportation information provided</em>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <!-- Video Tour -->
                <c:if test="${not empty room.videoUrl}">
                    <div class="detail-section">
                        <h4 class="mb-3">Video Tour</h4>
                        <div class="video-container">
                            <iframe src="${room.videoUrl.contains('youtube.com/watch?v=') ?
                                     room.videoUrl.replace('youtube.com/watch?v=', 'youtube.com/embed/') :
                                     room.videoUrl}"
                                    frameborder="0"
                                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                    allowfullscreen></iframe>
                        </div>
                    </div>
                </c:if>
            </div>

            <div class="col-lg-4">
                <!-- Contact Owner -->
                <div class="contact-box mb-4 shadow-sm">
                    <h4 class="mb-3">Contact Owner</h4>
                    <form action="/messages/send" method="post">
                        <input type="hidden" name="roomId" value="${room.id}">
                        <input type="hidden" name="recipientId" value="${owner.id}">

                        <div class="mb-3">
                            <label for="message" class="form-label">Message</label>
                            <textarea class="form-control" id="message" name="message" rows="4" required
                                      placeholder="I'm interested in this room..."></textarea>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">Send Message</button>
                    </form>

                    <hr>

                    <p class="mb-0"><strong>Owner:</strong> ${owner.firstName} ${owner.lastName}</p>
                    <p class="mb-0"><strong>Email:</strong> ${owner.email}</p>
                    <c:if test="${not empty owner.phoneNumber}">
                        <p class="mb-0"><strong>Phone:</strong> ${owner.phoneNumber}</p>
                    </c:if>
                </div>

                <!-- Location Information -->
                <c:if test="${not empty room.address}">
                    <div class="address-box shadow-sm">
                        <h5 class="mb-2">Location</h5>
                        <p class="mb-0">
                            ${room.address}<br>
                            <c:if test="${not empty room.city}">
                                ${room.city}<c:if test="${not empty room.state}">, ${room.state}</c:if>
                                <c:if test="${not empty room.zipCode}"> ${room.zipCode}</c:if>
                            </c:if>
                        </p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>