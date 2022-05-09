package ape.alarm.entity.common;

import ape.master.entity.code.AppCode;
import ape.master.entity.code.GeneralVariable;

import java.util.Objects;
import java.util.Optional;

public interface HasAjaxApp<E extends HasAjaxApp<E>> {

    AppCode getAjaxApp();

    E setAjaxApp(AppCode ajaxApp);

    default String getAjaxAppId() {
        return Optional.ofNullable(getAjaxApp()).map(AppCode::getId).orElse(null);
    }

    default String getAjaxAppName() {
        return Optional.ofNullable(getAjaxApp()).map(AppCode::getName).orElse(null);
    }

    default String getAjaxAppDesc() {
        return Optional.ofNullable(getAjaxApp()).map(AppCode::getDesc).orElse(null);
    }

    default String getAjaxAppPortal() {
        return Optional.ofNullable(getAjaxApp()).map(AppCode::getPortal).orElse(null);
    }

    default String getAjaxAppFront() {
        return Optional.ofNullable(getAjaxApp()).map(AppCode::getFront).orElse(null);
    }

    default boolean isGeneralAjaxApp() {
        return Objects.equals(getAjaxAppId(), GeneralVariable.URL_APP);
    }
}
