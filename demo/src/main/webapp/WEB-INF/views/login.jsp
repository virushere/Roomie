<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Login</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg">
                    <div class="card-header text-center bg-success text-white">
                        <h3>User Login</h3>
                    </div>
                    <div class="card-body">
                        <%-- Display error message if login fails --%>
                        <% if (request.getAttribute("errorMessage") != null) { %>
                            <div class="alert alert-danger text-center" role="alert">
                                <%= request.getAttribute("errorMessage") %>
                            </div>
                        <% } %>

                        <%-- Login form --%>
                            <form action="/auth/login-form" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <div class="mb-3">
                                   <label for="username" class="form-label">Email</label>
                                   <input type="email" id="username" name="username" class="form-control" required>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="password" id="password" name="password" class="form-control" required>
                                </div>
                                <button type="submit" class="btn btn-success w-100">Login</button>
                            </form>
                        <%-- Link to registration page --%>
                        <p class="text-center mt-3">
                            Don't have an account?
                            <a href="/auth/register" class="text-primary">Register here</a>.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
