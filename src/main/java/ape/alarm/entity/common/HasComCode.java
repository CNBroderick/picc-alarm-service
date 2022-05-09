package ape.alarm.entity.common;

import ape.master.entity.code.ComCode;
import ape.master.entity.code.GeneralVariable;

import java.util.Objects;
import java.util.Optional;

public interface HasComCode<E extends HasComCode<E>> {

    ComCode getComCode();

    E setComCode(ComCode comCode);

    default String getComCodeId() {
        return Optional.ofNullable(getComCode()).map(ComCode::getId).orElse(null);
    }

    default String getComCodeName() {
        return Optional.ofNullable(getComCode()).map(ComCode::getName).orElse(getComCodeId());
    }

    default String getComCodeDesc() {
        return Optional.ofNullable(getComCode()).map(ComCode::getDesc).orElse(null);
    }

    default boolean isNationalComcode() {
        return Objects.equals(getComCodeId(), GeneralVariable.COMCODE);
    }
}
