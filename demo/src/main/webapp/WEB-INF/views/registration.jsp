<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Registration</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
        }
        .card {
            border: none;
            border-radius: 12px;
            box-shadow: 0 6px 18px rgba(0,0,0,0.08);
            overflow: hidden;
        }
        .card-header {
            background-color: #0d6efd;
            padding: 1.5rem 0;
            border-bottom: none;
        }
        .card-body {
            padding: 2rem;
        }
        .form-label {
            font-weight: 500;
            font-size: 0.9rem;
            margin-bottom: 0.5rem;
        }
        .form-label::after {
            content: " *";
            color: #dc3545;
        }
        .form-control {
            padding: 0.75rem 1rem;
            border-radius: 8px;
            border: 1px solid #dee2e6;
            background-color: #f8f9fa;
            transition: all 0.2s ease;
        }
        .form-control:focus {
            background-color: #fff;
            box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.15);
            border-color: #0d6efd;
        }
        .btn-primary {
            background-color: #0d6efd;
            border: none;
            border-radius: 8px;
            padding: 0.75rem 1rem;
            font-weight: 600;
            transition: all 0.2s ease;
        }
        .btn-primary:hover {
            background-color: #0b5ed7;
            transform: translateY(-1px);
        }
        .form-text {
            color: #6c757d;
            font-size: 0.85rem;
        }
        .invalid-feedback {
            font-size: 0.85rem;
        }
        .login-link {
            color: #0d6efd;
            text-decoration: none;
            font-weight: 500;
        }
        .login-link:hover {
            text-decoration: underline;
        }
        .password-requirements {
            margin-top: 0.5rem;
            font-size: 0.85rem;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="card">
                    <div class="card-header text-center text-white">
                        <h3 class="mb-0">User Registration</h3>
                    </div>
                    <div class="card-body">
                        <!-- Only show error alert if error attribute exists -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mb-4">${error}</div>
                        </c:if>

                        <!-- Only show success alert if success attribute exists -->
                        <c:if test="${not empty success}">
                            <div class="alert alert-success mb-4">${success}</div>
                        </c:if>

                        <form action="/auth/register-form" method="post" class="needs-validation" novalidate>
                            <input type="hidden" name="_csrf" value="${_csrf.token}" />

                            <div class="mb-3">
                                <label for="firstName" class="form-label">First Name</label>
                                <input type="text" id="firstName" name="firstName" class="form-control"
                                       placeholder="Enter your first name" required autofocus>
                                <div class="invalid-feedback">Please enter your first name</div>
                            </div>

                            <div class="mb-3">
                                <label for="lastName" class="form-label">Last Name</label>
                                <input type="text" id="lastName" name="lastName" class="form-control"
                                       placeholder="Enter your last name" required>
                                <div class="invalid-feedback">Please enter your last name</div>
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" id="email" name="email" class="form-control"
                                       placeholder="Enter your email address" required>
                                <div class="invalid-feedback">Please enter a valid email address</div>
                            </div>

                            <div class="mb-4">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" id="password" name="password" class="form-control"
                                       placeholder="Create a password" required minlength="8">
                                <div class="invalid-feedback">Password must be at least 8 characters</div>
                                <div class="password-requirements">
                                    Password should be at least 8 characters and include letters and numbers
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary w-100 mb-3">Register</button>

                            <div class="text-center">
                                <p class="mb-0">Already have an account? <a href="/auth/login" class="login-link">Login here</a></p>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Form validation script -->
    <script>
    (function () {
        'use strict'

        // Fetch all forms we want to apply validation to
        var forms = document.querySelectorAll('.needs-validation')

        // Loop over them and prevent submission
        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }

                    form.classList.add('was-validated')
                }, false)
            })
    })()
    </script>
</body>
</html>
