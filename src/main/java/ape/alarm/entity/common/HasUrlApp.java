package ape.alarm.entity.common;

import ape.master.entity.code.AppCode;
import ape.master.entity.code.GeneralVariable;

import java.util.Objects;
import java.util.Optional;

public interface HasUrlApp<E extends HasUrlApp<E>> {

    AppCode getUrlApp();

    E setUrlApp(AppCode appCode);

    default String getUrlAppId() {
        return Optional.ofNullable(getUrlApp()).map(AppCode::getId).orElse(null);
    }

    default String getUrlAppName() {
        return Optional.ofNullable(getUrlApp()).map(AppCode::getName).orElse(null);
    }

    default String getUrlAppPortal() {
        return Optional.ofNullable(getUrlApp()).map(AppCode::getPortal).orElse(null);
    }

    default String getUrlAppFront() {
        return Optional.ofNullable(getUrlApp()).map(AppCode::getFront).orElse(null);
    }

    default String getUrlAppDesc() {
        return Optional.ofNullable(getUrlApp()).map(AppCode::getName).orElse(null);
    }

    default boolean isGeneralUrlApp() {
        return Objects.equals(getUrlAppId(), GeneralVariable.URL_APP);
    }
}
