package com.prokopchuk;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/evening")
public class EveningServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = Optional.ofNullable(req.getParameter("name"))
                .or(() -> Optional.ofNullable((String) req.getSession().getAttribute("name")))
                .orElse("Buddy");

        req.getSession().setAttribute("name", name);
        resp.getWriter().printf("Good evening, %s", name);
    }
}
