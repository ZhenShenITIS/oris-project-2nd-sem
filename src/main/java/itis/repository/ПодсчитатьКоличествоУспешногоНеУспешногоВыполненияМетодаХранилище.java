package itis.repository;

import itis.dto.СтатистикаУспешногоНеУспешногоВызоваФункции;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ПодсчитатьКоличествоУспешногоНеУспешногоВыполненияМетодаХранилище {
    private final Map<String, ПараЗначений> функцияУспехОтображение = new ConcurrentHashMap<>();

    public void увеличитьУспех(String имяФункции) {
        ПараЗначений параЗначений = функцияУспехОтображение.getOrDefault(имяФункции, new ПараЗначений(0, 0));
        параЗначений.увеличитьВызовСУспехом();
        функцияУспехОтображение.put(имяФункции, параЗначений);
    }


    public void увеличитьОшибки(String имяФункции) {
        ПараЗначений параЗначений = функцияУспехОтображение.getOrDefault(имяФункции, new ПараЗначений(0, 0));
        параЗначений.увеличитьВызов();
        функцияУспехОтображение.put(имяФункции, параЗначений);
    }

    public СтатистикаУспешногоНеУспешногоВызоваФункции получитьСтатистикуУспешногоНеУспешногоВызоваФункци(String имяФункции) {
        ПараЗначений параЗначений = функцияУспехОтображение.getOrDefault(имяФункции, new ПараЗначений(0, 0));
        return СтатистикаУспешногоНеУспешногоВызоваФункции
                .builder()
                .всеВызовы(параЗначений.вызовы)
                .имяФункции(имяФункции)
                .успешныеВызовы(параЗначений.успешныеВызовы)
                .неУспешныеВызовы(параЗначений.вызовы - параЗначений.успешныеВызовы)
                .build();
    }

    public Map<String, СтатистикаУспешногоНеУспешногоВызоваФункции> получитьВсюСтатистику() {
        return функцияУспехОтображение.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> СтатистикаУспешногоНеУспешногоВызоваФункции.builder()
                                .имяФункции(entry.getKey())
                                .успешныеВызовы(entry.getValue().успешныеВызовы)
                                .неУспешныеВызовы(entry.getValue().вызовы - entry.getValue().успешныеВызовы)
                                .всеВызовы(entry.getValue().вызовы)
                                .build()
                ));
    }



    @Getter
    @Setter
    @AllArgsConstructor
    public static class ПараЗначений {
        private long вызовы;
        private long успешныеВызовы;

        public void увеличитьВызовСУспехом () {
            вызовы++;
            успешныеВызовы++;
        }

        public void увеличитьВызов() {
            вызовы++;
        }
    }
}
