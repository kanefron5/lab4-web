package com.kanefron5.lab4;

import com.google.gson.GsonBuilder;
import com.kanefron5.lab4.database.DotServiceImpl;
import com.kanefron5.lab4.database.Lab4DotsEntity;
import com.kanefron5.lab4.database.Lab4UsersEntity;
import com.kanefron5.lab4.database.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@CrossOrigin
@RequestMapping(value = "/api/*")
public class MainController {
    private final static String error_f = "{\"error\":\"%s\"}";
    private final static String response_f = "{\"response\":\"%s\"}";
    private UserServiceImpl userService;
    private DotServiceImpl dotService;

    @Autowired
    public MainController(UserServiceImpl userService, DotServiceImpl dotService) {
        this.userService = userService;
        this.dotService = dotService;
    }

    @PostMapping(value = "registration", produces = "application/json")
    public ResponseEntity<String> registration(@RequestParam(name = "passworduser", defaultValue = "") String passworduser,
                                               @RequestParam(name = "emailuser", defaultValue = "") String emailuser) {
        if (!passworduser.trim().isEmpty() && !emailuser.trim().isEmpty()) {
            try {
                userService.putUser(new Lab4UsersEntity(CodingUtils.encode(passworduser), emailuser));
            } catch (Exception e) {
                return new ResponseEntity<>(String.format(error_f, e.getMessage()), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(String.format(response_f, "Зарегистрирован успешно!"), HttpStatus.OK);

        }
        return new ResponseEntity<>(String.format(error_f, "Param can't be null!"), HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value = "login", produces = "application/json")
    public ResponseEntity<String> login(@RequestParam(name = "emailuser", defaultValue = "") String emailuser,
                                        @RequestParam(name = "passworduser", defaultValue = "") String passworduser) {

        if (!passworduser.trim().isEmpty() && !emailuser.trim().isEmpty()) {
            try {
                Lab4UsersEntity user = userService.findByEmailuser(emailuser);

                if (user == null) {
                    return new ResponseEntity<>(String.format(error_f, "User is not registered!"), HttpStatus.BAD_REQUEST);
                }
                if (user.getPassworduser().equals(CodingUtils.encode(passworduser))) {
                    return new ResponseEntity<>(String.format(response_f, CodingUtils.tokenGenerator(emailuser)), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(String.format(error_f, "Incorrect password"), HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(String.format(error_f, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        return new ResponseEntity<>(String.format(error_f, "Param can't be null!"), HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value = "checkToken", produces = "application/json")
    public ResponseEntity<String> checkToken(@RequestParam(name = "token") String token) {
        return new ResponseEntity<>(String.format(response_f, CodingUtils.getTokenOwner(token, userService)), HttpStatus.OK);
    }


    @GetMapping(value = "getUser", produces = "application/json")
    public ResponseEntity<String> getUserById(@RequestParam(name = "token") String token) {
        String tokenOwner = CodingUtils.getTokenOwner(token, userService);

        Lab4UsersEntity user;
        try {
            user = userService.findByEmailuser(tokenOwner);
        } catch (Exception e) {
            return new ResponseEntity<>(String.format(error_f, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        if (user != null)
            return new ResponseEntity<>(new GsonBuilder().setPrettyPrinting().create().toJson(user), HttpStatus.OK);

        return new ResponseEntity<>(String.format(error_f, "No user!"), HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value = "sendDot", produces = "application/json")
    public ResponseEntity<String> sendDot(@RequestParam(name = "token") String token,
                                          @RequestParam(name = "x") String x,
                                          @RequestParam(name = "y") String y,
                                          @RequestParam(name = "r") String r) {
        String tokenOwner = CodingUtils.getTokenOwner(token, userService);

        if (token == null || token.isEmpty() || Objects.equals(tokenOwner, "false")) {
            return new ResponseEntity<>(String.format(error_f, "Токен неверный!"), HttpStatus.UNAUTHORIZED);
        } else {
            if (x == null || y == null || r == null || x.isEmpty() || y.isEmpty() || r.isEmpty()) {
                return new ResponseEntity<>(String.format(error_f, "Значения не переданны или не верны!"), HttpStatus.BAD_REQUEST);
            } else {
                try {
                    Double x_d = Double.parseDouble(x.replace(",", "."));
                    Double y_d = Double.parseDouble(y.replace(",", "."));
                    double r_d = Double.parseDouble(r.replace(",", "."));

                    if (r_d < 0) {
                        return new ResponseEntity<>(String.format(error_f, "Значения не переданны или не верны!"), HttpStatus.BAD_REQUEST);
                    }
                    dotService.addDot(new Lab4DotsEntity(tokenOwner, x_d, y_d, r_d));
                    List<Lab4DotsEntity> dots = dotService.findAll(tokenOwner);
                    dots.sort(Comparator.comparingInt(Lab4DotsEntity::getId));

                    return new ResponseEntity<>(new GsonBuilder().setPrettyPrinting().create().toJson(dots), HttpStatus.OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(String.format(error_f, "Значения не переданны или не верны!"), HttpStatus.BAD_REQUEST);
                }
            }
        }

    }

    @GetMapping(value = "getDots", produces = "application/json")
    public ResponseEntity<String> sendDot(@RequestParam(name = "token") String token) {
        String tokenOwner = CodingUtils.getTokenOwner(token, userService);

        if (token == null || token.isEmpty() || Objects.equals(tokenOwner, "false")) {
            return new ResponseEntity<>(String.format(error_f, "Токен неверный!"), HttpStatus.UNAUTHORIZED);
        } else {
            List<Lab4DotsEntity> dots = dotService.findAll(tokenOwner);
            dots.sort(Comparator.comparingInt(Lab4DotsEntity::getId));
            return new ResponseEntity<>(new GsonBuilder().setPrettyPrinting().create().toJson(dots), HttpStatus.OK);

        }

    }

}
