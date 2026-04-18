package itis.dto;

import lombok.Builder;

@Builder
public record СтатистикаУспешногоНеУспешногоВызоваФункции (
        String имяФункции,
        long успешныеВызовы,
        long неУспешныеВызовы,
        long всеВызовы
) {

}
