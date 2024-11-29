package com.example.javajsp121;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OrderServ extends HttpServlet
{
    private final List<Pizza> pizzaList = new ArrayList<>();
    @Override
    public void init() throws ServletException
    {
        pizzaList.add(new Pizza("Caesar", List.of("Chicken", "Parmesan", "Lettuce")));
        pizzaList.add(new Pizza("Burger", List.of("Beef", "Cheese", "Pickles")));
        pizzaList.add(new Pizza("Cheese", List.of("Mozzarella", "Parmesan", "Cheddar")));
        pizzaList.add(new Pizza("Pepperoni", List.of("Pepperoni", "Tomato Sauce", "Mozzarella")));
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action = request.getParameter("action");
        if ("create".equals(action))
        {
            String customPizzaName = request.getParameter("customPizzaName");
            String[] customToppings = request.getParameterValues("customToppings");
            pizzaList.add(new Pizza(customPizzaName, List.of(customToppings)));
            request.setAttribute("message", "Your custom pizza has been added to the menu!");
        }
        else
        {
            String pizza = request.getParameter("pizza");
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String email = request.getParameter("email");
            String[] selectedToppings = request.getParameterValues("toppings");
            String subject = "Your Pizza Order Confirmation";
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Dear ").append(name).append(",\n\n");
            emailContent.append("Thank you for your order!\n");
            emailContent.append("You ordered: ").append(pizza).append("\n");
            emailContent.append("Toppings: \n");
            for (String topping : selectedToppings)
            {
                emailContent.append("- ").append(topping).append("\n");
            }
            emailContent.append("\nYour delivery address: ").append(address).append("\n");
            emailContent.append("We will contact you at ").append(phone).append(" if needed.\n");
            emailContent.append("\nEnjoy your pizza!\nBest regards, Pizza Team");
            boolean emailSent = sendEmail(email, subject, emailContent.toString());
            if (emailSent)
            {
                request.setAttribute("message", "Order placed successfully! A confirmation email has been sent.");
            }
            else
            {
                request.setAttribute("errorMessage", "Order placed, but we couldn't send a confirmation email.");
            }
            request.setAttribute("pizza", pizza);
            request.setAttribute("selectedToppings", selectedToppings);
            request.setAttribute("name", name);
            request.setAttribute("phone", phone);
            request.setAttribute("address", address);
            request.setAttribute("email", email);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("orderSucc.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        request.setAttribute("pizzaList", pizzaList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
    }
    private boolean sendEmail(String recipientEmail, String subject, String content)
    {
        String host = "smtp.gmail.com";
        final String senderEmail = "your-email@gmail.com";
        final String senderPassword = "your-email-password";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(properties, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            return true;
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
