package ape.alarm.common.email.restored;

import ape.master.common.util.ApeAlarmIdFactory;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlarmRestoredPredicate implements Function<Collection<String>, Map<String, LocalDateTime>> {

    private final Duration duration;

    public AlarmRestoredPredicate(Duration duration) {
        this.duration = duration;
    }

    @Override
    public Map<String, LocalDateTime> apply(Collection<String> alarmIds) {
        Map<String, LocalDateTime> map = new LinkedHashMap<>();
        alarmIds.stream().map(AlarmId::new).collect(Collectors.groupingBy(AlarmId::getId, Collectors.mapping(Function.identity(), Collectors.toList())))
                .values().stream().map(this::check).forEach(map::putAll);
        return map;
    }

    private Map<String, LocalDateTime> check(List<AlarmId> alarmIds) {
        AlarmId[] as = alarmIds.stream().sorted().toArray(AlarmId[]::new);
        Map<String, LocalDateTime> map = new LinkedHashMap<>();
        Set<String> cache = new LinkedHashSet<>();
        for (int i = 0; i < as.length; i++) {
            cache.add(as[i].alarmId);
            LocalDateTime nextStartTime = i + 1 < as.length ? as[i + 1].startTime : LocalDateTime.now();
            if (as[i].endTime.plus(duration).isAfter(nextStartTime)) {
                continue;
            }
            for (String a : cache) {
                map.put(a, as[i].endTime.plus(duration));
            }
            cache.clear();
        }
        return map;
    }

    @Getter
    private static class AlarmId implements Comparable<AlarmId> {
        private final String alarmId;
        private final String id;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;

        public AlarmId(String alarmId) {
            this.alarmId = alarmId;
            this.id = ApeAlarmIdFactory.getId(alarmId);
            this.startTime = ApeAlarmIdFactory.getStartTime(alarmId);
            this.endTime = ApeAlarmIdFactory.getEndTime(alarmId);
        }

        @Override
        public int compareTo(@Nonnull AlarmId o) {
            return Comparator.comparing(AlarmId::getId).thenComparing(AlarmId::getStartTime).thenComparing(AlarmId::getEndTime).compare(this, o);
        }
    }
}
