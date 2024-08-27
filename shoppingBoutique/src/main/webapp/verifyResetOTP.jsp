<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Verify Reset OTP</title>

    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');
        @import url('https://fonts.googleapis.com/css2?family=Pacifico&display=swap');
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-image: linear-gradient(to right, #ff3333, #948d8c, #ff3333, #f0f5ef, #ff3333);
        }

        .otp-verification-box {
            background: rgba(229, 229, 229, 1);
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 400px; 
            text-align: center;
        }

        .otp-verification-box h3 {
            margin: 10px 0;
        }

        .input-box {
            margin: 30px 0;
            position: relative;
        }

        .input-box input {
            width: 100%;
            padding: 15px;
            box-sizing: border-box;
            border: 2px solid #ccc;
            border-radius: 5px;
            transition: .3s ease;
        }

        .input-box input:focus {
            border-color: #c9756c; 
            outline: none;
        }

        .input-box input::placeholder {
            font-size: 14px;
            font-weight: 600;
        }

        .input-submit {
            margin: 15px 0;
        }

        .input-submit button {
            width: 100%;
            padding: 10px;
            background-color: #c9756c;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .input-submit button:hover {
            background-color: #4c3d3d;
        }

        .error-message {
            color: red;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <div class="otp-verification-box">
      
        <h3>Verify Password Reset OTP</h3>
        <form action="EmployeeServlet" method="post">
            <input type="hidden" name="submit" value="verifyResetOTP">
            <div class="input-box">
                <input type="text" id="otp" name="otp" placeholder="Enter OTP" required />
            </div>
            <div class="input-submit">
                <button type="submit">Verify</button>
            </div>
        </form>
        <c:if test="${not empty otpMessage}">
            <p class="error-message">${otpMessage}</p>
        </c:if>
    </div>
</body>
</html>
