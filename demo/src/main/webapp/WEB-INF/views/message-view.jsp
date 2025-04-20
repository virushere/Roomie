<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Message - Roommate Finder</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .message-header {
            border-bottom: 1px solid #dee2e6;
            padding-bottom: 15px;
        }
        .message-content {
            padding: 20px 0;
            min-height: 200px;
        }
        .profile-image {
            width: 50px;
            height: 50px;
            object-fit: cover;
        }
        .message-meta {
            color: #6c757d;
            font-size: 0.9rem;
        }
        .message-footer {
            border-top: 1px solid #dee2e6;
            padding-top: 15px;
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
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card shadow">
                    <div class="card-header bg-white">
                        <div class="d-flex justify-content-between align-items-center">
                            <h4 class="mb-0">Message</h4>
                            <div>
                                <a href="javascript:history.back()" class="btn btn-outline-secondary btn-sm">
                                    <i class="bi bi-arrow-left"></i> Back
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="message-header">
                            <div class="row">
                                <div class="col-md-8">
                                    <div class="d-flex">
                                        <c:choose>
                                            <c:when test="${isInbox}">
                                                <c:choose>
                                                    <c:when test="${message.sender.hasProfileImage()}">
                                                        <img src="data:${message.sender.profileImageType};base64,${message.sender.base64Image}"
                                                            class="rounded-circle me-3 profile-image" alt="Profile">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="bg-secondary rounded-circle me-3 d-flex justify-content-center align-items-center text-white profile-image">
                                                            <span>${message.sender.firstName.charAt(0)}</span>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                                <div>
                                                    <h5 class="mb-0">From: ${message.sender.firstName} ${message.sender.lastName}</h5>
                                                    <p class="text-muted mb-0">${message.sender.email}</p>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${message.recipient.hasProfileImage()}">
                                                        <img src="data:${message.recipient.profileImageType};base64,${message.recipient.base64Image}"
                                                            class="rounded-circle me-3 profile-image" alt="Profile">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="bg-secondary rounded-circle me-3 d-flex justify-content-center align-items-center text-white profile-image">
                                                            <span>${message.recipient.firstName.charAt(0)}</span>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                                <div>
                                                    <h5 class="mb-0">To: ${message.recipient.firstName} ${message.recipient.lastName}</h5>
                                                    <p class="text-muted mb-0">${message.recipient.email}</p>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="col-md-4 text-md-end">
                                    <div class="message-meta">
                                        <p class="mb-0">Date: ${message.sentAt}</p>
                                        <c:if test="${message.roomId != null}">
                                            <p class="mb-0">Related to: <a href="/apartments/details/${message.roomId}">Room #${message.roomId}</a></p>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="message-content">
                            <p class="text-break">${message.content}</p>
                        </div>

                        <div class="message-footer">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <c:choose>
                                        <c:when test="${isInbox}">
                                            <a href="/messages/compose?recipient=${message.sender.id}&reply=${message.id}" class="btn btn-primary">Reply</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/messages/compose?recipient=${message.recipient.id}" class="btn btn-primary">Message Again</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div>
                                    <c:if test="${isInbox && !message.read}">
                                        <span class="badge bg-primary me-2">New</span>
                                    </c:if>
                                    <c:if test="${!isInbox && message.read}">
                                        <span class="badge bg-success me-2">Read</span>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>