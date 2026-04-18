package itis.controller;

import itis.dto.СтатистикаВремениВыполнения;
import itis.repository.ВремяВыполненияМетодаХранилище;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/benchmark")
@RequiredArgsConstructor
public class БенчмаркКонтроллер {

    private final ВремяВыполненияМетодаХранилище хранилище;

    @GetMapping("/{methodName}")
    public СтатистикаВремениВыполнения получитьСтатистикуВремениВыполнения(
            @PathVariable String methodName) {
        return хранилище.получитьСтатистикуВремениВыполнения(methodName);
    }

    @GetMapping("/percentile/{methodName}")
    public double рассчитатьПерсентиль(
            @PathVariable String methodName,
            @RequestParam int percentile) {
        return хранилище.рассчитатьПерсентиль(methodName, percentile);
    }

    @DeleteMapping("/{methodName}")
    public void очиститьСтатистикуМетода(@PathVariable String methodName) {
        хранилище.очиститьСтатистику(methodName);
    }

    @DeleteMapping
    public void очиститьВсюСтатистику() {
        хранилище.очиститьВсюСтатистику();
    }
}