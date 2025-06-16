package ar.uba.fi.ingsoft1.user;

import ar.uba.fi.ingsoft1.exception.ErrorAtSendingEmailException;
import org.springframework.beans.factory.annotation.Value;
import ar.uba.fi.ingsoft1.order.OrderDTO;
import org.springframework.stereotype.Service;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import java.util.stream.Collectors;


@Service
public class EmailService {

    @Value("${app.resend.apikey}")
    private String API_KEY;

    private void sendEmail(String email, String subject, String body) {
        Resend resend = new Resend(API_KEY);


        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Grupo11 <ingsoft1grupo11@resend.dev>")
                .to(email)
                .subject(subject)
                .html(body)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("Mail " + data.getId() + "sended to " + email + ". Subject: " + subject);
        } catch (Exception e) {
            throw new ErrorAtSendingEmailException(e.getMessage());
        }
    }

    public void sendConfirmationEmail(String email, String confirmationLink) {
        String subject = "New account confirmation email";
        String body = """
            <!DOCTYPE html>
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9;">
                    <h2 style="text-align: center; color: #4CAF50;">Confirm Your Registration</h2>
                    <p>To complete your registration, please confirm your email address by clicking the button below:</p>
                    <div style="text-align: center; margin: 20px 0;">
                        <a href="%s" style="background-color: #4CAF50; color: white; text-decoration: none; padding: 10px 20px; border-radius: 5px; font-size: 16px;">Confirm Your Email</a>
                    </div>
                    <p>If you didn’t request this, you can safely ignore this email.</p>
                </div>
            </body>
            </html>
        """.formatted(confirmationLink);
        sendEmail(email, subject, body);
    }


    public void sendPasswordResetEmail(String email, String resetPasswordLink) {
        String subject = "Reset your password";
        String body = """
            <!DOCTYPE html>
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9;">
                    <h2 style="text-align: center; color: #FF5722;">Reset Your Password</h2>
                    <p>You can reset it by clicking the button below:</p>
                    <div style="text-align: center; margin: 20px 0;">
                        <a href="%s" style="background-color: #FF5722; color: white; text-decoration: none; padding: 10px 20px; border-radius: 5px; font-size: 16px;">Reset Password</a>
                    </div>
                    <p>If you didn’t request a password reset, you can safely ignore this email. Your password will remain unchanged.</p>
                </div>
            </body>
            </html>
        """.formatted(resetPasswordLink);

        sendEmail(email, subject, body);
    }

    public void sendCheckOutEmail(String email, OrderDTO order) {
        String emailBody = """
            <!DOCTYPE html>
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2>Your order was created successfully!</h2>
                <p>Here are the details:</p>
                <ul>
                    <li><strong>Date:</strong> %s</li>
                    <li><strong>Owner ID:</strong> %s</li>
                    <li><strong>Order State:</strong> %s</li>
                    <li><strong>Products:</strong>
                        <ul>
                            %s
                        </ul>
                    </li>
                </ul>
                <p>Thank you for your order!</p>
            </body>
            </html>
        """.formatted(
                order.orderDate(),
                order.idOwner(),
                order.state(),
                order.items().stream()
                    .map(item -> "<li>" + item.amount() + " x " + item.product().productName() + "</li>")
                    .collect(Collectors.joining())
        );

        String subject = "New order email - Date: " + order.orderDate();

        sendEmail(email, subject, emailBody);
    }
}