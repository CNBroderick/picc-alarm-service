package ape.alarm.entity.url;

import java.util.*;
import java.util.function.Supplier;

@Deprecated
public class AlarmUrlTreeParentIdBuilder implements Supplier<List<AlarmUrl>> {

    private final Collection<AlarmUrl> origin;
    private final List<AlarmUrl> rootLevel = new ArrayList<>();
    private final Map<Integer, AlarmUrl> map = new LinkedHashMap<>();

    public AlarmUrlTreeParentIdBuilder(Collection<AlarmUrl> origin) {
        this.origin = origin;
        if (origin == null) return;

        origin.forEach(a -> {
            if (a.getParentId() <= 0) {
                rootLevel.add(a);
            } else {
                map.put(a.getId(), a);
            }
        });

        for (AlarmUrl alarmUrl : origin) {
            int parentId = alarmUrl.getParentId();
            AlarmUrl parent = map.get(alarmUrl.getParentId());
            if (parent == null) {
                rootLevel.add(alarmUrl);
                continue;
            }
            parent.addChildren(parent);
        }
    }

    @Override
    public List<AlarmUrl> get() {
        return rootLevel;
    }
}
