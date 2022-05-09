package ape.alarm.operation.jdbc.mapper;

import ape.master.entity.code.AppCode;
import ape.master.entity.code.ComCode;
import ape.master.entity.code.GeneralVariable;
import ape.master.operation.master.ApeMasterOperationEnum;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class AbstractAlarmMapper<T> implements IEntityRowMapper<T> {
    private LinkedHashMap<String, ComCode> comCodeMap;
    private LinkedHashMap<String, AppCode> urlAppMap;
    private LinkedHashMap<String, AppCode> ajaxAppMap;
    private LinkedHashMap<String, List<AppCode>> appCodeMap;

    public AbstractAlarmMapper() {
    }

    @Override
    public abstract T mapRow(ResultSet resultSet) throws Exception;

    public ComCode getComCode(ResultSet r) throws SQLException {
        return getComCode(r.getString("d_comcode"));
    }

    public AppCode getUrlApp(ResultSet r) throws SQLException {
        return getUrlApp(r.getString("d_url_app"));
    }

    public AppCode getAjaxApp(ResultSet r) throws SQLException {
        return getAjaxApp(r.getString("d_ajax_app"));
    }

    public LocalDateTime getUpdateTime(ResultSet r) throws Exception {
        return getLocalDateTime(r, "d_update_time");
    }

    public ComCode getComCode(String comcodeId) {
        return getComCodeMap().getOrDefault(comcodeId, new ComCode().setId(comcodeId)
                .setName(comcodeId).setRules(comcodeId).setCloud("其他"));
    }

    public AppCode getUrlApp(String urlAppCode) {
        return getUrlAppMap().getOrDefault(urlAppCode, new AppCode(urlAppCode));
    }

    public AppCode getAjaxApp(String ajaxAppCode) {
        return getAjaxAppMap().getOrDefault(ajaxAppCode, new AppCode(ajaxAppCode));
    }

    public LinkedHashMap<String, ComCode> getComCodeMap() {
        if (comCodeMap == null) {
            comCodeMap = ComCode.createMap(ApeMasterOperationEnum.ComCodeQuery.createOperation().execute().asList());
            comCodeMap.put(GeneralVariable.COMCODE, ComCode.national());
            comCodeMap.put(null, null);
        }
        return comCodeMap;
    }

    public LinkedHashMap<String, AppCode> getUrlAppMap() {
        if (urlAppMap == null) loadAppMap();
        return urlAppMap;
    }

    public LinkedHashMap<String, AppCode> getAjaxAppMap() {
        if (ajaxAppMap == null) loadAppMap();
        return ajaxAppMap;
    }

    private void loadAppMap() {
        if (appCodeMap == null) appCodeMap = AppCode.createTypeMap(ApeMasterOperationEnum.AppCodeQuery.createOperation().execute().asList());
        urlAppMap = AppCode.createIdMap(appCodeMap.getOrDefault("前台应用", Collections.emptyList()));
        urlAppMap.put(GeneralVariable.URL_APP, AppCode.generalUrlApp());
        ajaxAppMap = AppCode.createIdMap(appCodeMap.getOrDefault("前台微服务", Collections.emptyList()));
        ajaxAppMap.put(GeneralVariable.AJAX_APP, AppCode.generalAjaxApp());
        urlAppMap.put(null, null);
        ajaxAppMap.put(null, null);
    }

    public List<AppCode> getAppCodes() {
        if (appCodeMap == null) loadAppMap();
        List<AppCode> list = new ArrayList<>();
        appCodeMap.values().forEach(list::addAll);
        return list;
    }
}
