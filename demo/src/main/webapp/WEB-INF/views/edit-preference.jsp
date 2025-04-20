<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Preferences</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-section {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .form-section-title {
            margin-bottom: 15px;
            color: #0d6efd;
            border-bottom: 1px solid #ccc;
            padding-bottom: 5px;
        }
    </style>
</head>
<body class="bg-light">
    <!-- Navigation Bar -->
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

    <div class="container mt-4">
        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h2 class="mb-0">Edit Preferences</h2>
            </div>
            <div class="card-body">
                <form:form modelAttribute="preference" method="POST" action="${pageContext.request.contextPath}/users/update-preference/${preference.id}">

                    <!-- Personal Information Section -->
                    <div class="form-section">
                        <div class="form-section-title">Personal Information</div>
                        <!-- Gender -->
                        <div class="mb-3">
                            <label class="form-label">Gender</label>
                            <form:select path="gender" class="form-select">
                                <form:option value="" label="Select Gender"/>
                                <form:options items="${genders}" />
                            </form:select>
                        </div>
                        <!-- Date of Birth -->
                        <div class="mb-3">
                            <label class="form-label">Date of Birth</label>
                            <form:input path="dateOfBirth" type="date" class="form-control" max="${currentDate}"/>
                            <div class="form-text">Please enter a valid date. Future dates are not allowed.</div>
                        </div>
                        <!-- Food Preference -->
                        <div class="mb-3">
                            <label class="form-label">Food Preference</label>
                            <form:select path="foodPref" class="form-select">
                                <form:option value="" label="Select Food Preference"/>
                                <form:options items="${foodPreferences}" />
                            </form:select>
                        </div>
                        <!-- Alcohol Preference -->
                        <div class="mb-3">
                            <label class="form-label">Alcohol Preference</label>
                            <form:select path="alcoholPref" class="form-select">
                                <form:option value="" label="Select Alcohol Preference"/>
                                <form:options items="${alcoholPreferences}" />
                            </form:select>
                        </div>
                        <!-- Smoking -->
                        <div class="mb-3 form-check">
                            <form:checkbox path="smoking" class="form-check-input" id="smoking" />
                            <label class="form-check-label" for="smoking">Smoking</label>
                        </div>
                        <!-- Cleanliness Rating -->
                        <div class="mb-3">
                            <label class="form-label">Cleanliness Rating (1-5)</label>
                            <form:input path="cleanlinessRating" type="number" min="1" max="5" class="form-control" />
                            <div class="form-text">1 = Very messy, 5 = Very clean</div>
                        </div>
                        <!-- Sleep Schedule -->
                        <div class="mb-3">
                            <label class="form-label">Sleep Schedule</label>
                            <form:input path="sleepSchedule" type="text" placeholder="e.g., 10 PM - 6 AM" class="form-control" />
                        </div>
                    </div>

                    <!-- Educational & Career Information Section -->
                    <div class="form-section">
                        <div class="form-section-title">Educational &amp; Career Information</div>
                        <!-- Year of Intake -->
                        <div class="mb-3">
                            <label class="form-label">Year of Intake</label>
                            <form:input path="yearOfIntake" type="number" min="${currentYear}" max="${currentYear + 10}" class="form-control"/>
                        </div>
                        <!-- Occupation -->
                        <div class="mb-3">
                            <label class="form-label">Occupation</label>
                            <form:select path="occupation" class="form-select">
                                <form:option value="" label="Select Occupation"/>
                                <form:options items="${occupations}" />
                            </form:select>
                        </div>
                    </div>

                    <!-- Housing Preferences Section -->
                    <div class="form-section">
                        <div class="form-section-title">Housing Preferences</div>
                        <!-- Lease Option -->
                        <div class="mb-3">
                            <label class="form-label">Lease Option</label>
                            <form:select path="leaseOption" class="form-select">
                                <form:option value="" label="Select Lease Option"/>
                                <form:options items="${leaseOptions}" />
                            </form:select>
                        </div>
                        <!-- Maximum Roommates -->
                        <div class="mb-3">
                            <label class="form-label">Maximum Number of Roommates</label>
                            <form:input path="maxRoommates" type="number" min="1" class="form-control" />
                        </div>
                        <!-- Maximum Rent Preference -->
                        <div class="mb-3">
                            <label class="form-label">Maximum Rent Preference ($)</label>
                            <form:input path="maxRentPreference" type="number" class="form-control" placeholder="e.g., 1000"/>
                        </div>
                        <!-- Pet Friendly -->
                        <div class="mb-3 form-check">
                            <form:checkbox path="isPetFriendly" class="form-check-input" id="isPetFriendly"/>
                            <label class="form-check-label" for="isPetFriendly">Pet Friendly</label>
                        </div>
                        <!-- Interests/Hobbies -->
                        <div class="mb-3">
                            <label class="form-label">Interests / Hobbies</label>
                            <form:textarea path="interests" rows="3" class="form-control" placeholder="List your interests or hobbies"></form:textarea>
                        </div>
                    </div>

                    <!-- Roommate Preferences Section -->
                    <div class="form-section">
                        <div class="form-section-title">Roommate Preferences</div>
                        <!-- Preferred Roommate Gender -->
                        <div class="mb-3">
                            <label class="form-label">Preferred Roommate Gender</label>
                            <form:select path="preferredRoommateGender" class="form-select">
                                <form:option value="" label="No Preference"/>
                                <form:option value="MALE" label="Male"/>
                                <form:option value="FEMALE" label="Female"/>
                            </form:select>
                        </div>
                        <!-- Preferred Age Range -->
                        <div class="row mb-3">
                            <div class="col">
                                <label class="form-label">Minimum Preferred Age</label>
                                <form:input path="preferredMinAge" type="number" class="form-control" placeholder="Min Age" />
                            </div>
                            <div class="col">
                                <label class="form-label">Maximum Preferred Age</label>
                                <form:input path="preferredMaxAge" type="number" class="form-control" placeholder="Max Age" />
                            </div>
                        </div>
                        <!-- Guest Frequency -->
                        <div class="mb-3">
                            <label class="form-label">Guest Frequency</label>
                            <form:select path="guestFrequency" class="form-select">
                                <form:option value="" label="Select Frequency"/>
                                <form:option value="RARELY" label="Rarely"/>
                                <form:option value="SOMETIMES" label="Sometimes"/>
                                <form:option value="OFTEN" label="Often"/>
                            </form:select>
                        </div>
                        <!-- Noise Tolerance -->
                        <div class="mb-3">
                            <label class="form-label">Noise Tolerance</label>
                            <form:select path="noiseTolerance" class="form-select">
                                <form:option value="" label="Select Noise Tolerance"/>
                                <form:option value="LOW" label="Low"/>
                                <form:option value="MEDIUM" label="Medium"/>
                                <form:option value="HIGH" label="High"/>
                            </form:select>
                        </div>
                        <!-- Roommate Lifestyle -->
                        <div class="mb-3">
                            <label class="form-label">Roommate Lifestyle</label>
                            <form:select path="roommateLifestyle" class="form-select">
                                <form:option value="" label="Select Lifestyle"/>
                                <form:option value="INTROVERT" label="Introvert"/>
                                <form:option value="EXTROVERT" label="Extrovert"/>
                                <form:option value="BALANCED" label="Balanced"/>
                            </form:select>
                        </div>
                        <!-- Room Type Preference -->
                        <div class="mb-3">
                            <label class="form-label">Room Type Preference</label>
                            <form:select path="roomTypePreference" class="form-select">
                                <form:option value="" label="No Preference"/>
                                <form:option value="PRIVATE" label="Private"/>
                                <form:option value="SHARED" label="Shared"/>
                            </form:select>
                        </div>
                    </div>

                    <!-- Languages Known Section -->
                    <div class="form-section">
                        <div class="form-section-title">Languages Known</div>
                        <div class="mb-3">
                            <c:forEach items="${languages}" var="lang" varStatus="status">
                                <div class="form-check form-check-inline">
                                    <input type="checkbox" name="languagesKnown" value="${lang}" class="form-check-input" id="lang${status.index}"
                                        <c:forEach items="${preference.languagesKnown}" var="userLang">
                                            <c:if test="${userLang == lang}">checked="checked"</c:if>
                                        </c:forEach>/>
                                    <label class="form-check-label" for="lang${status.index}">${lang}</label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <!-- Preferred Areas Section -->
                    <div class="form-section">
                        <div class="form-section-title">Preferred Areas</div>
                        <div class="mb-3">
                            <c:forEach items="${preferredAreas}" var="area" varStatus="status">
                                <div class="form-check form-check-inline">
                                    <input type="checkbox" name="preferredAreas" value="${area}" class="form-check-input" id="area${status.index}"
                                        <c:forEach items="${preference.preferredAreas}" var="userArea">
                                            <c:if test="${userArea == area}">checked="checked"</c:if>
                                        </c:forEach>/>
                                    <label class="form-check-label" for="area${status.index}">${area}</label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <!-- Submit Button -->
                    <div class="d-grid gap-2">
                        <input type="submit" value="Save Changes" class="btn btn-primary"/>
                        <a href="${pageContext.request.contextPath}/users/my-listings" class="btn btn-outline-secondary">Cancel</a>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
