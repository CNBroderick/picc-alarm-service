package ape.alarm.entity.po;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class AlarmBmacDataExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AlarmBmacDataExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
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
        protected List<Criterion> requestCriteria;

        protected List<Criterion> responseCriteria;

        protected List<Criterion> restoreRequestCriteria;

        protected List<Criterion> restoreResponseCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
            requestCriteria = new ArrayList<>();
            responseCriteria = new ArrayList<>();
            restoreRequestCriteria = new ArrayList<>();
            restoreResponseCriteria = new ArrayList<>();
        }

        public List<Criterion> getRequestCriteria() {
            return requestCriteria;
        }

        protected void addRequestCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            requestCriteria.add(new Criterion(condition, value, "ape.alarm.entity.typehandler.JsonObjectTypeHandler"));
            allCriteria = null;
        }

        protected void addRequestCriterion(String condition, JsonObject value1, JsonObject value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            requestCriteria.add(new Criterion(condition, value1, value2, "ape.alarm.entity.typehandler.JsonObjectTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getResponseCriteria() {
            return responseCriteria;
        }

        protected void addResponseCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            responseCriteria.add(new Criterion(condition, value, "ape.alarm.entity.typehandler.JsonObjectTypeHandler"));
            allCriteria = null;
        }

        protected void addResponseCriterion(String condition, JsonObject value1, JsonObject value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            responseCriteria.add(new Criterion(condition, value1, value2, "ape.alarm.entity.typehandler.JsonObjectTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getRestoreRequestCriteria() {
            return restoreRequestCriteria;
        }

        protected void addRestoreRequestCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            restoreRequestCriteria.add(new Criterion(condition, value, "ape.alarm.entity.typehandler.JsonObjectTypeHandler"));
            allCriteria = null;
        }

        protected void addRestoreRequestCriterion(String condition, JsonObject value1, JsonObject value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            restoreRequestCriteria.add(new Criterion(condition, value1, value2, "ape.alarm.entity.typehandler.JsonObjectTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getRestoreResponseCriteria() {
            return restoreResponseCriteria;
        }

        protected void addRestoreResponseCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            restoreResponseCriteria.add(new Criterion(condition, value, "ape.alarm.entity.typehandler.JsonObjectTypeHandler"));
            allCriteria = null;
        }

        protected void addRestoreResponseCriterion(String condition, JsonObject value1, JsonObject value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            restoreResponseCriteria.add(new Criterion(condition, value1, value2, "ape.alarm.entity.typehandler.JsonObjectTypeHandler"));
            allCriteria = null;
        }

        public boolean isValid() {
            return criteria.size() > 0
                || requestCriteria.size() > 0
                || responseCriteria.size() > 0
                || restoreRequestCriteria.size() > 0
                || restoreResponseCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(requestCriteria);
                allCriteria.addAll(responseCriteria);
                allCriteria.addAll(restoreRequestCriteria);
                allCriteria.addAll(restoreResponseCriteria);
            }
            return allCriteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
            allCriteria = null;
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
            allCriteria = null;
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
            allCriteria = null;
        }

        public Criteria andIdIsNull() {
            addCriterion("d_id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("d_id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("d_id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("d_id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("d_id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("d_id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("d_id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("d_id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("d_id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("d_id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("d_id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("d_id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andAidIsNull() {
            addCriterion("d_aid is null");
            return (Criteria) this;
        }

        public Criteria andAidIsNotNull() {
            addCriterion("d_aid is not null");
            return (Criteria) this;
        }

        public Criteria andAidEqualTo(Integer value) {
            addCriterion("d_aid =", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidNotEqualTo(Integer value) {
            addCriterion("d_aid <>", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidGreaterThan(Integer value) {
            addCriterion("d_aid >", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidGreaterThanOrEqualTo(Integer value) {
            addCriterion("d_aid >=", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidLessThan(Integer value) {
            addCriterion("d_aid <", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidLessThanOrEqualTo(Integer value) {
            addCriterion("d_aid <=", value, "aid");
            return (Criteria) this;
        }

        public Criteria andAidIn(List<Integer> values) {
            addCriterion("d_aid in", values, "aid");
            return (Criteria) this;
        }

        public Criteria andAidNotIn(List<Integer> values) {
            addCriterion("d_aid not in", values, "aid");
            return (Criteria) this;
        }

        public Criteria andAidBetween(Integer value1, Integer value2) {
            addCriterion("d_aid between", value1, value2, "aid");
            return (Criteria) this;
        }

        public Criteria andAidNotBetween(Integer value1, Integer value2) {
            addCriterion("d_aid not between", value1, value2, "aid");
            return (Criteria) this;
        }

        public Criteria andCodeIsNull() {
            addCriterion("d_code is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("d_code is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(Integer value) {
            addCriterion("d_code =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(Integer value) {
            addCriterion("d_code <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(Integer value) {
            addCriterion("d_code >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("d_code >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(Integer value) {
            addCriterion("d_code <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(Integer value) {
            addCriterion("d_code <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<Integer> values) {
            addCriterion("d_code in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<Integer> values) {
            addCriterion("d_code not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(Integer value1, Integer value2) {
            addCriterion("d_code between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("d_code not between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeIsNull() {
            addCriterion("d_restore_code is null");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeIsNotNull() {
            addCriterion("d_restore_code is not null");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeEqualTo(Integer value) {
            addCriterion("d_restore_code =", value, "restoreCode");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeNotEqualTo(Integer value) {
            addCriterion("d_restore_code <>", value, "restoreCode");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeGreaterThan(Integer value) {
            addCriterion("d_restore_code >", value, "restoreCode");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("d_restore_code >=", value, "restoreCode");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeLessThan(Integer value) {
            addCriterion("d_restore_code <", value, "restoreCode");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeLessThanOrEqualTo(Integer value) {
            addCriterion("d_restore_code <=", value, "restoreCode");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeIn(List<Integer> values) {
            addCriterion("d_restore_code in", values, "restoreCode");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeNotIn(List<Integer> values) {
            addCriterion("d_restore_code not in", values, "restoreCode");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeBetween(Integer value1, Integer value2) {
            addCriterion("d_restore_code between", value1, value2, "restoreCode");
            return (Criteria) this;
        }

        public Criteria andRestoreCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("d_restore_code not between", value1, value2, "restoreCode");
            return (Criteria) this;
        }

        public Criteria andRequestIsNull() {
            addCriterion("d_request is null");
            return (Criteria) this;
        }

        public Criteria andRequestIsNotNull() {
            addCriterion("d_request is not null");
            return (Criteria) this;
        }

        public Criteria andRequestEqualTo(JsonObject value) {
            addRequestCriterion("d_request =", value, "request");
            return (Criteria) this;
        }

        public Criteria andRequestNotEqualTo(JsonObject value) {
            addRequestCriterion("d_request <>", value, "request");
            return (Criteria) this;
        }

        public Criteria andRequestGreaterThan(JsonObject value) {
            addRequestCriterion("d_request >", value, "request");
            return (Criteria) this;
        }

        public Criteria andRequestGreaterThanOrEqualTo(JsonObject value) {
            addRequestCriterion("d_request >=", value, "request");
            return (Criteria) this;
        }

        public Criteria andRequestLessThan(JsonObject value) {
            addRequestCriterion("d_request <", value, "request");
            return (Criteria) this;
        }

        public Criteria andRequestLessThanOrEqualTo(JsonObject value) {
            addRequestCriterion("d_request <=", value, "request");
            return (Criteria) this;
        }

        public Criteria andRequestLike(JsonObject value) {
            addRequestCriterion("d_request like", value, "request");
            return (Criteria) this;
        }

        public Criteria andRequestNotLike(JsonObject value) {
            addRequestCriterion("d_request not like", value, "request");
            return (Criteria) this;
        }

        public Criteria andRequestIn(List<JsonObject> values) {
            addRequestCriterion("d_request in", values, "request");
            return (Criteria) this;
        }

        public Criteria andRequestNotIn(List<JsonObject> values) {
            addRequestCriterion("d_request not in", values, "request");
            return (Criteria) this;
        }

        public Criteria andRequestBetween(JsonObject value1, JsonObject value2) {
            addRequestCriterion("d_request between", value1, value2, "request");
            return (Criteria) this;
        }

        public Criteria andRequestNotBetween(JsonObject value1, JsonObject value2) {
            addRequestCriterion("d_request not between", value1, value2, "request");
            return (Criteria) this;
        }

        public Criteria andResponseIsNull() {
            addCriterion("d_response is null");
            return (Criteria) this;
        }

        public Criteria andResponseIsNotNull() {
            addCriterion("d_response is not null");
            return (Criteria) this;
        }

        public Criteria andResponseEqualTo(JsonObject value) {
            addResponseCriterion("d_response =", value, "response");
            return (Criteria) this;
        }

        public Criteria andResponseNotEqualTo(JsonObject value) {
            addResponseCriterion("d_response <>", value, "response");
            return (Criteria) this;
        }

        public Criteria andResponseGreaterThan(JsonObject value) {
            addResponseCriterion("d_response >", value, "response");
            return (Criteria) this;
        }

        public Criteria andResponseGreaterThanOrEqualTo(JsonObject value) {
            addResponseCriterion("d_response >=", value, "response");
            return (Criteria) this;
        }

        public Criteria andResponseLessThan(JsonObject value) {
            addResponseCriterion("d_response <", value, "response");
            return (Criteria) this;
        }

        public Criteria andResponseLessThanOrEqualTo(JsonObject value) {
            addResponseCriterion("d_response <=", value, "response");
            return (Criteria) this;
        }

        public Criteria andResponseLike(JsonObject value) {
            addResponseCriterion("d_response like", value, "response");
            return (Criteria) this;
        }

        public Criteria andResponseNotLike(JsonObject value) {
            addResponseCriterion("d_response not like", value, "response");
            return (Criteria) this;
        }

        public Criteria andResponseIn(List<JsonObject> values) {
            addResponseCriterion("d_response in", values, "response");
            return (Criteria) this;
        }

        public Criteria andResponseNotIn(List<JsonObject> values) {
            addResponseCriterion("d_response not in", values, "response");
            return (Criteria) this;
        }

        public Criteria andResponseBetween(JsonObject value1, JsonObject value2) {
            addResponseCriterion("d_response between", value1, value2, "response");
            return (Criteria) this;
        }

        public Criteria andResponseNotBetween(JsonObject value1, JsonObject value2) {
            addResponseCriterion("d_response not between", value1, value2, "response");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestIsNull() {
            addCriterion("d_restore_request is null");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestIsNotNull() {
            addCriterion("d_restore_request is not null");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestEqualTo(JsonObject value) {
            addRestoreRequestCriterion("d_restore_request =", value, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestNotEqualTo(JsonObject value) {
            addRestoreRequestCriterion("d_restore_request <>", value, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestGreaterThan(JsonObject value) {
            addRestoreRequestCriterion("d_restore_request >", value, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestGreaterThanOrEqualTo(JsonObject value) {
            addRestoreRequestCriterion("d_restore_request >=", value, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestLessThan(JsonObject value) {
            addRestoreRequestCriterion("d_restore_request <", value, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestLessThanOrEqualTo(JsonObject value) {
            addRestoreRequestCriterion("d_restore_request <=", value, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestLike(JsonObject value) {
            addRestoreRequestCriterion("d_restore_request like", value, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestNotLike(JsonObject value) {
            addRestoreRequestCriterion("d_restore_request not like", value, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestIn(List<JsonObject> values) {
            addRestoreRequestCriterion("d_restore_request in", values, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestNotIn(List<JsonObject> values) {
            addRestoreRequestCriterion("d_restore_request not in", values, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestBetween(JsonObject value1, JsonObject value2) {
            addRestoreRequestCriterion("d_restore_request between", value1, value2, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreRequestNotBetween(JsonObject value1, JsonObject value2) {
            addRestoreRequestCriterion("d_restore_request not between", value1, value2, "restoreRequest");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseIsNull() {
            addCriterion("d_restore_response is null");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseIsNotNull() {
            addCriterion("d_restore_response is not null");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseEqualTo(JsonObject value) {
            addRestoreResponseCriterion("d_restore_response =", value, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseNotEqualTo(JsonObject value) {
            addRestoreResponseCriterion("d_restore_response <>", value, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseGreaterThan(JsonObject value) {
            addRestoreResponseCriterion("d_restore_response >", value, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseGreaterThanOrEqualTo(JsonObject value) {
            addRestoreResponseCriterion("d_restore_response >=", value, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseLessThan(JsonObject value) {
            addRestoreResponseCriterion("d_restore_response <", value, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseLessThanOrEqualTo(JsonObject value) {
            addRestoreResponseCriterion("d_restore_response <=", value, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseLike(JsonObject value) {
            addRestoreResponseCriterion("d_restore_response like", value, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseNotLike(JsonObject value) {
            addRestoreResponseCriterion("d_restore_response not like", value, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseIn(List<JsonObject> values) {
            addRestoreResponseCriterion("d_restore_response in", values, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseNotIn(List<JsonObject> values) {
            addRestoreResponseCriterion("d_restore_response not in", values, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseBetween(JsonObject value1, JsonObject value2) {
            addRestoreResponseCriterion("d_restore_response between", value1, value2, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRestoreResponseNotBetween(JsonObject value1, JsonObject value2) {
            addRestoreResponseCriterion("d_restore_response not between", value1, value2, "restoreResponse");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionIsNull() {
            addCriterion("d_request_exception is null");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionIsNotNull() {
            addCriterion("d_request_exception is not null");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionEqualTo(String value) {
            addCriterion("d_request_exception =", value, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionNotEqualTo(String value) {
            addCriterion("d_request_exception <>", value, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionGreaterThan(String value) {
            addCriterion("d_request_exception >", value, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionGreaterThanOrEqualTo(String value) {
            addCriterion("d_request_exception >=", value, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionLessThan(String value) {
            addCriterion("d_request_exception <", value, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionLessThanOrEqualTo(String value) {
            addCriterion("d_request_exception <=", value, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionLike(String value) {
            addCriterion("d_request_exception like", value, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionNotLike(String value) {
            addCriterion("d_request_exception not like", value, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionIn(List<String> values) {
            addCriterion("d_request_exception in", values, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionNotIn(List<String> values) {
            addCriterion("d_request_exception not in", values, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionBetween(String value1, String value2) {
            addCriterion("d_request_exception between", value1, value2, "requestException");
            return (Criteria) this;
        }

        public Criteria andRequestExceptionNotBetween(String value1, String value2) {
            addCriterion("d_request_exception not between", value1, value2, "requestException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionIsNull() {
            addCriterion("d_restore_exception is null");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionIsNotNull() {
            addCriterion("d_restore_exception is not null");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionEqualTo(String value) {
            addCriterion("d_restore_exception =", value, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionNotEqualTo(String value) {
            addCriterion("d_restore_exception <>", value, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionGreaterThan(String value) {
            addCriterion("d_restore_exception >", value, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionGreaterThanOrEqualTo(String value) {
            addCriterion("d_restore_exception >=", value, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionLessThan(String value) {
            addCriterion("d_restore_exception <", value, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionLessThanOrEqualTo(String value) {
            addCriterion("d_restore_exception <=", value, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionLike(String value) {
            addCriterion("d_restore_exception like", value, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionNotLike(String value) {
            addCriterion("d_restore_exception not like", value, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionIn(List<String> values) {
            addCriterion("d_restore_exception in", values, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionNotIn(List<String> values) {
            addCriterion("d_restore_exception not in", values, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionBetween(String value1, String value2) {
            addCriterion("d_restore_exception between", value1, value2, "restoreException");
            return (Criteria) this;
        }

        public Criteria andRestoreExceptionNotBetween(String value1, String value2) {
            addCriterion("d_restore_exception not between", value1, value2, "restoreException");
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

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private final String typeHandler;

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
    }
}
