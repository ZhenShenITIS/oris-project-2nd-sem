package itis.controller;

import itis.dto.СтатистикаУспешногоНеУспешногоВызоваФункции;
import itis.repository.ПодсчитатьКоличествоУспешногоНеУспешногоВыполненияМетодаХранилище;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metrics/success")
@RequiredArgsConstructor
public class МетрикиУспешностиКонтроллер {

    private final ПодсчитатьКоличествоУспешногоНеУспешногоВыполненияМетодаХранилище хранилище;

    @GetMapping("/{methodName}")
    public СтатистикаУспешногоНеУспешногоВызоваФункции получитьСтатистикуМетода(
            @PathVariable String methodName) {
        return хранилище.получитьСтатистикуУспешногоНеУспешногоВызоваФункци(methodName);
    }

    @GetMapping
    public Map<String, СтатистикаУспешногоНеУспешногоВызоваФункции> получитьВсюСтатистику() {
        return хранилище.получитьВсюСтатистику();
    }
}