package itis.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record СтатистикаВремениВыполнения(
        String имяМетода,
        int количествоВызовов,
        double среднееВремя,
        long минимальноеВремя,
        long максимальноеВремя,
        List<Long> временаВыполнения
) {
    public double среднееВремяВМиллисекундах() {
        return среднееВремя / 1_000_000.0;
    }

    public double минимальноеВремяВМиллисекундах() {
        return минимальноеВремя / 1_000_000.0;
    }

    public double максимальноеВремяВМиллисекундах() {
        return максимальноеВремя / 1_000_000.0;
    }
}