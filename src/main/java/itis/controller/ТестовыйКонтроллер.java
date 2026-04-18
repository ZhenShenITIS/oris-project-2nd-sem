package itis.controller;

import itis.service.ТестовыйСервис;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class ТестовыйКонтроллер {

    private final ТестовыйСервис тестовыйСервис;

    @GetMapping("/success")
    public String тестУспешногоМетода() {
        return тестовыйСервис.успешныйМетод();
    }

    @GetMapping("/error")
    public String тестМетодаСОшибкой() {
        try {
            return тестовыйСервис.методСОшибкой();
        } catch (RuntimeException e) {
            return "Произошла ошибка: " + e.getMessage();
        }
    }

    @GetMapping("/benchmark")
    public String тестБенчмарка() {
        return тестовыйСервис.методТолькоДляБенчмарка();
    }

    @GetMapping("/multiple")
    public String тестНесколькихВызовов() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            try {
                result.append("Вызов ").append(i + 1).append(": ");
                if (i % 3 == 0) {
                    тестовыйСервис.методСОшибкой();
                    result.append("методСОшибкой - успех\n");
                } else {
                    тестовыйСервис.успешныйМетод();
                    result.append("успешныйМетод\n");
                }
            } catch (RuntimeException e) {
                result.append("методСОшибкой - ошибка: ").append(e.getMessage()).append("\n");
            }
        }
        return result.toString();
    }
}