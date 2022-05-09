package ape.alarm.entity.po;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmWosExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AlarmWosExample() {
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

        public Criteria andUuidIsNull() {
            addCriterion("`d_uuid` is null");
            return (Criteria) this;
        }

        public Criteria andUuidIsNotNull() {
            addCriterion("`d_uuid` is not null");
            return (Criteria) this;
        }

        public Criteria andUuidEqualTo(String value) {
            addCriterion("`d_uuid` =", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotEqualTo(String value) {
            addCriterion("`d_uuid` <>", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThan(String value) {
            addCriterion("`d_uuid` >", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThanOrEqualTo(String value) {
            addCriterion("`d_uuid` >=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThan(String value) {
            addCriterion("`d_uuid` <", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThanOrEqualTo(String value) {
            addCriterion("`d_uuid` <=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLike(String value) {
            addCriterion("`d_uuid` like", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotLike(String value) {
            addCriterion("`d_uuid` not like", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidIn(List<String> values) {
            addCriterion("`d_uuid` in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotIn(List<String> values) {
            addCriterion("`d_uuid` not in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidBetween(String value1, String value2) {
            addCriterion("`d_uuid` between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotBetween(String value1, String value2) {
            addCriterion("`d_uuid` not between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andImSourceIsNull() {
            addCriterion("`d_im_source` is null");
            return (Criteria) this;
        }

        public Criteria andImSourceIsNotNull() {
            addCriterion("`d_im_source` is not null");
            return (Criteria) this;
        }

        public Criteria andImSourceEqualTo(String value) {
            addCriterion("`d_im_source` =", value, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceNotEqualTo(String value) {
            addCriterion("`d_im_source` <>", value, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceGreaterThan(String value) {
            addCriterion("`d_im_source` >", value, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceGreaterThanOrEqualTo(String value) {
            addCriterion("`d_im_source` >=", value, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceLessThan(String value) {
            addCriterion("`d_im_source` <", value, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceLessThanOrEqualTo(String value) {
            addCriterion("`d_im_source` <=", value, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceLike(String value) {
            addCriterion("`d_im_source` like", value, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceNotLike(String value) {
            addCriterion("`d_im_source` not like", value, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceIn(List<String> values) {
            addCriterion("`d_im_source` in", values, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceNotIn(List<String> values) {
            addCriterion("`d_im_source` not in", values, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceBetween(String value1, String value2) {
            addCriterion("`d_im_source` between", value1, value2, "imSource");
            return (Criteria) this;
        }

        public Criteria andImSourceNotBetween(String value1, String value2) {
            addCriterion("`d_im_source` not between", value1, value2, "imSource");
            return (Criteria) this;
        }

        public Criteria andWfNumIsNull() {
            addCriterion("`d_wf_num` is null");
            return (Criteria) this;
        }

        public Criteria andWfNumIsNotNull() {
            addCriterion("`d_wf_num` is not null");
            return (Criteria) this;
        }

        public Criteria andWfNumEqualTo(String value) {
            addCriterion("`d_wf_num` =", value, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumNotEqualTo(String value) {
            addCriterion("`d_wf_num` <>", value, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumGreaterThan(String value) {
            addCriterion("`d_wf_num` >", value, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumGreaterThanOrEqualTo(String value) {
            addCriterion("`d_wf_num` >=", value, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumLessThan(String value) {
            addCriterion("`d_wf_num` <", value, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumLessThanOrEqualTo(String value) {
            addCriterion("`d_wf_num` <=", value, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumLike(String value) {
            addCriterion("`d_wf_num` like", value, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumNotLike(String value) {
            addCriterion("`d_wf_num` not like", value, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumIn(List<String> values) {
            addCriterion("`d_wf_num` in", values, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumNotIn(List<String> values) {
            addCriterion("`d_wf_num` not in", values, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumBetween(String value1, String value2) {
            addCriterion("`d_wf_num` between", value1, value2, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfNumNotBetween(String value1, String value2) {
            addCriterion("`d_wf_num` not between", value1, value2, "wfNum");
            return (Criteria) this;
        }

        public Criteria andWfIdIsNull() {
            addCriterion("`d_wf_id` is null");
            return (Criteria) this;
        }

        public Criteria andWfIdIsNotNull() {
            addCriterion("`d_wf_id` is not null");
            return (Criteria) this;
        }

        public Criteria andWfIdEqualTo(String value) {
            addCriterion("`d_wf_id` =", value, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdNotEqualTo(String value) {
            addCriterion("`d_wf_id` <>", value, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdGreaterThan(String value) {
            addCriterion("`d_wf_id` >", value, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdGreaterThanOrEqualTo(String value) {
            addCriterion("`d_wf_id` >=", value, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdLessThan(String value) {
            addCriterion("`d_wf_id` <", value, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdLessThanOrEqualTo(String value) {
            addCriterion("`d_wf_id` <=", value, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdLike(String value) {
            addCriterion("`d_wf_id` like", value, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdNotLike(String value) {
            addCriterion("`d_wf_id` not like", value, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdIn(List<String> values) {
            addCriterion("`d_wf_id` in", values, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdNotIn(List<String> values) {
            addCriterion("`d_wf_id` not in", values, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdBetween(String value1, String value2) {
            addCriterion("`d_wf_id` between", value1, value2, "wfId");
            return (Criteria) this;
        }

        public Criteria andWfIdNotBetween(String value1, String value2) {
            addCriterion("`d_wf_id` not between", value1, value2, "wfId");
            return (Criteria) this;
        }

        public Criteria andMntOrderIsNull() {
            addCriterion("`d_mnt_order` is null");
            return (Criteria) this;
        }

        public Criteria andMntOrderIsNotNull() {
            addCriterion("`d_mnt_order` is not null");
            return (Criteria) this;
        }

        public Criteria andMntOrderEqualTo(Integer value) {
            addCriterion("`d_mnt_order` =", value, "mntOrder");
            return (Criteria) this;
        }

        public Criteria andMntOrderNotEqualTo(Integer value) {
            addCriterion("`d_mnt_order` <>", value, "mntOrder");
            return (Criteria) this;
        }

        public Criteria andMntOrderGreaterThan(Integer value) {
            addCriterion("`d_mnt_order` >", value, "mntOrder");
            return (Criteria) this;
        }

        public Criteria andMntOrderGreaterThanOrEqualTo(Integer value) {
            addCriterion("`d_mnt_order` >=", value, "mntOrder");
            return (Criteria) this;
        }

        public Criteria andMntOrderLessThan(Integer value) {
            addCriterion("`d_mnt_order` <", value, "mntOrder");
            return (Criteria) this;
        }

        public Criteria andMntOrderLessThanOrEqualTo(Integer value) {
            addCriterion("`d_mnt_order` <=", value, "mntOrder");
            return (Criteria) this;
        }

        public Criteria andMntOrderIn(List<Integer> values) {
            addCriterion("`d_mnt_order` in", values, "mntOrder");
            return (Criteria) this;
        }

        public Criteria andMntOrderNotIn(List<Integer> values) {
            addCriterion("`d_mnt_order` not in", values, "mntOrder");
            return (Criteria) this;
        }

        public Criteria andMntOrderBetween(Integer value1, Integer value2) {
            addCriterion("`d_mnt_order` between", value1, value2, "mntOrder");
            return (Criteria) this;
        }

        public Criteria andMntOrderNotBetween(Integer value1, Integer value2) {
            addCriterion("`d_mnt_order` not between", value1, value2, "mntOrder");
            return (Criteria) this;
        }

        public Criteria andResCodeIsNull() {
            addCriterion("`d_res_code` is null");
            return (Criteria) this;
        }

        public Criteria andResCodeIsNotNull() {
            addCriterion("`d_res_code` is not null");
            return (Criteria) this;
        }

        public Criteria andResCodeEqualTo(Integer value) {
            addCriterion("`d_res_code` =", value, "resCode");
            return (Criteria) this;
        }

        public Criteria andResCodeNotEqualTo(Integer value) {
            addCriterion("`d_res_code` <>", value, "resCode");
            return (Criteria) this;
        }

        public Criteria andResCodeGreaterThan(Integer value) {
            addCriterion("`d_res_code` >", value, "resCode");
            return (Criteria) this;
        }

        public Criteria andResCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("`d_res_code` >=", value, "resCode");
            return (Criteria) this;
        }

        public Criteria andResCodeLessThan(Integer value) {
            addCriterion("`d_res_code` <", value, "resCode");
            return (Criteria) this;
        }

        public Criteria andResCodeLessThanOrEqualTo(Integer value) {
            addCriterion("`d_res_code` <=", value, "resCode");
            return (Criteria) this;
        }

        public Criteria andResCodeIn(List<Integer> values) {
            addCriterion("`d_res_code` in", values, "resCode");
            return (Criteria) this;
        }

        public Criteria andResCodeNotIn(List<Integer> values) {
            addCriterion("`d_res_code` not in", values, "resCode");
            return (Criteria) this;
        }

        public Criteria andResCodeBetween(Integer value1, Integer value2) {
            addCriterion("`d_res_code` between", value1, value2, "resCode");
            return (Criteria) this;
        }

        public Criteria andResCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("`d_res_code` not between", value1, value2, "resCode");
            return (Criteria) this;
        }

        public Criteria andResMessageIsNull() {
            addCriterion("`d_res_message` is null");
            return (Criteria) this;
        }

        public Criteria andResMessageIsNotNull() {
            addCriterion("`d_res_message` is not null");
            return (Criteria) this;
        }

        public Criteria andResMessageEqualTo(String value) {
            addCriterion("`d_res_message` =", value, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageNotEqualTo(String value) {
            addCriterion("`d_res_message` <>", value, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageGreaterThan(String value) {
            addCriterion("`d_res_message` >", value, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageGreaterThanOrEqualTo(String value) {
            addCriterion("`d_res_message` >=", value, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageLessThan(String value) {
            addCriterion("`d_res_message` <", value, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageLessThanOrEqualTo(String value) {
            addCriterion("`d_res_message` <=", value, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageLike(String value) {
            addCriterion("`d_res_message` like", value, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageNotLike(String value) {
            addCriterion("`d_res_message` not like", value, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageIn(List<String> values) {
            addCriterion("`d_res_message` in", values, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageNotIn(List<String> values) {
            addCriterion("`d_res_message` not in", values, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageBetween(String value1, String value2) {
            addCriterion("`d_res_message` between", value1, value2, "resMessage");
            return (Criteria) this;
        }

        public Criteria andResMessageNotBetween(String value1, String value2) {
            addCriterion("`d_res_message` not between", value1, value2, "resMessage");
            return (Criteria) this;
        }

        public Criteria andWfTitleIsNull() {
            addCriterion("`d_wf_title` is null");
            return (Criteria) this;
        }

        public Criteria andWfTitleIsNotNull() {
            addCriterion("`d_wf_title` is not null");
            return (Criteria) this;
        }

        public Criteria andWfTitleEqualTo(String value) {
            addCriterion("`d_wf_title` =", value, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleNotEqualTo(String value) {
            addCriterion("`d_wf_title` <>", value, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleGreaterThan(String value) {
            addCriterion("`d_wf_title` >", value, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleGreaterThanOrEqualTo(String value) {
            addCriterion("`d_wf_title` >=", value, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleLessThan(String value) {
            addCriterion("`d_wf_title` <", value, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleLessThanOrEqualTo(String value) {
            addCriterion("`d_wf_title` <=", value, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleLike(String value) {
            addCriterion("`d_wf_title` like", value, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleNotLike(String value) {
            addCriterion("`d_wf_title` not like", value, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleIn(List<String> values) {
            addCriterion("`d_wf_title` in", values, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleNotIn(List<String> values) {
            addCriterion("`d_wf_title` not in", values, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleBetween(String value1, String value2) {
            addCriterion("`d_wf_title` between", value1, value2, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfTitleNotBetween(String value1, String value2) {
            addCriterion("`d_wf_title` not between", value1, value2, "wfTitle");
            return (Criteria) this;
        }

        public Criteria andWfDescIsNull() {
            addCriterion("`d_wf_desc` is null");
            return (Criteria) this;
        }

        public Criteria andWfDescIsNotNull() {
            addCriterion("`d_wf_desc` is not null");
            return (Criteria) this;
        }

        public Criteria andWfDescEqualTo(String value) {
            addCriterion("`d_wf_desc` =", value, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescNotEqualTo(String value) {
            addCriterion("`d_wf_desc` <>", value, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescGreaterThan(String value) {
            addCriterion("`d_wf_desc` >", value, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescGreaterThanOrEqualTo(String value) {
            addCriterion("`d_wf_desc` >=", value, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescLessThan(String value) {
            addCriterion("`d_wf_desc` <", value, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescLessThanOrEqualTo(String value) {
            addCriterion("`d_wf_desc` <=", value, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescLike(String value) {
            addCriterion("`d_wf_desc` like", value, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescNotLike(String value) {
            addCriterion("`d_wf_desc` not like", value, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescIn(List<String> values) {
            addCriterion("`d_wf_desc` in", values, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescNotIn(List<String> values) {
            addCriterion("`d_wf_desc` not in", values, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescBetween(String value1, String value2) {
            addCriterion("`d_wf_desc` between", value1, value2, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andWfDescNotBetween(String value1, String value2) {
            addCriterion("`d_wf_desc` not between", value1, value2, "wfDesc");
            return (Criteria) this;
        }

        public Criteria andIsSaveIsNull() {
            addCriterion("`d_is_save` is null");
            return (Criteria) this;
        }

        public Criteria andIsSaveIsNotNull() {
            addCriterion("`d_is_save` is not null");
            return (Criteria) this;
        }

        public Criteria andIsSaveEqualTo(String value) {
            addCriterion("`d_is_save` =", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveNotEqualTo(String value) {
            addCriterion("`d_is_save` <>", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveGreaterThan(String value) {
            addCriterion("`d_is_save` >", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveGreaterThanOrEqualTo(String value) {
            addCriterion("`d_is_save` >=", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveLessThan(String value) {
            addCriterion("`d_is_save` <", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveLessThanOrEqualTo(String value) {
            addCriterion("`d_is_save` <=", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveLike(String value) {
            addCriterion("`d_is_save` like", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveNotLike(String value) {
            addCriterion("`d_is_save` not like", value, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveIn(List<String> values) {
            addCriterion("`d_is_save` in", values, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveNotIn(List<String> values) {
            addCriterion("`d_is_save` not in", values, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveBetween(String value1, String value2) {
            addCriterion("`d_is_save` between", value1, value2, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsSaveNotBetween(String value1, String value2) {
            addCriterion("`d_is_save` not between", value1, value2, "isSave");
            return (Criteria) this;
        }

        public Criteria andIsEndIsNull() {
            addCriterion("`d_is_end` is null");
            return (Criteria) this;
        }

        public Criteria andIsEndIsNotNull() {
            addCriterion("`d_is_end` is not null");
            return (Criteria) this;
        }

        public Criteria andIsEndEqualTo(String value) {
            addCriterion("`d_is_end` =", value, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndNotEqualTo(String value) {
            addCriterion("`d_is_end` <>", value, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndGreaterThan(String value) {
            addCriterion("`d_is_end` >", value, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndGreaterThanOrEqualTo(String value) {
            addCriterion("`d_is_end` >=", value, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndLessThan(String value) {
            addCriterion("`d_is_end` <", value, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndLessThanOrEqualTo(String value) {
            addCriterion("`d_is_end` <=", value, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndLike(String value) {
            addCriterion("`d_is_end` like", value, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndNotLike(String value) {
            addCriterion("`d_is_end` not like", value, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndIn(List<String> values) {
            addCriterion("`d_is_end` in", values, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndNotIn(List<String> values) {
            addCriterion("`d_is_end` not in", values, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndBetween(String value1, String value2) {
            addCriterion("`d_is_end` between", value1, value2, "isEnd");
            return (Criteria) this;
        }

        public Criteria andIsEndNotBetween(String value1, String value2) {
            addCriterion("`d_is_end` not between", value1, value2, "isEnd");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("`d_create_time` is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("`d_create_time` is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(LocalDateTime value) {
            addCriterion("`d_create_time` =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(LocalDateTime value) {
            addCriterion("`d_create_time` <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(LocalDateTime value) {
            addCriterion("`d_create_time` >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("`d_create_time` >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(LocalDateTime value) {
            addCriterion("`d_create_time` <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("`d_create_time` <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<LocalDateTime> values) {
            addCriterion("`d_create_time` in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<LocalDateTime> values) {
            addCriterion("`d_create_time` not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`d_create_time` between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`d_create_time` not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeIsNull() {
            addCriterion("`d_close_time` is null");
            return (Criteria) this;
        }

        public Criteria andCloseTimeIsNotNull() {
            addCriterion("`d_close_time` is not null");
            return (Criteria) this;
        }

        public Criteria andCloseTimeEqualTo(LocalDateTime value) {
            addCriterion("`d_close_time` =", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeNotEqualTo(LocalDateTime value) {
            addCriterion("`d_close_time` <>", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeGreaterThan(LocalDateTime value) {
            addCriterion("`d_close_time` >", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("`d_close_time` >=", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeLessThan(LocalDateTime value) {
            addCriterion("`d_close_time` <", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("`d_close_time` <=", value, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeIn(List<LocalDateTime> values) {
            addCriterion("`d_close_time` in", values, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeNotIn(List<LocalDateTime> values) {
            addCriterion("`d_close_time` not in", values, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`d_close_time` between", value1, value2, "closeTime");
            return (Criteria) this;
        }

        public Criteria andCloseTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`d_close_time` not between", value1, value2, "closeTime");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`d_status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`d_status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("`d_status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("`d_status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("`d_status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("`d_status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("`d_status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("`d_status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("`d_status` like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("`d_status` not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("`d_status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("`d_status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("`d_status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("`d_status` not between", value1, value2, "status");
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
