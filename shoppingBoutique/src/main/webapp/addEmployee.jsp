<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Role"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Employee</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editEmp.css">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
        <style>
            label{
                color: grey;
            }
        </style>
    </head>
    <body>
        
        <%
            Role[] roles = Role.values();
            String message = (String) request.getAttribute("addEmployeeMessage");
            Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
        %>
        <% if (employee != null && employee.getRole() != Role.Staff) { %>
        <div class="manager-container">
            <jsp:include page="sidebar.jsp"></jsp:include>
                <div class="left-main">
                    <div class="login-box">
                        <div class="login-header">
                            <h3>Add Employee</h3>
                        </div>
                        <form action="EmployeeServlet" method="post">
                            <div class="two-forms">
                                <div class="input-box">
                                    <label for="firstName">First Name</label>
                                    <input type="text"
                                           id="firstName"
                                           placeholder='First Name'
                                           name='firstName'
                                           class='input-field'
                                           autocomplete="off" required
                                           />
                                    <i class="bx bx-user"></i>
                                </div>
                                <div class="input-box">
                                    <label for="lastName">Last Name</label>
                                    <input type="text"
                                           id="lastName"
                                           placeholder='Last Name'
                                           name='lastName'
                                           class='input-field'
                                           autocomplete="off" required
                                           />
                                    <i class="bx bx-user"></i>
                                </div>
                            </div>

                            <div class="input-box">
                                <label for="email">Email</label>
                                <input type="email"
                                       id="email"
                                       placeholder='Email'
                                       name='email'
                                       class='input-field'
                                       autocomplete="off" required
                                       />
                                <i class="bx bx-envelope"></i>
                            </div>
                             <div class="select-container">
                                <label for="companySelector">Company</label>
                                <select class="select-box" name="company" id="companySelector">
                                    <option value="VMC MEDIA">VMC MEDIA</option>
                                    <option value="LEMPITSE">LEMPITSE</option>
                                    <option value="LEMPITSE">SENTOBURG</option>
                                </select>
                            </div>
                            <div class="input-box">
                                <label for="min_hours">Working Hours</label>
                                <input type="min_hours"
                                       id="email"
                                       placeholder='min_hours'
                                       name='min_hours'
                                       class='input-field'
                                       autocomplete="off" required
                                       />
                                <i class="bx bx-envelope"></i>
                            </div>

                            <div class="input-box">
                                <label for="password">Password</label>
                                <input type="password"
                                       id="password"
                                       placeholder='Password'
                                       name='password'
                                       class="input-field"
                                       autocomplete="off" required
                                       />
                                <i class="bx bx-lock-alt"></i>
                            </div>

                            <div class="select-container">
                                <label for="roleSelector">Role</label>
                                <select class="select-box" name="role" id="roleSelector">
                                    <option value="Staff">Staff</option>
                                    <option value="Manager">Manager</option>
                                </select>
                            </div>
                        <%
                            String successMessage = (String) session.getAttribute("successMessage");
                            String errorMessage = (String) session.getAttribute("errorMessage");
                            session.removeAttribute("successMessage"); // Remove after displaying
                            session.removeAttribute("errorMessage"); // Remove after displaying
                        %>

                        <% if (successMessage != null) {%>
                        <div class="alert alert-success">
                            <%= successMessage%>
                        </div>
                        <% } %>

                        <% if (errorMessage != null) {%>
                        <div class="alert alert-danger">
                            <%= errorMessage%>
                        </div>
                        <% } %>

                        <% if (message != null) {%>
                        <p><%=message%></p>
                        <% } %>
                        <div class="input-submit">
                            <input name="submit" value="add" hidden>
                            <button class="submit-btn" id="submit">Add Employee</button>
                        </div>
                    </form>
                </div>
            </div>

        </div>
        <% } else { %>
        <jsp:include page="unauthorized.jsp"/>
        <% }%>
        <script>
            var selectedRole = document.getElementById("roleSelector");

            selectedRole.addEventListener('change', function () {
                var role = selectedRole.value;
                // Handle role change if needed
            });
        </script>
    </body>
</html>
