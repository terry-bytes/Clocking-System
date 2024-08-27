<%@page import="ateam.Models.Employee"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Time Tracking</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/boxicons/2.0.9/css/boxicons.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            text-align: center;
            margin: 0;
            padding: 0;
            background-image: linear-gradient(to right, #ff3333  ,#948d8c ,#ff3333,#f0f5ef,#ff3333 );
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px;
            background-image: linear-gradient(to right, #ff3333  ,#948d8c ,#ff3333,#f0f5ef,#ff3333 );
            color: #fff;
        }
        .user-info {
            font-size: 18px;
            color: #fff;
        }
        .user-info i {
            margin-right: 8px;
        }
        .logout {
            font-size: 18px;
            color: #fff;
        }
        .logout a {
            color: #fff;
            text-decoration: none;
            display: flex;
            align-items: center;
        }
        .logout i {
            margin-right: 8px;
        }
        .container {
            max-width: 500px;
            margin: 0 auto;
            padding: 20px;
            background: rgba(229, 229, 229, 1);
            border-radius: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin-top: 50px;
        }
         @keyframes fadeInSlideUp {
            0% {
                opacity: 0;
                transform: translateY(20px);
            }
            100% {
                opacity: 1;
                transform: translateY(0);
            }
        }

        h1 {
            animation: fadeInSlideUp 2s ease-out;
        }
        #clock {
            font-size: 60px;
            font-weight: bold;
            color: #333;
            margin-bottom: 30px;
            padding: 10px;
            background: #e9ecef;
            border-radius: 8px;
        }
        .button {
            font-size: 18px;
            padding: 15px 25px;
            margin: 10px;
            cursor: pointer;
            border: none;
            border-radius: 5px;
            color: #fff;
            transition: background-color 0.3s;
        }
        .button.clock-in {
            background-color: #28a745; /* Green for Clock In */
        }
        .button.clock-in:hover {
            background-color: #218838; /* Darker green on hover */
        }
        .button.clock-out {
            background-color: #dc3545; /* Red for Clock Out */
        }
        .button.clock-out:hover {
            background-color: #c82333; /* Darker red on hover */
        }
    </style>
    <script>
        function updateClock() {
            var now = new Date();
            var hours = now.getHours();
            var minutes = now.getMinutes();
            var seconds = now.getSeconds();
            hours = hours < 10 ? '0' + hours : hours;
            minutes = minutes < 10 ? '0' + minutes : minutes;
            seconds = seconds < 10 ? '0' + seconds : seconds;
            var strTime = hours + ':' + minutes + ':' + seconds;
            document.getElementById('clock').innerHTML = strTime;
        }
        window.onload = function() {
            updateClock();
            setInterval(updateClock, 1000);
        }
    </script>
</head>
<body>
    <div class="header">
        <div class="user-info">
            <%
                Employee loggedInUser = (Employee) session.getAttribute("Employee");
                if (loggedInUser != null) {
            %>
                <i class="fas fa-user-circle"></i>
                <%= loggedInUser.getFirst_name() %> <%= loggedInUser.getLast_name() %>
            <%
                } else {
            %>
                <i class="fas fa-user-circle"></i>
                Guest
            <%
                }
            %>
        </div>
        <div class="logout">
            <a href="EmployeeServlet?submit=logout">
                <i class='bx bx-log-out'></i>
                <span class="text nav-text">Logout</span>
            </a>
        </div>
    </div>
    
    <div class="container">
       
        <div id="clock"></div>
        <% 
            String message = (String) request.getAttribute("message");
            if (message != null) {
        %>
            <div class="message <%= message.contains("successfully") ? "success" : "error" %>">
                <%= message %>
            </div>
        <% 
            } 
        %>
        
        <form action="ClockInOutServlet" method="post">
            <input type="submit" name="action" value="Clock In" class="button clock-in">
            <input type="submit" name="action" value="Clock Out" class="button clock-out">
        </form>
    </div>
</body>
</html>
