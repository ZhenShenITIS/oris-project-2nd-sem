package itis.service;

import itis.aop.ПодсчитатьВремяВыполненияМетода;
import itis.aop.ПодсчитатьКоличествоУспешногоНеУспешногоВыполненияМетода;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class ТестовыйСервис {

    private final Random random = new Random();

    @ПодсчитатьКоличествоУспешногоНеУспешногоВыполненияМетода
    @ПодсчитатьВремяВыполненияМетода
    public String успешныйМетод() {
        log.info("Выполняется успешный метод");
        try {
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Успех";
    }

    @ПодсчитатьКоличествоУспешногоНеУспешногоВыполненияМетода
    @ПодсчитатьВремяВыполненияМетода
    public String методСОшибкой() {
        log.info("Выполняется метод с ошибкой");
        try {
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (random.nextBoolean()) {
            throw new RuntimeException("Искусственная ошибка");
        }
        return "Успех";
    }

    @ПодсчитатьВремяВыполненияМетода
    public String методТолькоДляБенчмарка() {
        log.info("Выполняется метод только для бенчмарка");
        try {
            Thread.sleep(random.nextInt(200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Бенчмарк завершен";
    }
}