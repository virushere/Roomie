<!--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>-->
<!--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>-->
<!--<!DOCTYPE html>-->
<!--<html>-->
<!--<head>-->
<!--    <meta charset="UTF-8">-->
<!--    <title> OLD NO USING - Edit Profile</title>-->
<!--    <style>-->
<!--        body {-->
<!--            font-family: Arial, sans-serif;-->
<!--            line-height: 1.6;-->
<!--            margin: 0;-->
<!--            padding: 20px;-->
<!--            background-color: #f4f4f4;-->
<!--        }-->
<!--        h1 {-->
<!--            color: #333;-->
<!--            margin-bottom: 20px;-->
<!--        }-->
<!--        form {-->
<!--            background-color: #fff;-->
<!--            padding: 20px;-->
<!--            border-radius: 5px;-->
<!--            box-shadow: 0 0 10px rgba(0,0,0,0.1);-->
<!--        }-->
<!--        div {-->
<!--            margin-bottom: 15px;-->
<!--        }-->
<!--        label {-->
<!--            display: block;-->
<!--            margin-bottom: 5px;-->
<!--            color: #666;-->
<!--        }-->
<!--        input[type="text"], input[type="email"], textarea {-->
<!--            width: 100%;-->
<!--            padding: 8px;-->
<!--            border: 1px solid #ddd;-->
<!--            border-radius: 4px;-->
<!--            box-sizing: border-box;-->
<!--        }-->
<!--        input[type="submit"] {-->
<!--            background-color: #4CAF50;-->
<!--            color: white;-->
<!--            padding: 10px 15px;-->
<!--            border: none;-->
<!--            border-radius: 4px;-->
<!--            cursor: pointer;-->
<!--        }-->
<!--        input[type="submit"]:hover {-->
<!--            background-color: #45a049;-->
<!--        }-->

        /* Radio button styling */
<!--        .radio-group {-->
<!--            display: flex;-->
<!--            align-items: center;-->
<!--        }-->

<!--        .radio-option {-->
<!--            display: flex;-->
<!--            align-items: center;-->
<!--            margin-right: 20px;-->
<!--        }-->

<!--        .radio-option input[type="radio"] {-->
<!--            margin-right: 5px;-->
<!--        }-->

<!--        .radio-label {-->
<!--            font-weight: bold;-->
<!--        }-->

<!--        .active-label {-->
<!--            color: #4CAF50;-->
<!--        }-->

<!--        .inactive-label {-->
<!--            color: #f44336;-->
<!--        }-->
<!--    </style>-->
<!--</head>-->
<!--<body>-->
<!--    <h1>Edit Profile</h1>-->
<!--    <form:form action="/users/profile" method="post" modelAttribute="user" enctype="multipart/form-data">-->
<!--            <div>-->
<!--                <label for="profileImageFile">Profile Picture:</label>-->
                <!-- IMPORTANT: This must be a regular input, not form:input -->
<!--                <input type="file" name="profileImageFile" id="profileImageFile" />-->

                <!-- Add a hidden field to retain the current profile picture path -->
<!--                <form:hidden path="profilePicture" />-->
<!--            </div>-->
<!--        <div>-->
<!--            <label for="firstName">First Name:</label>-->
<!--            <form:input path="firstName" id="firstName" />-->
<!--        </div>-->
<!--        <div>-->
<!--            <label for="lastName">Last Name:</label>-->
<!--            <form:input path="lastName" id="lastName" />-->
<!--        </div>-->
<!--        <div>-->
<!--            <label for="email">Email:</label>-->
<!--            <form:input path="email" id="email" />-->
<!--        </div>-->
<!--        <div>-->
<!--            <label for="phoneNumber">Phone:</label>-->
<!--            <form:input path="phoneNumber" id="phoneNumber" />-->
<!--        </div>-->
<!--        <div>-->
<!--            <label for="bio">Bio:</label>-->
<!--            <form:textarea path="bio" id="bio" rows="3" />-->
<!--        </div>-->
<!--        <div>-->
<!--            <label>Actively Looking:</label>-->
<!--            <div class="radio-group">-->
<!--                <div class="radio-option">-->
<!--                    <form:radiobutton path="isActive" id="activeYes" value="true" />-->
<!--                    <label for="activeYes" class="radio-label active-label">Actively Looking</label>-->
<!--                </div>-->
<!--                <div class="radio-option">-->
<!--                    <form:radiobutton path="isActive" id="activeNo" value="false" />-->
<!--                    <label for="activeNo" class="radio-label inactive-label">Not Actively Looking</label>-->
<!--                </div>-->
<!--            </div>-->
<!--        </div>-->
<!--        <div>-->
<!--            <input type="submit" value="Update Profile" />-->
<!--        </div>-->
<!--    </form:form>-->

<!--    <script>-->
<!--        document.addEventListener('DOMContentLoaded', function() {-->
            // Form validation
<!--            document.querySelector('form').addEventListener('submit', function(event) {-->
<!--                const firstName = document.getElementById('firstName').value.trim();-->
<!--                const lastName = document.getElementById('lastName').value.trim();-->
<!--                const email = document.getElementById('email').value.trim();-->

<!--                if (!firstName || !lastName || !email) {-->
<!--                    event.preventDefault();-->
<!--                    alert('Please fill in all required fields (First Name, Last Name, and Email)');-->
<!--                }-->
<!--            });-->
<!--        });-->
<!--    </script>-->
<!--</body>-->
<!--</html>-->