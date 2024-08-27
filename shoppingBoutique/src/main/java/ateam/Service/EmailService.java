/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Service;

import ateam.Models.Email;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Train 09
 */
public interface EmailService {

    void sendMail(Email email);

    void sendPasswordResetMail(String email, String otp);
}
