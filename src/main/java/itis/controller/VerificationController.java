
package itis.controller;

import itis.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class VerificationController {

    private final UserService userService;

    @GetMapping("/verification")
    public String verifyAccount(@RequestParam(name = "code") String code, Model model) {
        log.info("Получен код верификации: {}", code);
        boolean isVerified = userService.verifyUser(code);
        log.info("Результат верификации: {}", isVerified);
        model.addAttribute("success", isVerified);
        return "verification_result";
    }
}