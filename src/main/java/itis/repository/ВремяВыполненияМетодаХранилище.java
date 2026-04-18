package itis.repository;

import itis.dto.СтатистикаВремениВыполнения;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@Slf4j
public class ВремяВыполненияМетодаХранилище {
    private final Map<String, List<Long>> времяВыполненияМетода = new ConcurrentHashMap<>();

    public void добавитьВремяВыполнения(String имяМетода, long времяВНаносекундах) {
        времяВыполненияМетода
                .computeIfAbsent(имяМетода, k -> new CopyOnWriteArrayList<>())
                .add(времяВНаносекундах);
    }

    public СтатистикаВремениВыполнения получитьСтатистикуВремениВыполнения(String имяМетода) {
        List<Long> временаВыполнения = времяВыполненияМетода.getOrDefault(имяМетода, new ArrayList<>());

        if (временаВыполнения.isEmpty()) {
            return СтатистикаВремениВыполнения.builder()
                    .имяМетода(имяМетода)
                    .количествоВызовов(0)
                    .среднееВремя(0)
                    .минимальноеВремя(0)
                    .максимальноеВремя(0)
                    .build();
        }

        long сумма = 0;
        long минимум = Long.MAX_VALUE;
        long максимум = Long.MIN_VALUE;

        for (Long время : временаВыполнения) {
            сумма += время;
            if (время < минимум) минимум = время;
            if (время > максимум) максимум = время;
        }

        double среднее = (double) сумма / временаВыполнения.size();

        return СтатистикаВремениВыполнения.builder()
                .имяМетода(имяМетода)
                .количествоВызовов(временаВыполнения.size())
                .среднееВремя(среднее)
                .минимальноеВремя(минимум)
                .максимальноеВремя(максимум)
                .временаВыполнения(new ArrayList<>(временаВыполнения))
                .build();
    }

    public double рассчитатьПерсентиль(String имяМетода, int персентиль) {
        List<Long> временаВыполнения = времяВыполненияМетода.getOrDefault(имяМетода, new ArrayList<>());

        if (временаВыполнения.isEmpty()) {
            return 0;
        }

        if (персентиль < 0 || персентиль > 100) {
            throw new IllegalArgumentException("Персентиль должен быть в диапазоне от 0 до 100");
        }

        List<Long> отсортированныеВремена = new ArrayList<>(временаВыполнения);
        отсортированныеВремена.sort(Long::compareTo);


        int индекс = (int) Math.round((персентиль / 100.0) * (отсортированныеВремена.size() - 1));

        индекс = Math.min(индекс, отсортированныеВремена.size() - 1);
        индекс = Math.max(индекс, 0);

        return отсортированныеВремена.get(индекс);
    }

    public void очиститьСтатистику(String имяМетода) {
        времяВыполненияМетода.remove(имяМетода);
    }

    public void очиститьВсюСтатистику() {
        времяВыполненияМетода.clear();
    }
}