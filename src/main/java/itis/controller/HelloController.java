package itis.controller;

import itis.dto.UserDto;
import itis.model.User;
import itis.service.HelloService;
import itis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HelloController {
    private final UserService userService;
    private final HelloService helloService;

    @GetMapping("/hello")
    public String hello(@RequestParam(required = false, name = "name") String name) {
        return helloService.sayHello(name);
    }
    

}
