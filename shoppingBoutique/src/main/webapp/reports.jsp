<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Employee Clocking Report</title>
      
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <style>
            .select-container {
                margin-bottom: 20px;
                position: relative;
            }

            .select-container label {
                display: block;
                margin-bottom: 5px;
            }

            .select-box {
                width: 100%;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
                box-sizing: border-box;
            }

            body {
                background: #aecdf0;
                background-image: linear-gradient(to right, #ff3333, #948d8c, #ff3333, #f0f5ef, #ff3333);
            }

            form {
                width: 300px;
                margin: 0 auto;
            }

            label {
                display: block;
                margin: 10px 0 5px;
            }

            input[type="text"], input[type="date"], select, input[type="submit"] {
                width: 100%;
                padding: 8px;
                margin-bottom: 10px;
                box-sizing: border-box;
            }

            input[type="submit"] {
                background-color: #c9756c;
                color: white;
                border: none;
                cursor: pointer;
            }

            input[type="submit"]:hover {
                background-color: #4c3d3d;
            }

            /* Icon Styles */
            .icon-option {
                text-align: center;
                margin: 20px 0;
            }

            .icon-option i {
                font-size: 50px;
                margin: 0 20px;
                cursor: pointer;
                color: #4c3d3d;
            }

            .icon-option i:hover {
                color: #e8e4e2;
            }

            /* Hide elements initially */
            .hidden {
                display: none;
            }

            /* Header and Back Button Styles */
            header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 10px;
                background-color: #f0f0f0;
                border-bottom: 2px solid #ccc;
            }

            .back-button {
                font-size: 24px;
                color: #333;
                text-decoration: none;
            }

            .back-button:hover {
                color: #ff3333;
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
            <h2>Generate Employee Clocking Report</h2>
        </header>

        <div class="icon-option">
            <label>Choose Filter Option:</label>
          
            <i class="fas fa-user" id="employeeIcon" onclick="showEmployeeForm()" title="Filter by Employee"></i>
           
            <i class="fas fa-building" id="companyIcon" onclick="showCompanyForm()" title="Filter by Company"></i>
        </div>

     
        <form action="ReportServlet" method="post" id="employeeForm" class="hidden">
            <label for="first_name">First Name:</label>
            <input type="text" id="first_name" name="first_name">

            <label for="last_name">Last Name:</label>
            <input type="text" id="last_name" name="last_name">

            <label for="start_date">Start Date:</label>
            <input type="date" id="start_date" name="start_date" required>

            <label for="end_date">End Date:</label>
            <input type="date" id="end_date" name="end_date" required>

            <div class="select-container">
                <label for="companySelector">Company</label>
                <select class="select-box" name="company" id="companySelector">
                    <option value="VMC MEDIA">VMC MEDIA</option>
                    <option value="LEMPITSE">LEMPITSE</option>
                    <option value="SENTOBURG">SENTOBURG</option>
                </select>
            </div>

            <div class="select-container">
                <label for="format">Report Format</label>
                <select class="select-box" name="format" id="format">
                    <option value="csv">CSV</option>
                    <option value="pdf">PDF</option>
                </select>
            </div>

            <input type="submit" value="Generate Report">
        </form>

        <!-- Company Form -->
        <form action="ReportServlet" method="post" id="companyForm" class="hidden">
            <label for="companySelector">Company</label>
            <select class="select-box" name="company" id="companySelector">
                <option value="VMC MEDIA">VMC MEDIA</option>
                <option value="LEMPITSE">LEMPITSE</option>
                <option value="SENTOBURG">SENTOBURG</option>
            </select>

            <label for="start_date">Start Date:</label>
            <input type="date" id="start_date" name="start_date" required>

            <label for="end_date">End Date:</label>
            <input type="date" id="end_date" name="end_date" required>

            <div class="select-container">
                <label for="format">Report Format</label>
                <select class="select-box" name="format" id="format">
                    <option value="csv">CSV</option>
                    <option value="pdf">PDF</option>
                </select>
            </div>

            <input type="submit" value="Generate Report">
        </form>

        <script>
       
            function showEmployeeForm() {
                document.getElementById('employeeForm').style.display = 'block';
                document.getElementById('companyForm').style.display = 'none';
            }

           
            function showCompanyForm() {
                document.getElementById('employeeForm').style.display = 'none';
                document.getElementById('companyForm').style.display = 'block';
            }
        </script>

    </body>
</html>
