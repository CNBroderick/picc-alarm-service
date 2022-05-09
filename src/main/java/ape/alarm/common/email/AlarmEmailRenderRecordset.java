package ape.alarm.common.email;

import dataq.core.data.schema.Recordset;
import org.bklab.quark.util.schema.RecordFactory;
import org.bklab.quark.util.schema.SchemaFactory;

public class AlarmEmailRenderRecordset extends Recordset {
    public final static String FIELD_NAME = "名称";
    public final static String FIELD_DESCRIPTION = "描述";
    public final static String FIELD_VALUE = "值";

    public AlarmEmailRenderRecordset() {
        super(new SchemaFactory().string(FIELD_NAME, FIELD_DESCRIPTION, FIELD_VALUE).get());
    }

    public AlarmEmailRenderRecordset addRecord(String name, String value) {
        return addRecord(name, value, "");
    }

    public AlarmEmailRenderRecordset addRecord(String name, String value, String description) {
        new RecordFactory(createRecord())
                .set(FIELD_NAME, name)
                .set(FIELD_VALUE, value)
                .set(FIELD_DESCRIPTION, description)
                .get();
        return this;
    }

}
