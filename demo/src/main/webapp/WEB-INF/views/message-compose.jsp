<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Compose Message - Roommate Finder</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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
                    <li class="nav-item">
                        <a class="nav-link" href="/home/listings">Browse Listings</a>
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
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">Compose Message</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>

                        <form action="/messages/send" method="post">
                            <!-- Recipient Information -->
                            <c:if test="${not empty recipient}">
                                <div class="mb-4">
                                    <div class="d-flex align-items-center">
                                        <c:choose>
                                            <c:when test="${recipient.hasProfileImage()}">
                                                <img src="data:${recipient.profileImageType};base64,${recipient.base64Image}"
                                                     class="rounded-circle me-3"
                                                     alt="${recipient.firstName}'s profile"
                                                     style="width: 50px; height: 50px; object-fit: cover;">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="bg-secondary rounded-circle me-3 d-flex justify-content-center align-items-center text-white"
                                                     style="width: 50px; height: 50px; font-size: 1.2rem;">
                                                    ${recipient.firstName.substring(0,1).toUpperCase()}
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                        <div>
                                            <h5 class="mb-0">To: ${recipient.firstName} ${recipient.lastName}</h5>
                                            <p class="text-muted mb-0">${recipient.email}</p>
                                        </div>
                                    </div>
                                    <input type="hidden" name="recipientId" value="${recipient.id}">
                                </div>
                            </c:if>

                            <c:if test="${empty recipient}">
                                <div class="mb-3">
                                    <label for="recipientId" class="form-label">Recipient</label>
                                    <select class="form-select" id="recipientId" name="recipientId" required>
                                        <option value="">Select a recipient</option>
                                        <c:forEach items="${users}" var="user">
                                            <option value="${user.id}">${user.firstName} ${user.lastName} (${user.email})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </c:if>

                            <!-- Room Reference (if applicable) -->
                            <c:if test="${not empty param.roomId}">
                                <input type="hidden" name="roomId" value="${param.roomId}">
                            </c:if>

                            <!-- Message Content -->
                            <div class="mb-3">
                                <label for="message" class="form-label">Message</label>
                                <textarea class="form-control" id="message" name="message" rows="6" required
                                          placeholder="Type your message here...">${param.message}</textarea>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">Send Message</button>
                                <a href="javascript:history.back()" class="btn btn-outline-secondary">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>