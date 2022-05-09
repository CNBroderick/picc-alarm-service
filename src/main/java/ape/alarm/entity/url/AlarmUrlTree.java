package ape.alarm.entity.url;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AlarmUrlTree implements Supplier<Collection<AlarmUrl>> {

    private final Collection<AlarmUrl> alarmUrls;

    Map<AlarmUrlLevel, List<AlarmUrl>> levelMap;
    public AlarmUrlTree(Collection<AlarmUrl> alarmUrlCollection) {
        this.alarmUrls = alarmUrlCollection;
        this.levelMap = alarmUrls.stream().collect(Collectors.groupingBy(AlarmUrl::getLevel,
                LinkedHashMap::new, Collectors.mapping(Function.identity(), Collectors.toList())));
    }

    private boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    private Collection<AlarmUrl> buildRootLevel(Collection<AlarmUrl> roots, Collection<AlarmUrl> provinces) {
        AlarmUrl alarmUrl = roots.stream().findFirst().orElse(new AlarmUrl().setAlarm(false));
        roots.clear();
        roots.add(alarmUrl);
        alarmUrl.getChildren().addAll(provinces);
        return provinces;
    }

    private Collection<AlarmUrl> buildProvinceLevel(Collection<AlarmUrl> provinceLevel,
                                                    Collection<AlarmUrl> ajaxLevel,
                                                    Collection<AlarmUrl> urlLevel) {
        Map<String, AlarmUrl> alarmUrlMap = provinceLevel.stream().collect(
                Collectors.toMap(AlarmUrl::getComcodeId, Function.identity(), (a,b)->b, LinkedHashMap::new));

        for (AlarmUrl ajax : ajaxLevel) {
            AlarmUrl province = alarmUrlMap.get(ajax.getComcodeId());
            if(province == null) {
            }
        }
        return provinceLevel;
    }

    private  List<AlarmUrl> getLevel(AlarmUrlLevel level) {
        return levelMap.getOrDefault(level, new ArrayList<>());
    }

    @Override
    public Collection<AlarmUrl> get() {
        if (alarmUrls == null || alarmUrls.isEmpty()) return Collections.emptyList();
        List<AlarmUrl> matching = new ArrayList<>(alarmUrls);

        List<AlarmUrl> rootLevel = getLevel(AlarmUrlLevel.ROOT);
        matching.removeAll(rootLevel);

        Collection<AlarmUrl> provinceLevel = buildRootLevel(rootLevel, getLevel(AlarmUrlLevel.PROVINCE));
        matching.removeAll(provinceLevel);

        Collection<AlarmUrl> urlLevel = buildProvinceLevel(provinceLevel, getLevel(AlarmUrlLevel.AJAX_APP), getLevel(AlarmUrlLevel.URL));


        return rootLevel;
    }
}
