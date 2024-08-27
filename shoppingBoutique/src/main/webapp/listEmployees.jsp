<%@ page import="java.util.List" %>
<%@ page import="ateam.Models.Employee" %>
<%@ page import="ateam.Models.Role" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

        <title>Employee List</title>
        <style>
            .back-button {
                font-size: 24px;
                color: #333;
                text-decoration: none;
            }

            .back-button:hover {
                color: #ff3333;
            }
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: 'Poppins', sans-serif;
            }
            body {
                background-image: linear-gradient(to right, #ff3333, #948d8c, #ff3333, #f0f5ef, #ff3333);
                height: 100vh;
                padding: 20px;
            }
            .container {

                max-width: 1200px;
                margin: 0 auto;
            }
            h1 {
                color: #333;
                text-align: center;
                margin-bottom: 20px;
            }
            .header {
                background-color: #c9756c;
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
            }
            .header input {
                padding: 8px;
                border: 2px solid #007bff;
                border-radius: 5px;
                font-size: 16px;
                transition: border-color 0.3s;
            }
            .header input:focus {
                border-color: #3498db;
                outline: none;
            }
            .btn-submit {
                background-color: #c9756c;
                border: none;
                color: white;
                padding: 10px 20px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 16px;
                margin: 4px 2px;
                cursor: pointer;
                border-radius: 5px;
                transition: background-color 0.3s;
            }
            .btn-submit:hover {
                background-color:  #4c3d3d;

            }
            .emp-tb {
                width: 100%;
                background: rgba(229, 229, 229, 1);
                border-collapse: collapse;
                font-size: 1em;
                margin-bottom: 20px;
                border-radius: 5px;
                overflow: hidden;
            }
            .emp-tb thead tr {
                background-color: #c9756c;
                color: #fff;
                text-align: left;
            }
            .emp-tb th, .emp-tb td {
                padding: 12px 15px;
            }
            .emp-tb tbody tr {
                border-bottom: 1px solid #ddd;
            }
            .emp-tb tbody tr:nth-of-type(even) {
                background-color: #c9756c;
            }
            .emp-tb tbody tr:last-of-type {
                border-bottom: 2px solid #007bff;
            }
            .emp-tb tbody tr:hover {
                background-color:  #4c3d3d;
                color: #007bff;
            }
            .pagination {
                display: flex;
                justify-content: center;
                margin-top: 20px;
            }
            .pagination a {
                margin: 0 5px;
                padding: 8px 16px;
                text-decoration: none;
                color: #c9756c;
                border: 1px solid #dee2e6;
                border-radius: 4px;
            }
            .pagination a.active {
                background-color: #c9756c;
                color: white;
                border: 1px solid #007bff;
            }
            .pagination a:hover:not(.active) {
                background-color:  #4c3d3d;
            }
            header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 10px;
                background-color: #f0f0f0;
                border-bottom: 2px solid #ccc;
            }
            h2 {
                flex: 1;
                text-align: center;
                margin: 0;
            }

        </style>
    </head>
    <body>

        <header>
            <a href="addEmployee.jsp" class="back-button">
                <i class="fas fa-arrow-left"></i> Back
            </a>
            <h2>Employee List</h2>
        </header>
        <div class="container">

            <div class="header">
                <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Search for employees..">
                <a class="btn-submit" href="addEmployee.jsp">Add Employee</a>
            </div>
            <table class="emp-tb">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Min Hours</th>
                        <th>Company</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Employee> employees = (List<Employee>) request.getAttribute("employees");
                        if (employees != null) {
                            for (Employee employee : employees) {
                    %>
                    <tr>
                        <td><%= employee.getEmployee_ID()%></td>
                        <td><%= employee.getFirst_name()%></td>
                        <td><%= employee.getLast_name()%></td>
                        <td><%= employee.getEmail()%></td>
                        <td><%= employee.getRole()%></td>
                        <td><%= employee.getMin_hours()%></td>
                        <td><%= employee.getCompany()%></td>
                        <td>
                            <a href="EmployeeServlet?submit=edit&employeeId=<%= employee.getEmployee_ID()%>" title="Edit">
                                <i class="fas fa-edit"></i>
                            </a>
                            <a href="EmployeeServlet?submit=deleteConfirm&employeeId=<%= employee.getEmployee_ID()%>" title="Delete">
                                <i class="fas fa-trash-alt"></i>
                            </a>
                        </td>

                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>

            <script>

                function updateTable() {
                    const table = document.querySelector('.emp-tb');
                    const tr = table.getElementsByTagName('tr');
                    const start = (currentPage - 1) * pageSize + 1;
                    const end = Math.min(start + pageSize - 1, tr.length - 1);

                    for (let i = 1; i < tr.length; i++) {
                        tr[i].style.display = (i >= start && i <= end) ? '' : 'none';
                    }

                    document.querySelectorAll('.pagination a').forEach(link => {
                        link.classList.remove('active');
                    });
                    document.querySelector(`.pagination a[onclick*="${currentPage}"]`).classList.add('active');
                }

                updateTable();

                function filterTable() {
                    var input, filter, table, tr, td, i, j;
                    input = document.getElementById('searchInput');
                    filter = input.value.toUpperCase();
                    table = document.querySelector('.emp-tb');
                    tr = table.getElementsByTagName('tr');

                    for (i = 1; i < tr.length; i++) {
                        tr[i].style.display = 'none';
                        td = tr[i].getElementsByTagName('td');
                        for (j = 0; j < td.length; j++) {
                            if (td[j] && td[j].innerHTML.toUpperCase().indexOf(filter) > -1) {
                                tr[i].style.display = '';
                                break;
                            }
                        }
                    }
                }
            </script>
    </body>
</html>
