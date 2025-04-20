<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create Preferences</title>
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
        }
    </style>
</head>
<body class="bg-light">
<div class="container mt-5">
    <h2>Create New Preferences</h2>
    <form:form modelAttribute="preference" method="POST" action="${pageContext.request.contextPath}/users/create-preference">
        <!-- Personal Preferences Section -->
        <div class="form-section">
            <h5 class="form-section-title">Personal Preferences</h5>
            <!-- Gender -->
            <div class="mb-3">
                <label class="form-label">Gender</label>
                <form:select path="gender" class="form-select">
                    <form:option value="" label="Select Gender"/>
                    <form:options items="${genders}" />
                </form:select>
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
                <form:input path="cleanlinessRating" type="number" min="1" max="5" class="form-control"/>
                <div class="form-text">1 = Very messy, 5 = Very clean</div>
            </div>
            <!-- Sleep Schedule -->
            <div class="mb-3">
                <label class="form-label">Sleep Schedule</label>
                <form:input path="sleepSchedule" type="text" placeholder="e.g., 10 PM - 6 AM" class="form-control"/>
            </div>
            <!-- Date of Birth -->
            <div class="mb-3">
                <label class="form-label">Date of Birth</label>
                <form:input path="dateOfBirth" type="date" class="form-control"/>
            </div>
            <!-- Occupation -->
            <div class="mb-3">
                <label class="form-label">Occupation</label>
                <form:select path="occupation" class="form-select">
                    <form:option value="" label="Select Occupation"/>
                    <form:options items="${occupations}" />
                </form:select>
            </div>
            <!-- Party Friendly -->
            <div class="mb-3">
                <label class="form-label">Party Friendly</label>
                <form:select path="partyFriendly" class="form-select">
                    <form:option value="" label="Select Party Friendliness"/>
                    <form:options items="${partyFriendlyOptions}" />
                </form:select>
            </div>
            <!-- Study/Work Schedule -->
            <div class="mb-3">
                <label class="form-label">Study/Work Schedule</label>
                <form:select path="studyWorkSchedule" class="form-select">
                    <form:option value="" label="Select Schedule"/>
                    <form:options items="${studyWorkScheduleOptions}" />
                </form:select>
            </div>
            <!-- Maximum Rent Preference -->
            <div class="mb-3">
                <label class="form-label">Maximum Rent Preference ($)</label>
                <form:input path="maxRentPreference" type="number" class="form-control"/>
            </div>
            <!-- Is Pet Friendly -->
            <div class="mb-3 form-check">
                <form:checkbox path="isPetFriendly" class="form-check-input" id="isPetFriendly"/>
                <label class="form-check-label" for="isPetFriendly">Pet Friendly</label>
            </div>
            <!-- Interests -->
            <div class="mb-3">
                <label class="form-label">Interests/Hobbies</label>
                <form:textarea path="interests" rows="3" class="form-control"/>
            </div>
        </div>

        <!-- Roommate Preferences Section -->
        <div class="form-section">
            <h5 class="form-section-title">Roommate Preferences</h5>
            <!-- Preferred Roommate Gender -->
            <div class="mb-3">
                <label class="form-label">Preferred Roommate Gender</label>
                <form:select path="preferredRoommateGender" class="form-select">
                    <form:option value="" label="No Preference"/>
                    <form:options items="${preferredRoommateGenders}" />
                </form:select>
            </div>
            <!-- Preferred Age Range -->
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Preferred Minimum Age</label>
                    <form:input path="preferredMinAge" type="number" class="form-control"/>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Preferred Maximum Age</label>
                    <form:input path="preferredMaxAge" type="number" class="form-control"/>
                </div>
            </div>
            <!-- Guest Frequency -->
            <div class="mb-3">
                <label class="form-label">Guest Frequency</label>
                <form:select path="guestFrequency" class="form-select">
                    <form:option value="" label="Select Frequency"/>
                    <form:options items="${guestFrequencyOptions}" />
                </form:select>
            </div>
            <!-- Noise Tolerance -->
            <div class="mb-3">
                <label class="form-label">Noise Tolerance</label>
                <form:select path="noiseTolerance" class="form-select">
                    <form:option value="" label="Select Noise Tolerance"/>
                    <form:options items="${noiseToleranceOptions}" />
                </form:select>
            </div>
            <!-- Roommate Lifestyle -->
            <div class="mb-3">
                <label class="form-label">Roommate Lifestyle</label>
                <form:select path="roommateLifestyle" class="form-select">
                    <form:option value="" label="Select Lifestyle"/>
                    <form:options items="${roommateLifestyleOptions}" />
                </form:select>
            </div>
            <!-- Room Type Preference -->
            <div class="mb-3">
                <label class="form-label">Room Type Preference</label>
                <form:select path="roomTypePreference" class="form-select">
                    <form:option value="" label="No Preference"/>
                    <form:options items="${roomTypePreferenceOptions}" />
                </form:select>
            </div>
        </div>

        <!-- Housing Preferences Section -->
        <div class="form-section">
            <h5 class="form-section-title">Housing Preferences</h5>
            <!-- Year of Intake -->
            <div class="mb-3">
                <label class="form-label">Year of Intake</label>
                <form:input path="yearOfIntake" type="number" min="${currentYear}" max="${currentYear + 10}" class="form-control" />
            </div>
        </div>

        <!-- Preferred Areas Section -->
        <div class="form-section">
            <h5 class="form-section-title">Preferred Areas</h5>
            <div class="row">
                <c:forEach items="${areaOptions}" var="area">
                    <div class="col-md-4">
                        <div class="form-check mb-2">
                            <form:checkbox path="preferredAreas" value="${area}" class="form-check-input" id="area_${area}" />
                            <label class="form-check-label" for="area_${area}">${area}</label>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Languages Known Section -->
        <div class="form-section">
            <h5 class="form-section-title">Languages Known</h5>
            <div class="row">
                <c:forEach items="${languages}" var="lang" varStatus="status">
                    <div class="col-md-4">
                        <div class="form-check mb-2">
                            <input type="checkbox" name="languagesKnown" value="${lang}" class="form-check-input" id="lang${status.index}" />
                            <label class="form-check-label" for="lang${status.index}">${lang}</label>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Submit Button -->
        <div class="mt-4">
            <button type="submit" class="btn btn-primary">Create Preferences</button>
            <a href="${pageContext.request.contextPath}/users/my-listings" class="btn btn-secondary">Cancel</a>
        </div>
    </form:form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
