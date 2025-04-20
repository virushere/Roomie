<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Messages - Roommate Finder</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .message-list {
            border-right: 1px solid #dee2e6;
        }
        .message-item {
            border-bottom: 1px solid #dee2e6;
            padding: 15px;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .message-item:hover {
            background-color: #f8f9fa;
        }
        .message-item.unread {
            background-color: #e9f5ff;
        }
        .message-item.active {
            background-color: #e9ecef;
        }
        .message-sender {
            font-weight: 600;
            margin-bottom: 2px;
        }
        .message-preview {
            color: #6c757d;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .message-time {
            font-size: 0.8rem;
            color: #6c757d;
        }
        .message-content {
            min-height: 300px;
        }
        .message-actions {
            padding: 10px;
            border-bottom: 1px solid #dee2e6;
            background-color: #f8f9fa;
        }
        .profile-image {
            width: 40px;
            height: 40px;
            object-fit: cover;
        }
        .unread-badge {
            font-size: 0.7rem;
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
        <div class="row mb-4">
            <div class="col-md-6">
                <h2>Messages</h2>
            </div>
            <div class="col-md-6 text-end">
                <a href="/messages/compose" class="btn btn-primary">New Message</a>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="card shadow">
                    <div class="card-header bg-white">
                        <ul class="nav nav-tabs card-header-tabs">
                            <li class="nav-item">
                                <a class="nav-link active" href="/messages/inbox">Inbox</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/messages/sent">Sent</a>
                            </li>
                        </ul>
                    </div>
                    <div class="card-body p-0">
                        <c:if test="${not empty success}">
                            <div class="alert alert-success m-3">${success}</div>
                        </c:if>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger m-3">${error}</div>
                        </c:if>

                        <c:choose>
                            <c:when test="${not empty messages}">
                                <div class="table-responsive">
                                    <table class="table table-hover mb-0">
                                        <thead>
                                            <tr>
                                                <th>Sender</th>
                                                <th>Subject</th>
                                                <th>Date</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="message" items="${messages}">
                                                <tr class="${!message.read ? 'table-primary' : ''}">
                                                    <td class="align-middle">
                                                        <div class="d-flex align-items-center">
                                                            <c:choose>
                                                                <c:when test="${message.sender.hasProfileImage()}">
                                                                    <img src="data:${message.sender.profileImageType};base64,${message.sender.base64Image}"
                                                                        class="rounded-circle me-2 profile-image" alt="Profile">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <div class="bg-secondary rounded-circle me-2 d-flex justify-content-center align-items-center text-white profile-image">
                                                                        <span>${message.sender.firstName.charAt(0)}</span>
                                                                    </div>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <div>
                                                                ${message.sender.firstName} ${message.sender.lastName}
                                                                <c:if test="${!message.read}">
                                                                    <span class="badge bg-primary ms-2 unread-badge">New</span>
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td class="align-middle text-truncate" style="max-width: 300px;">
                                                        <c:if test="${message.roomId != null}">
                                                            <span class="badge bg-secondary me-1">Room #${message.roomId}</span>
                                                        </c:if>
                                                        ${message.content}
                                                    </td>
                                                    <td class="align-middle">
                                                        ${message.sentAt}
                                                    </td>
                                                    <td class="align-middle">
                                                        <a href="/messages/view/${message.id}" class="btn btn-sm btn-primary">View</a>
                                                        <a href="/messages/compose?recipient=${message.sender.id}&reply=${message.id}" class="btn btn-sm btn-outline-primary">Reply</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center p-5">
                                    <i class="bi bi-envelope-fill fs-1 text-muted mb-3"></i>
                                    <h5>No messages in your inbox</h5>
                                    <p class="text-muted">When you receive messages, they will appear here.</p>
                                    <a href="/messages/compose" class="btn btn-primary mt-3">Compose New Message</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>