package ape.alarm.entity.po;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmWosInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AlarmWosInfoExample() {
        oredCriteria = new ArrayList<>();
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("`d_id` is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("`d_id` is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("`d_id` =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("`d_id` <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("`d_id` >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("`d_id` >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("`d_id` <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("`d_id` <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("`d_id` in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("`d_id` not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("`d_id` between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("`d_id` not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andWosIdIsNull() {
            addCriterion("`d_wos_id` is null");
            return (Criteria) this;
        }

        public Criteria andWosIdIsNotNull() {
            addCriterion("`d_wos_id` is not null");
            return (Criteria) this;
        }

        public Criteria andWosIdEqualTo(Integer value) {
            addCriterion("`d_wos_id` =", value, "wosId");
            return (Criteria) this;
        }

        public Criteria andWosIdNotEqualTo(Integer value) {
            addCriterion("`d_wos_id` <>", value, "wosId");
            return (Criteria) this;
        }

        public Criteria andWosIdGreaterThan(Integer value) {
            addCriterion("`d_wos_id` >", value, "wosId");
            return (Criteria) this;
        }

        public Criteria andWosIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("`d_wos_id` >=", value, "wosId");
            return (Criteria) this;
        }

        public Criteria andWosIdLessThan(Integer value) {
            addCriterion("`d_wos_id` <", value, "wosId");
            return (Criteria) this;
        }

        public Criteria andWosIdLessThanOrEqualTo(Integer value) {
            addCriterion("`d_wos_id` <=", value, "wosId");
            return (Criteria) this;
        }

        public Criteria andWosIdIn(List<Integer> values) {
            addCriterion("`d_wos_id` in", values, "wosId");
            return (Criteria) this;
        }

        public Criteria andWosIdNotIn(List<Integer> values) {
            addCriterion("`d_wos_id` not in", values, "wosId");
            return (Criteria) this;
        }

        public Criteria andWosIdBetween(Integer value1, Integer value2) {
            addCriterion("`d_wos_id` between", value1, value2, "wosId");
            return (Criteria) this;
        }

        public Criteria andWosIdNotBetween(Integer value1, Integer value2) {
            addCriterion("`d_wos_id` not between", value1, value2, "wosId");
            return (Criteria) this;
        }

        public Criteria andAlarmIdIsNull() {
            addCriterion("`d_alarm_id` is null");
            return (Criteria) this;
        }

        public Criteria andAlarmIdIsNotNull() {
            addCriterion("`d_alarm_id` is not null");
            return (Criteria) this;
        }

        public Criteria andAlarmIdEqualTo(Integer value) {
            addCriterion("`d_alarm_id` =", value, "alarmId");
            return (Criteria) this;
        }

        public Criteria andAlarmIdNotEqualTo(Integer value) {
            addCriterion("`d_alarm_id` <>", value, "alarmId");
            return (Criteria) this;
        }

        public Criteria andAlarmIdGreaterThan(Integer value) {
            addCriterion("`d_alarm_id` >", value, "alarmId");
            return (Criteria) this;
        }

        public Criteria andAlarmIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("`d_alarm_id` >=", value, "alarmId");
            return (Criteria) this;
        }

        public Criteria andAlarmIdLessThan(Integer value) {
            addCriterion("`d_alarm_id` <", value, "alarmId");
            return (Criteria) this;
        }

        public Criteria andAlarmIdLessThanOrEqualTo(Integer value) {
            addCriterion("`d_alarm_id` <=", value, "alarmId");
            return (Criteria) this;
        }

        public Criteria andAlarmIdIn(List<Integer> values) {
            addCriterion("`d_alarm_id` in", values, "alarmId");
            return (Criteria) this;
        }

        public Criteria andAlarmIdNotIn(List<Integer> values) {
            addCriterion("`d_alarm_id` not in", values, "alarmId");
            return (Criteria) this;
        }

        public Criteria andAlarmIdBetween(Integer value1, Integer value2) {
            addCriterion("`d_alarm_id` between", value1, value2, "alarmId");
            return (Criteria) this;
        }

        public Criteria andAlarmIdNotBetween(Integer value1, Integer value2) {
            addCriterion("`d_alarm_id` not between", value1, value2, "alarmId");
            return (Criteria) this;
        }

        public Criteria andAffectSystemIsNull() {
            addCriterion("`d_affect_system` is null");
            return (Criteria) this;
        }

        public Criteria andAffectSystemIsNotNull() {
            addCriterion("`d_affect_system` is not null");
            return (Criteria) this;
        }

        public Criteria andAffectSystemEqualTo(String value) {
            addCriterion("`d_affect_system` =", value, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemNotEqualTo(String value) {
            addCriterion("`d_affect_system` <>", value, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemGreaterThan(String value) {
            addCriterion("`d_affect_system` >", value, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemGreaterThanOrEqualTo(String value) {
            addCriterion("`d_affect_system` >=", value, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemLessThan(String value) {
            addCriterion("`d_affect_system` <", value, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemLessThanOrEqualTo(String value) {
            addCriterion("`d_affect_system` <=", value, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemLike(String value) {
            addCriterion("`d_affect_system` like", value, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemNotLike(String value) {
            addCriterion("`d_affect_system` not like", value, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemIn(List<String> values) {
            addCriterion("`d_affect_system` in", values, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemNotIn(List<String> values) {
            addCriterion("`d_affect_system` not in", values, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemBetween(String value1, String value2) {
            addCriterion("`d_affect_system` between", value1, value2, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andAffectSystemNotBetween(String value1, String value2) {
            addCriterion("`d_affect_system` not between", value1, value2, "affectSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemIsNull() {
            addCriterion("`d_error_system` is null");
            return (Criteria) this;
        }

        public Criteria andErrorSystemIsNotNull() {
            addCriterion("`d_error_system` is not null");
            return (Criteria) this;
        }

        public Criteria andErrorSystemEqualTo(String value) {
            addCriterion("`d_error_system` =", value, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemNotEqualTo(String value) {
            addCriterion("`d_error_system` <>", value, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemGreaterThan(String value) {
            addCriterion("`d_error_system` >", value, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemGreaterThanOrEqualTo(String value) {
            addCriterion("`d_error_system` >=", value, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemLessThan(String value) {
            addCriterion("`d_error_system` <", value, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemLessThanOrEqualTo(String value) {
            addCriterion("`d_error_system` <=", value, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemLike(String value) {
            addCriterion("`d_error_system` like", value, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemNotLike(String value) {
            addCriterion("`d_error_system` not like", value, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemIn(List<String> values) {
            addCriterion("`d_error_system` in", values, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemNotIn(List<String> values) {
            addCriterion("`d_error_system` not in", values, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemBetween(String value1, String value2) {
            addCriterion("`d_error_system` between", value1, value2, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andErrorSystemNotBetween(String value1, String value2) {
            addCriterion("`d_error_system` not between", value1, value2, "errorSystem");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeIsNull() {
            addCriterion("`d_place_time` is null");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeIsNotNull() {
            addCriterion("`d_place_time` is not null");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeEqualTo(LocalDateTime value) {
            addCriterion("`d_place_time` =", value, "placeTime");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeNotEqualTo(LocalDateTime value) {
            addCriterion("`d_place_time` <>", value, "placeTime");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeGreaterThan(LocalDateTime value) {
            addCriterion("`d_place_time` >", value, "placeTime");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("`d_place_time` >=", value, "placeTime");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeLessThan(LocalDateTime value) {
            addCriterion("`d_place_time` <", value, "placeTime");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("`d_place_time` <=", value, "placeTime");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeIn(List<LocalDateTime> values) {
            addCriterion("`d_place_time` in", values, "placeTime");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeNotIn(List<LocalDateTime> values) {
            addCriterion("`d_place_time` not in", values, "placeTime");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`d_place_time` between", value1, value2, "placeTime");
            return (Criteria) this;
        }

        public Criteria andPlaceTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`d_place_time` not between", value1, value2, "placeTime");
            return (Criteria) this;
        }

        public Criteria andAffectRateIsNull() {
            addCriterion("`d_affect_rate` is null");
            return (Criteria) this;
        }

        public Criteria andAffectRateIsNotNull() {
            addCriterion("`d_affect_rate` is not null");
            return (Criteria) this;
        }

        public Criteria andAffectRateEqualTo(BigDecimal value) {
            addCriterion("`d_affect_rate` =", value, "affectRate");
            return (Criteria) this;
        }

        public Criteria andAffectRateNotEqualTo(BigDecimal value) {
            addCriterion("`d_affect_rate` <>", value, "affectRate");
            return (Criteria) this;
        }

        public Criteria andAffectRateGreaterThan(BigDecimal value) {
            addCriterion("`d_affect_rate` >", value, "affectRate");
            return (Criteria) this;
        }

        public Criteria andAffectRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`d_affect_rate` >=", value, "affectRate");
            return (Criteria) this;
        }

        public Criteria andAffectRateLessThan(BigDecimal value) {
            addCriterion("`d_affect_rate` <", value, "affectRate");
            return (Criteria) this;
        }

        public Criteria andAffectRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`d_affect_rate` <=", value, "affectRate");
            return (Criteria) this;
        }

        public Criteria andAffectRateIn(List<BigDecimal> values) {
            addCriterion("`d_affect_rate` in", values, "affectRate");
            return (Criteria) this;
        }

        public Criteria andAffectRateNotIn(List<BigDecimal> values) {
            addCriterion("`d_affect_rate` not in", values, "affectRate");
            return (Criteria) this;
        }

        public Criteria andAffectRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`d_affect_rate` between", value1, value2, "affectRate");
            return (Criteria) this;
        }

        public Criteria andAffectRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`d_affect_rate` not between", value1, value2, "affectRate");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeIsNull() {
            addCriterion("`d_affect_amplitude` is null");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeIsNotNull() {
            addCriterion("`d_affect_amplitude` is not null");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeEqualTo(BigDecimal value) {
            addCriterion("`d_affect_amplitude` =", value, "affectAmplitude");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeNotEqualTo(BigDecimal value) {
            addCriterion("`d_affect_amplitude` <>", value, "affectAmplitude");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeGreaterThan(BigDecimal value) {
            addCriterion("`d_affect_amplitude` >", value, "affectAmplitude");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`d_affect_amplitude` >=", value, "affectAmplitude");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeLessThan(BigDecimal value) {
            addCriterion("`d_affect_amplitude` <", value, "affectAmplitude");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`d_affect_amplitude` <=", value, "affectAmplitude");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeIn(List<BigDecimal> values) {
            addCriterion("`d_affect_amplitude` in", values, "affectAmplitude");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeNotIn(List<BigDecimal> values) {
            addCriterion("`d_affect_amplitude` not in", values, "affectAmplitude");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`d_affect_amplitude` between", value1, value2, "affectAmplitude");
            return (Criteria) this;
        }

        public Criteria andAffectAmplitudeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`d_affect_amplitude` not between", value1, value2, "affectAmplitude");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("`d_title` is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("`d_title` is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("`d_title` =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("`d_title` <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("`d_title` >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("`d_title` >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("`d_title` <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("`d_title` <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("`d_title` like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("`d_title` not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("`d_title` in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("`d_title` not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("`d_title` between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("`d_title` not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("`d_description` is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("`d_description` is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("`d_description` =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("`d_description` <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("`d_description` >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("`d_description` >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("`d_description` <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("`d_description` <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("`d_description` like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("`d_description` not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("`d_description` in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("`d_description` not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("`d_description` between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("`d_description` not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andAffectTypeIsNull() {
            addCriterion("`d_affect_type` is null");
            return (Criteria) this;
        }

        public Criteria andAffectTypeIsNotNull() {
            addCriterion("`d_affect_type` is not null");
            return (Criteria) this;
        }

        public Criteria andAffectTypeEqualTo(String value) {
            addCriterion("`d_affect_type` =", value, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeNotEqualTo(String value) {
            addCriterion("`d_affect_type` <>", value, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeGreaterThan(String value) {
            addCriterion("`d_affect_type` >", value, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeGreaterThanOrEqualTo(String value) {
            addCriterion("`d_affect_type` >=", value, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeLessThan(String value) {
            addCriterion("`d_affect_type` <", value, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeLessThanOrEqualTo(String value) {
            addCriterion("`d_affect_type` <=", value, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeLike(String value) {
            addCriterion("`d_affect_type` like", value, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeNotLike(String value) {
            addCriterion("`d_affect_type` not like", value, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeIn(List<String> values) {
            addCriterion("`d_affect_type` in", values, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeNotIn(List<String> values) {
            addCriterion("`d_affect_type` not in", values, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeBetween(String value1, String value2) {
            addCriterion("`d_affect_type` between", value1, value2, "affectType");
            return (Criteria) this;
        }

        public Criteria andAffectTypeNotBetween(String value1, String value2) {
            addCriterion("`d_affect_type` not between", value1, value2, "affectType");
            return (Criteria) this;
        }

        public Criteria andIpAddressIsNull() {
            addCriterion("`d_ip_address` is null");
            return (Criteria) this;
        }

        public Criteria andIpAddressIsNotNull() {
            addCriterion("`d_ip_address` is not null");
            return (Criteria) this;
        }

        public Criteria andIpAddressEqualTo(String value) {
            addCriterion("`d_ip_address` =", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressNotEqualTo(String value) {
            addCriterion("`d_ip_address` <>", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressGreaterThan(String value) {
            addCriterion("`d_ip_address` >", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressGreaterThanOrEqualTo(String value) {
            addCriterion("`d_ip_address` >=", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressLessThan(String value) {
            addCriterion("`d_ip_address` <", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressLessThanOrEqualTo(String value) {
            addCriterion("`d_ip_address` <=", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressLike(String value) {
            addCriterion("`d_ip_address` like", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressNotLike(String value) {
            addCriterion("`d_ip_address` not like", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressIn(List<String> values) {
            addCriterion("`d_ip_address` in", values, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressNotIn(List<String> values) {
            addCriterion("`d_ip_address` not in", values, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressBetween(String value1, String value2) {
            addCriterion("`d_ip_address` between", value1, value2, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressNotBetween(String value1, String value2) {
            addCriterion("`d_ip_address` not between", value1, value2, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andAffectAreaIsNull() {
            addCriterion("`d_affect_area` is null");
            return (Criteria) this;
        }

        public Criteria andAffectAreaIsNotNull() {
            addCriterion("`d_affect_area` is not null");
            return (Criteria) this;
        }

        public Criteria andAffectAreaEqualTo(String value) {
            addCriterion("`d_affect_area` =", value, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaNotEqualTo(String value) {
            addCriterion("`d_affect_area` <>", value, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaGreaterThan(String value) {
            addCriterion("`d_affect_area` >", value, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaGreaterThanOrEqualTo(String value) {
            addCriterion("`d_affect_area` >=", value, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaLessThan(String value) {
            addCriterion("`d_affect_area` <", value, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaLessThanOrEqualTo(String value) {
            addCriterion("`d_affect_area` <=", value, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaLike(String value) {
            addCriterion("`d_affect_area` like", value, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaNotLike(String value) {
            addCriterion("`d_affect_area` not like", value, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaIn(List<String> values) {
            addCriterion("`d_affect_area` in", values, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaNotIn(List<String> values) {
            addCriterion("`d_affect_area` not in", values, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaBetween(String value1, String value2) {
            addCriterion("`d_affect_area` between", value1, value2, "affectArea");
            return (Criteria) this;
        }

        public Criteria andAffectAreaNotBetween(String value1, String value2) {
            addCriterion("`d_affect_area` not between", value1, value2, "affectArea");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeIsNull() {
            addCriterion("`d_recovery_time` is null");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeIsNotNull() {
            addCriterion("`d_recovery_time` is not null");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeEqualTo(LocalDateTime value) {
            addCriterion("`d_recovery_time` =", value, "recoveryTime");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeNotEqualTo(LocalDateTime value) {
            addCriterion("`d_recovery_time` <>", value, "recoveryTime");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeGreaterThan(LocalDateTime value) {
            addCriterion("`d_recovery_time` >", value, "recoveryTime");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("`d_recovery_time` >=", value, "recoveryTime");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeLessThan(LocalDateTime value) {
            addCriterion("`d_recovery_time` <", value, "recoveryTime");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("`d_recovery_time` <=", value, "recoveryTime");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeIn(List<LocalDateTime> values) {
            addCriterion("`d_recovery_time` in", values, "recoveryTime");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeNotIn(List<LocalDateTime> values) {
            addCriterion("`d_recovery_time` not in", values, "recoveryTime");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`d_recovery_time` between", value1, value2, "recoveryTime");
            return (Criteria) this;
        }

        public Criteria andRecoveryTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`d_recovery_time` not between", value1, value2, "recoveryTime");
            return (Criteria) this;
        }

        public Criteria andFaultDurationIsNull() {
            addCriterion("`d_fault_duration` is null");
            return (Criteria) this;
        }

        public Criteria andFaultDurationIsNotNull() {
            addCriterion("`d_fault_duration` is not null");
            return (Criteria) this;
        }

        public Criteria andFaultDurationEqualTo(String value) {
            addCriterion("`d_fault_duration` =", value, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationNotEqualTo(String value) {
            addCriterion("`d_fault_duration` <>", value, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationGreaterThan(String value) {
            addCriterion("`d_fault_duration` >", value, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationGreaterThanOrEqualTo(String value) {
            addCriterion("`d_fault_duration` >=", value, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationLessThan(String value) {
            addCriterion("`d_fault_duration` <", value, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationLessThanOrEqualTo(String value) {
            addCriterion("`d_fault_duration` <=", value, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationLike(String value) {
            addCriterion("`d_fault_duration` like", value, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationNotLike(String value) {
            addCriterion("`d_fault_duration` not like", value, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationIn(List<String> values) {
            addCriterion("`d_fault_duration` in", values, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationNotIn(List<String> values) {
            addCriterion("`d_fault_duration` not in", values, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationBetween(String value1, String value2) {
            addCriterion("`d_fault_duration` between", value1, value2, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andFaultDurationNotBetween(String value1, String value2) {
            addCriterion("`d_fault_duration` not between", value1, value2, "faultDuration");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionIsNull() {
            addCriterion("`d_impact_function` is null");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionIsNotNull() {
            addCriterion("`d_impact_function` is not null");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionEqualTo(String value) {
            addCriterion("`d_impact_function` =", value, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionNotEqualTo(String value) {
            addCriterion("`d_impact_function` <>", value, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionGreaterThan(String value) {
            addCriterion("`d_impact_function` >", value, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionGreaterThanOrEqualTo(String value) {
            addCriterion("`d_impact_function` >=", value, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionLessThan(String value) {
            addCriterion("`d_impact_function` <", value, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionLessThanOrEqualTo(String value) {
            addCriterion("`d_impact_function` <=", value, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionLike(String value) {
            addCriterion("`d_impact_function` like", value, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionNotLike(String value) {
            addCriterion("`d_impact_function` not like", value, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionIn(List<String> values) {
            addCriterion("`d_impact_function` in", values, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionNotIn(List<String> values) {
            addCriterion("`d_impact_function` not in", values, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionBetween(String value1, String value2) {
            addCriterion("`d_impact_function` between", value1, value2, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andImpactFunctionNotBetween(String value1, String value2) {
            addCriterion("`d_impact_function` not between", value1, value2, "impactFunction");
            return (Criteria) this;
        }

        public Criteria andSendTimeIsNull() {
            addCriterion("`d_send_time` is null");
            return (Criteria) this;
        }

        public Criteria andSendTimeIsNotNull() {
            addCriterion("`d_send_time` is not null");
            return (Criteria) this;
        }

        public Criteria andSendTimeEqualTo(LocalDateTime value) {
            addCriterion("`d_send_time` =", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotEqualTo(LocalDateTime value) {
            addCriterion("`d_send_time` <>", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeGreaterThan(LocalDateTime value) {
            addCriterion("`d_send_time` >", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("`d_send_time` >=", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeLessThan(LocalDateTime value) {
            addCriterion("`d_send_time` <", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("`d_send_time` <=", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeIn(List<LocalDateTime> values) {
            addCriterion("`d_send_time` in", values, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotIn(List<LocalDateTime> values) {
            addCriterion("`d_send_time` not in", values, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`d_send_time` between", value1, value2, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`d_send_time` not between", value1, value2, "sendTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private final String condition;
        private final String typeHandler;
        private Object value;
        private Object secondValue;
        private boolean noValue;
        private boolean singleValue;
        private boolean betweenValue;
        private boolean listValue;

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }
    }
}
