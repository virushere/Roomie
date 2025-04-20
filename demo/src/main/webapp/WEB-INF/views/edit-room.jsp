<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Room Listing</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .image-preview {
            max-width: 100%;
            max-height: 200px;
            margin-top: 10px;
            border-radius: 8px;
        }
        .form-section {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .form-section-title {
            margin-bottom: 15px;
            color: #0d6efd;
        }
    </style>
</head>
<body class="bg-light">
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

    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-lg-9">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">Edit Room Listing</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mb-4">${error}</div>
                        </c:if>
                        <form action="/rooms/edit/${room.id}" method="POST" enctype="multipart/form-data">
                            <!-- Basic Information Section -->
                            <div class="form-section">
                                <h5 class="form-section-title">Basic Information</h5>
                                <div class="mb-3">
                                    <label for="description" class="form-label">Description</label>
                                    <textarea name="description" id="description" class="form-control" rows="3"
                                              placeholder="Describe your apartment or room...">${room.description}</textarea>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="rent" class="form-label">Monthly Rent ($)</label>
                                        <input name="rent" type="number" class="form-control" id="rent" required min="0" value="${room.rent}">
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="utilitiesCost" class="form-label">Utilities Cost ($)</label>
                                        <input name="utilitiesCost" type="number" class="form-control" id="utilitiesCost" min="0" value="${room.utilitiesCost}">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <div class="form-check">
                                            <!-- Removed hidden field -->
                                            <input name="rentNegotiable" type="checkbox" class="form-check-input" id="rentNegotiable" value="true"
                                                <c:if test="${room.rentNegotiable}">checked="checked"</c:if> />
                                            <label class="form-check-label" for="rentNegotiable">Rent Negotiable</label>
                                        </div>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <div class="form-check">
                                            <!-- Removed hidden field -->
                                            <input name="subletAvailable" type="checkbox" class="form-check-input" id="subletAvailable" value="true"
                                                <c:if test="${room.subletAvailable}">checked="checked"</c:if> />
                                            <label class="form-check-label" for="subletAvailable">Sublet Available</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="squareFeet" class="form-label">Square Feet</label>
                                    <input name="squareFeet" type="number" class="form-control" id="squareFeet" min="0" value="${room.squareFeet}">
                                </div>
                                <div class="mb-3">
                                    <label for="availableUntil" class="form-label">Available Until (Optional)</label>
                                    <input type="date" id="availableUntil" name="availableUntil"
                                           value="${room.availableUntil}" class="form-control">
                                </div>
                            </div>

                            <!-- Location Section -->
                            <div class="form-section">
                                <h5 class="form-section-title">Location</h5>
                                <div class="mb-3">
                                    <label for="address" class="form-label">Address</label>
                                    <input name="address" type="text" class="form-control" id="address" value="${room.address}">
                                </div>
                                <div class="row">
                                    <div class="col-md-4 mb-3">
                                        <label for="city" class="form-label">City</label>
                                        <input name="city" type="text" class="form-control" id="city" value="${room.city}">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label for="state" class="form-label">State</label>
                                        <input name="state" type="text" class="form-control" id="state" value="${room.state}">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label for="zipCode" class="form-label">ZIP Code</label>
                                        <input name="zipCode" type="text" class="form-control" id="zipCode" value="${room.zipCode}">
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="transportOptions" class="form-label">Transportation Options</label>
                                    <input name="transportOptions" type="text" class="form-control" id="transportOptions"
                                           placeholder="Public transit, parking, etc." value="${room.transportOptions}">
                                </div>
                                <div class="mb-3">
                                    <label for="nearbyUniversities" class="form-label">Nearby Universities</label>
                                    <input name="nearbyUniversities" type="text" class="form-control" id="nearbyUniversities"
                                           placeholder="List nearby universities/colleges" value="${room.nearbyUniversities}">
                                </div>
                                <div class="mb-3">
                                    <label for="distanceToUniversity" class="form-label">Distance to University (in minutes)</label>
                                    <input name="distanceToUniversity" type="number" class="form-control" id="distanceToUniversity" min="0" value="${room.distanceToUniversity}">
                                </div>
                            </div>

                            <!-- Room Details Section -->
                            <div class="form-section">
                                <h5 class="form-section-title">Room Details</h5>
                                <div class="row">
                                    <div class="col-md-4 mb-3">
                                        <label for="numBeds" class="form-label">Number of Beds</label>
                                        <input name="numBeds" type="number" class="form-control" id="numBeds" required min="1" value="${room.numBeds}">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label for="currentOccupancy" class="form-label">Current Occupancy</label>
                                        <input name="currentOccupancy" type="number" class="form-control" id="currentOccupancy" required min="0" value="${room.currentOccupancy}">
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label for="totalCapacity" class="form-label">Total Capacity</label>
                                        <input name="totalCapacity" type="number" class="form-control" id="totalCapacity" required min="1" value="${room.totalCapacity}">
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="numBaths" class="form-label">Number of Bathrooms</label>
                                    <input name="numBaths" type="number" class="form-control" id="numBaths" step="0.5" min="0" value="${room.numBaths}">
                                    <div class="form-text">Use 0.5 for half-bathrooms</div>
                                </div>
                                <div class="mb-3">
                                    <label for="availableFrom" class="form-label">Available From</label>
                                    <input type="date" id="availableFrom" name="availableFrom" value="${room.availableFrom}" class="form-control" required>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-md-4 mb-3">
                                        <div class="form-check">
                                            <input name="isFurnished" type="checkbox" class="form-check-input" id="isFurnished" value="true"
                                                   <c:if test="${room.isFurnished}">checked="checked"</c:if> />
                                            <label class="form-check-label" for="isFurnished">Furnished</label>
                                        </div>
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <div class="form-check">
                                            <input name="hasParking" type="checkbox" class="form-check-input" id="hasParking" value="true"
                                                   <c:if test="${room.hasParking}">checked="checked"</c:if> />
                                            <label class="form-check-label" for="hasParking">Parking Available</label>
                                        </div>
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <div class="form-check">
                                            <input name="hasLaundry" type="checkbox" class="form-check-input" id="hasLaundry" value="true"
                                                   <c:if test="${room.hasLaundry}">checked="checked"</c:if> />
                                            <label class="form-check-label" for="hasLaundry">Laundry On-site</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <div class="form-check">
                                        <input name="petsAllowed" type="checkbox" class="form-check-input" id="petsAllowed" value="true"
                                               <c:if test="${room.petsAllowed}">checked="checked"</c:if> />
                                        <label class="form-check-label" for="petsAllowed">Pets Allowed</label>
                                    </div>
                                </div>
                            </div>

                            <!-- Media Section -->
                            <div class="form-section">
                                <h5 class="form-section-title">Media</h5>
                                <div class="mb-3">
                                    <label for="roomImageFile" class="form-label">Room Photo</label>
                                    <input type="file" name="roomImageFile" id="roomImageFile" class="form-control" accept="image/*">
                                    <c:if test="${room.hasImage()}">
                                        <div class="mt-2">
                                            <p>Current image: ${room.imageFilename}</p>
                                            <img src="data:${room.imageType};base64,${room.base64Image}" alt="Room Photo" class="image-preview">
                                        </div>
                                    </c:if>
                                    <div id="newImagePreviewContainer" class="mt-2" style="display: none;">
                                        <p>New image preview:</p>
                                        <img id="newImagePreview" class="image-preview" alt="New Image Preview">
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="videoUrl" class="form-label">Video URL (YouTube, Vimeo, etc.)</label>
                                    <input name="videoUrl" type="url" class="form-control" id="videoUrl"
                                           placeholder="https://www.youtube.com/watch?v=..." value="${room.videoUrl}">
                                    <div class="form-text">Enter a link to a video tour of your place</div>
                                </div>
                                <c:if test="${not empty room.videoUrl}">
                                    <!-- Compute the embed URL -->
                                    <c:set var="embedUrl" value="${room.videoUrl}" />
                                    <c:if test="${room.videoUrl.contains('youtube.com/watch?v=')}">
                                        <c:set var="embedUrl" value="${room.videoUrl.replace('youtube.com/watch?v=', 'youtube.com/embed/')}" />
                                    </c:if>
                                    <div class="mb-3">
                                        <p>Current video:</p>
                                        <div class="ratio ratio-16x9">
                                            <iframe src="${embedUrl}" title="Room Video" frameborder="0"
                                                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                                    allowfullscreen></iframe>
                                        </div>
                                    </div>
                                </c:if>
                            </div>

                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-primary btn-lg">Save Changes</button>
                                <a href="/users/my-listings" class="btn btn-outline-secondary">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript for image preview and client-side validation -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.querySelector('form');
            const currentOccupancyInput = document.getElementById('currentOccupancy');
            const totalCapacityInput = document.getElementById('totalCapacity');
            const fileInput = document.getElementById('roomImageFile');
            const newImagePreview = document.getElementById('newImagePreview');
            const newImagePreviewContainer = document.getElementById('newImagePreviewContainer');

            // Image preview for new uploads
            fileInput.addEventListener('change', function() {
                if (this.files && this.files[0]) {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        newImagePreview.src = e.target.result;
                        newImagePreviewContainer.style.display = 'block';
                    };
                    reader.readAsDataURL(this.files[0]);
                } else {
                    newImagePreviewContainer.style.display = 'none';
                }
            });

            // Validate on form submission
            form.addEventListener('submit', function(event) {
                const currentOccupancy = parseInt(currentOccupancyInput.value);
                const totalCapacity = parseInt(totalCapacityInput.value);
                if (totalCapacity < currentOccupancy) {
                    event.preventDefault();
                    alert('Total capacity must be greater than or equal to current occupancy.');
                    totalCapacityInput.focus();
                }
            });

            // Real-time validation when either field changes
            function validateCapacity() {
                const currentOccupancy = parseInt(currentOccupancyInput.value);
                const totalCapacity = parseInt(totalCapacityInput.value);
                if (!isNaN(currentOccupancy) && !isNaN(totalCapacity) && totalCapacity < currentOccupancy) {
                    totalCapacityInput.setCustomValidity('Total capacity must be greater than or equal to current occupancy.');
                } else {
                    totalCapacityInput.setCustomValidity('');
                }
            }
            currentOccupancyInput.addEventListener('input', validateCapacity);
            totalCapacityInput.addEventListener('input', validateCapacity);
        });
    </script>
</body>
</html>