<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Error ${status}</title>
  <link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
    rel="stylesheet"/>
  <style>
    body { background-color: #f8f9fa; }
    .error-container {
      min-height: 100vh; display: flex;
      align-items: center; justify-content: center;
    }
    .error-card {
      max-width: 420px;
      background: #fff; padding: 2rem;
      border-radius: .5rem;
      box-shadow: 0 .25rem .5rem rgba(0,0,0,0.1);
      text-align: center;
    }
    .error-gif { width: 180px; margin-bottom: 1.5rem; }
  </style>
</head>
<body>
  <div class="container error-container">
    <div class="error-card">
      <c:choose>
        <c:when test="${status == 404}">
          <img src="https://media.giphy.com/media/3o6gbbuLW76jkt8vIc/giphy.gif"
               alt="404 Not Found" class="error-gif"/>
          <h1 class="display-6 mb-2">404 — Page Not Found</h1>
          <p class="mb-3">We can’t seem to find the page you’re looking for.</p>
        </c:when>
        <c:when test="${status == 500}">
          <img src="https://media.giphy.com/media/3oEjI6SIIHBdRxXI40/giphy.gif"
               alt="500 Internal Server Error" class="error-gif"/>
          <h1 class="display-6 mb-2">500 — Server Error</h1>
          <p class="mb-3">Oops! Something went wrong on our end.</p>
        </c:when>
        <c:otherwise>
          <img src="https://media.giphy.com/media/14uQ3cOFteDaU/giphy.gif"
               alt="Oops!" class="error-gif"/>
          <h1 class="display-6 mb-2">Error ${status}</h1>
          <p class="mb-3">Sorry, something unexpected happened.</p>
        </c:otherwise>
      </c:choose>
      <a href="http://localhost:8081/home/roommate-listings" class="btn btn-primary">
        ← Back to Listings
      </a>
    </div>
  </div>
  <script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">
  </script>
</body>
</html>
