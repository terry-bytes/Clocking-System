<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Role"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Employee</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editEmp.css">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    </head>
    <body>
        <%
            Role[] roles = Role.values();
            String message = (String) request.getAttribute("editEmployeeMessage");
            Employee employee = (Employee) request.getAttribute("employee");
            List<Employee> employees = (List<Employee>) request.getAttribute("employeeList");
        %>
        <% if (employee != null) {%>
        <div class="manager-container">
            <jsp:include page="sidebar.jsp"></jsp:include>
            <div class="left-main">
                <div class="login-box">
                    <div class="login-header">
                        <h3>Edit Employee</h3>
                    </div>
                    <form action="${pageContext.request.contextPath}/EmployeeServlet" method="POST">
                        <input type="hidden" name="employeeId" value="<%=employee.getEmployee_ID()%>"/>
                        
                        <div class="two-forms">
                            <div class="input-box">
                                <label for="firstName">First Name</label>
                                <input type="text"
                                       id="firstName"
                                       placeholder='First Name'
                                       name='firstName'
                                       class='input-field'
                                       autocomplete="off" required
                                       value="<%=employee.getFirst_name()%>"/>
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
                                       value="<%=employee.getLast_name()%>"/>
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
                                   value="<%=employee.getEmail()%>"/>
                            <i class="bx bx-envelope"></i>
                        </div>

                        <div class="input-box">
                            <label for="company">Company Name</label>
                            <input type="company"
                                   id="company"
                                   placeholder='Company Name'
                                   name='company'
                                   class='input-field'
                                   autocomplete="off" required
                                   value="<%=employee.getCompany()%>"/>
                            <i class="bx bx-building-house"></i>
                        </div>

                        <div class="input-box">
                            <label for="min_hours">Working Hours</label>
                            <input type="number"
                                   id="min_hours"
                                   placeholder='Working Hours'
                                   name='min_hours'
                                   class='input-field'
                                   autocomplete="off" required
                                   value="<%=employee.getMin_hours()%>"/>
                            <i class="bx bx-time-five"></i>
                        </div>

                        <div class="input-box">
                            <label for="password">Password</label>
                            <input type="password"
                                   id="password"
                                   placeholder='Password'
                                   name='password'
                                   class="input-field"
                                   autocomplete="off" required/>
                            <i class="bx bx-lock-alt"></i>
                        </div>

                        <div class="select-container">
                            <label for="roleSelector">Role</label>
                            <select class="select-box" name="role" id="roleSelector">
                                <option value="Staff" <%=employee.getRole() == Role.Staff ? "selected" : ""%>>Staff</option>
                                <option value="Manager" <%=employee.getRole() == Role.Manager ? "selected" : ""%>>Manager</option>
                            </select>
                        </div>

                        <%-- Success/Error Messages --%>
                        <%
                            String successMessage = (String) session.getAttribute("successMessage");
                            String errorMessage = (String) session.getAttribute("errorMessage");
                            session.removeAttribute("successMessage"); // Remove after displaying
                            session.removeAttribute("errorMessage"); // Remove after displaying
                        %>

                        <% if (successMessage != null) {%>
                        <div class="alert alert-success">
                            <%= successMessage %>
                        </div>
                        <% } %>

                        <% if (errorMessage != null) {%>
                        <div class="alert alert-danger">
                            <%= errorMessage %>
                        </div>
                        <% } %>

                        <% if (message != null) {%>
                        <p><%=message%></p>
                        <% } %>

                        <div class="input-submit">
                            <input name="submit" value="edit" hidden>
                            <button class="submit-btn" id="submit">Update Employee</button>
                        </div>
                    </form>
                </div>
                
                <%-- Display List of Employees if available --%>
                <% if (employees != null && !employees.isEmpty()) { %>
                <div class="employee-list-container">
                    <h4>Employee List</h4>
                    <table class="employee-list-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Email</th>
                                <th>Role</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Employee emp : employees) { %>
                            <tr>
                                <td><%= emp.getEmployee_ID() %></td>
                                <td><%= emp.getFirst_name() %></td>
                                <td><%= emp.getLast_name() %></td>
                                <td><%= emp.getEmail() %></td>
                                <td><%= emp.getRole() %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                <% } %>

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
