package com.marketsim.task.controller;

import com.marketsim.task.model.Authresponse;
import com.marketsim.task.model.request.AuthRequest;
import com.marketsim.task.service.MyAdminDetails;
import com.marketsim.task.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyAdminDetails myAdminDetails;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/auth")
    public Authresponse createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) throws Exception
    {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())


            );
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = myAdminDetails.loadUserByUsername(authenticationRequest.getUserName());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new Authresponse(jwt);
    }

}
