package com.gudratli.kalemyazilimtask.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudratli.kalemyazilimtask.dto.*;
import com.gudratli.kalemyazilimtask.entity.User;
import com.gudratli.kalemyazilimtask.enums.RoleType;
import com.gudratli.kalemyazilimtask.exception.DuplicateException;
import com.gudratli.kalemyazilimtask.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Api("User controller")
public class UserController
{
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @ApiOperation(value = "Login", notes = "Login API and get access token.")
    public ResponseEntity<ResponseDTO<JWTResponseDTO>> login (@RequestBody
    @ApiParam(name = "Login Request DTO", value = "DTO for login.", required = true) LoginRequestDTO loginRequestDTO,
            HttpServletRequest request)
    {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword());
        authenticationManager.authenticate(token);

        User user = userService.getByUsername(loginRequestDTO.getUsername());

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String accessToken = JWT.create()
                .withSubject(loginRequestDTO.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getRole().name())
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(loginRequestDTO.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 31))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        JWTResponseDTO jwtResponseDTO = new JWTResponseDTO(accessToken, refreshToken);
        ResponseDTO<JWTResponseDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setObject(jwtResponseDTO);
        responseDTO.setSuccessMessage("Successfully token got.");

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/sign-up")
    @ApiOperation(value = "Sign up", notes = "Sign up and create new account.")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> register (@RequestBody
    @ApiParam(name = "User Create DTO", value = "DTO for creating new account.", required = true)
            UserCreateDTO userCreateDTO)
    {
        User user = modelMapper.map(userCreateDTO, User.class);
        ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<>();

        try
        {
            user.setRole(RoleType.ADMIN);
            user.setCreatedAt(new Date());
            user = userService.addUser(user);
            responseDTO.setSuccessMessage("Successfully registered.");
        } catch (DuplicateException e)
        {
            responseDTO.setErrorCode(304);
            responseDTO.setErrorMessage(e.getMessage());
        }

        responseDTO.setObject(modelMapper.map(user, UserResponseDTO.class));

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/token/refresh")
    @ApiOperation(value = "Refresh token", notes = "Send refresh token and get new access token.")
    public void refreshToken (HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String prefix = "Bearer ";
        if (authorizationHeader != null && authorizationHeader.startsWith(prefix))
        {
            try
            {
                String refreshToken = authorizationHeader.substring(prefix.length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userService.getByUsername(username);
                String accessToken = JWT.create()
                        .withSubject(username)
                        .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRole().name())
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception ex)
            {
                ex.printStackTrace();
                response.setHeader("error", ex.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), ex.getMessage());
            }
        } else
            throw new RuntimeException("Refresh token is missing.");
    }
}
