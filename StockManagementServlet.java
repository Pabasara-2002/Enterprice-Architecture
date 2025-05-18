package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/stockManagement")
public class StockManagementServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM stock");

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h2>Stock List</h2>");
            out.println("<table border='1'><tr><th>Product Name</th><th>Quantity</th><th>Price</th><th>Actions</th></tr>");

            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("product_name") + "</td>");
                out.println("<td>" + rs.getInt("quantity") + "</td>");
                out.println("<td>" + rs.getDouble("price") + "</td>");
                out.println("<td><a href='updateStock?id=" + rs.getInt("id") + "'>Update</a> | " +
                            "<a href='deleteStock?id=" + rs.getInt("id") + "'>Delete</a></td></tr>");
            }

            out.println("</table>");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Database error.");
        }
    }
}
