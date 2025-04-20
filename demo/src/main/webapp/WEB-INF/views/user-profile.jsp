<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <!-- Navigation -->
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

    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">My Profile</h4>
                    </div>
                    <div class="card-body">
                        <!-- Alerts for success/error messages -->
                        <c:if test="${param.success == 'true'}">
                            <div class="alert alert-success">Your profile has been updated successfully!</div>
                        </c:if>
                        <c:if test="${param.error == 'true'}">
                            <div class="alert alert-danger">There was an error uploading your profile picture. Please try again.</div>
                        </c:if>

                        <!-- Profile Picture -->
                        <div class="text-center mb-4">
                            <c:choose>
                                <c:when test="${user.hasProfileImage()}">
                                    <img src="/users/profile-image/${user.id}" alt="Profile Picture" class="rounded-circle" style="width: 150px; height: 150px; object-fit: cover;">
                                </c:when>
                                <c:otherwise>
                                    <div class="bg-secondary rounded-circle d-inline-flex justify-content-center align-items-center text-white"
                                         style="width: 150px; height: 150px; font-size: 3rem;">
                                        ${fn:toUpperCase(fn:substring(user.firstName, 0, 1))}
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Profile Form -->
                        <form action="/users/profile" method="post" enctype="multipart/form-data">
                            <div class="mb-3">
                                <label for="profileImageFile" class="form-label">Profile Picture</label>
                                <input type="file" name="profileImageFile" id="profileImageFile"
                                       class="form-control" accept="image/*">
                                <div class="form-text">
                                    <c:choose>
                                        <c:when test="${not empty user.profilePicture}">
                                            Current file: ${user.profilePicture}
                                        </c:when>
                                        <c:otherwise>
                                            Upload a profile picture (Max size: 5MB)
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="firstName" class="form-label">First Name</label>
                                    <input type="text" name="firstName" id="firstName"
                                           class="form-control" required value="${user.firstName}">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="lastName" class="form-label">Last Name</label>
                                    <input type="text" name="lastName" id="lastName"
                                           class="form-control" required value="${user.lastName}">
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" name="email" id="email" class="form-control"
                                       value="${user.email}" readonly>
                                <div class="form-text">Email cannot be changed</div>
                            </div>

                            <div class="mb-3">
                                <label for="phoneNumber" class="form-label">Phone Number</label>
                                <input type="tel" name="phoneNumber" id="phoneNumber"
                                       class="form-control" value="${user.phoneNumber}">
                            </div>

                            <div class="mb-3">
                                <label for="university" class="form-label">University</label>
                                <input type="text" name="university" id="university"
                                       class="form-control" value="${user.university}">
                            </div>

                            <div class="mb-3">
                                <label for="bio" class="form-label">Bio</label>
                                <textarea name="bio" id="bio" class="form-control"
                                          rows="4">${user.bio}</textarea>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Actively Looking:</label>
                                <div>
                                    <div style="margin-bottom: 10px;">
                                        <input type="radio" id="activeYes" name="isActive" value="true" style="margin-right: 8px;" ${user.isActive ? 'checked' : ''}>
                                        <label for="activeYes" style="color: #198754; font-weight: 500;">Actively Looking</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="activeNo" name="isActive" value="false" style="margin-right: 8px;" ${!user.isActive ? 'checked' : ''}>
                                        <label for="activeNo" style="color: #dc3545; font-weight: 500;">Not Actively Looking</label>
                                    </div>
                                </div>
                            </div>

                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-primary">Save Changes</button>
                                <a href="/users/my-listings" class="btn btn-outline-secondary">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    document.addEventListener('DOMContentLoaded', function() {
        const fileInput = document.getElementById('profileImageFile');

        const inactiveRadio = document.getElementById('activeNo');
        inactiveRadio.addEventListener('change', function() {
            if (this.checked) {
                alert('Warning: Your profile will be marked as inactive. You won\'t appear in roommate searches while inactive and you not be able to login again.');
            }
        });

        fileInput.addEventListener('change', function() {
            if (this.files && this.files[0]) {
                const reader = new FileReader();

                reader.onload = function(e) {
                    const existingImage = document.querySelector('.rounded-circle');
                    if (existingImage && existingImage.tagName === 'IMG') {
                        existingImage.src = e.target.result;
                    } else {
                        const placeholder = document.querySelector('.bg-secondary.rounded-circle');
                        if (placeholder) {
                            placeholder.parentNode.innerHTML = `<img src="${e.target.result}"
                                             alt="Profile Picture Preview"
                                             class="rounded-circle"
                                             style="width: 150px; height: 150px; object-fit: cover;">`;
                        }
                    }
                };

                reader.readAsDataURL(this.files[0]);
            }
        });
    });
    </script>
</body>
</html>