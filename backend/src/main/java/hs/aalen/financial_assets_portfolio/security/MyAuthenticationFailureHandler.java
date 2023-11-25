package hs.aalen.financial_assets_portfolio.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//@Component
//public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//    /*
//     * Authentication Failure handler is required for logins, overridden and fitted
//     * for ajax communication
//     */
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public void onAuthenticationFailure(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            AuthenticationException exception)
//            throws IOException, ServletException {
//
//        /* response String if authentication fails */
//
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        Map<String, Object> data = new HashMap<>();
//        data.put(
//                "timestamp",
//                Calendar.getInstance().getTime());
//        data.put(
//                "exception", exception.getMessage());
//
//        response.getOutputStream()
//                .println(objectMapper.writeValueAsString(data));
//
//    }
//}
