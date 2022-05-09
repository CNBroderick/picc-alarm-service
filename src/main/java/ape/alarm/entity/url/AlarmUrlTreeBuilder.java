package ape.alarm.entity.url;

import ape.master.entity.code.AppCode;
import ape.master.entity.code.ComCode;

import java.util.*;
import java.util.stream.Collectors;

@Deprecated
public class AlarmUrlTreeBuilder {

    private final Collection<AlarmUrl> sources;

    private final Map<String, AlarmUrl> provinceLevel = new LinkedHashMap<>();

    public AlarmUrlTreeBuilder(Collection<AlarmUrl> source) {
        if (source == null) {
            sources = Collections.emptyList();
            return;
        }
        this.sources = source;
        if (!sources.isEmpty()) buildProvinceLevel(sources);
    }

    public Collection<AlarmUrl> getSources() {
        return sources;
    }

    public Collection<AlarmUrl> getProvinceLevel() {
        return provinceLevel.values();
    }

    private void buildProvinceLevel(Collection<AlarmUrl> alarmUrls) {
        Map<String, List<AlarmUrl>> map = new LinkedHashMap<>();
        Map<String, ComCode> comCodeMap = ComCode.createMap(alarmUrls.stream().map(AlarmUrl::getComCode).collect(Collectors.toUnmodifiableList()));


        for (AlarmUrl alarmUrl : alarmUrls) {
            AlarmUrlLevel urlLevel = AlarmUrlLevel.parse(alarmUrl);
            String comcodeId = alarmUrl.getComcodeId();
            map.computeIfAbsent(comcodeId, k -> new ArrayList<>());
            if (urlLevel == AlarmUrlLevel.PROVINCE) {
                provinceLevel.put(comcodeId, alarmUrl);
            } else {
                map.get(comcodeId).add(alarmUrl);
            }
        }

        map.forEach((province, urls) -> buildUrlAppLevel(provinceLevel.computeIfAbsent(province, p ->
                new AlarmUrl().setComCode(comCodeMap.get(province))
        ), urls));
    }

    private void buildUrlAppLevel(AlarmUrl province, Collection<AlarmUrl> alarmUrls) {
        Map<String, List<AlarmUrl>> map = new LinkedHashMap<>();
        Map<String, AppCode> appCodeMap = new LinkedHashMap<>();
        Map<String, AlarmUrl> appLevelMap = new LinkedHashMap<>();

        for (AlarmUrl alarmUrl : alarmUrls) {
            AlarmUrlLevel urlLevel = AlarmUrlLevel.parse(alarmUrl);
            String urlAppId = alarmUrl.getUrlAppId();
            appCodeMap.computeIfAbsent(urlAppId, k -> alarmUrl.getUrlApp());
            map.computeIfAbsent(urlAppId, k -> new ArrayList<>());
            if (urlLevel == AlarmUrlLevel.URL_APP) {
                appLevelMap.put(urlAppId, alarmUrl);
                province.addChildren(alarmUrl);
            } else {
                map.get(urlAppId).add(alarmUrl);
            }
        }
        map.forEach((urlApp, urls) -> buildUrlLevel(appLevelMap.computeIfAbsent(urlApp, a ->
                province.createChild().setUrlApp(appCodeMap.get(urlApp))
        ), urls));
    }

    private void buildUrlLevel(AlarmUrl urlApp, Collection<AlarmUrl> alarmUrls) {
        Map<String, List<AlarmUrl>> map = new LinkedHashMap<>();
        Map<String, AlarmUrl> urlLevelMap = new LinkedHashMap<>();

        process(map, urlLevelMap, AlarmUrlLevel.URL, urlApp, alarmUrls);

        map.forEach((url, urls) -> buildAjaxLevel(urlLevelMap.computeIfAbsent(url, a ->
                urlApp.createChild().setUrl(url)
        ), urls));
    }

    private void buildAjaxLevel(AlarmUrl url, Collection<AlarmUrl> alarmUrls) {
        Map<String, List<AlarmUrl>> map = new LinkedHashMap<>();
        Map<String, AlarmUrl> ajaxAppLevelMap = new LinkedHashMap<>();
        process(map, ajaxAppLevelMap, AlarmUrlLevel.AJAX_APP, url, alarmUrls);
        map.forEach((ajaxUrl, urls) -> Optional.ofNullable(ajaxAppLevelMap.get(ajaxUrl))
                .stream().peek(url::addChildren).findFirst()
                .orElseGet(() -> url.createChild().setAjaxUrl(ajaxUrl))
                .getChildren().addAll(urls)
        );
    }

    private void process(Map<String, List<AlarmUrl>> map, Map<String, AlarmUrl> ajaxUrlLevelMap,
                         AlarmUrlLevel alarmUrlLevel, AlarmUrl parent, Collection<AlarmUrl> alarmUrls) {
        for (AlarmUrl alarmUrl : alarmUrls) {
            AlarmUrlLevel urlLevel = AlarmUrlLevel.parse(alarmUrl);
            String urlAppId = alarmUrl.getUrlAppId();
            map.computeIfAbsent(urlAppId, k -> new ArrayList<>());
            if (urlLevel == alarmUrlLevel) {
                ajaxUrlLevelMap.put(urlAppId, alarmUrl);
                parent.addChildren(alarmUrl);
            } else {
                map.get(urlAppId).add(alarmUrl);
            }
        }
    }
}
