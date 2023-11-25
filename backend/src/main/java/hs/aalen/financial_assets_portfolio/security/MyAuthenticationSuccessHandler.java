package hs.aalen.financial_assets_portfolio.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

//
//@Component
//public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//    /*
//     * Authentication Failure handler is required for logins, overridden and fitted
//     * for ajax communication
//     */
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//        /* This is actually not an error, but an OK message. It is sent to avoid redirects. */
//        response.sendError(HttpServletResponse.SC_OK);
//    }
//}