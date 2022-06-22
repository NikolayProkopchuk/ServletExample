package com.prokopchuk;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/evening")
public class EveningServlet extends HttpServlet {
    private final Map<UUID, Map<String, Object>> customSessionMap = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID sessionUUID = Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals("customSessionId"))
                .findAny()
                .map(c -> UUID.fromString(c.getValue()))
                .orElseGet(UUID::randomUUID);

        String name = Optional.ofNullable(req.getParameter("name"))
                .or(() -> Optional.ofNullable(customSessionMap.get(sessionUUID)).map(map -> (String) map.get("name")))
                .orElse("Buddy");

        customSessionMap.put(sessionUUID, Map.of("name", name));

        resp.addCookie(new Cookie("customSessionId", sessionUUID.toString()));
        resp.getWriter().printf("Good evening, %s", name);
    }
}
