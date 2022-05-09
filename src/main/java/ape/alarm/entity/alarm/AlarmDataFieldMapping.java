package ape.alarm.entity.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.sql.ResultSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AlarmDataFieldMapping {

    private String type;
    private String name;
    private String caption;
    private String suffix;

    public boolean hasSuffix() {
        return suffix != null && !suffix.isBlank();
    }

    public String createFieldValue(String value) {
        return hasSuffix() ? value + " " + suffix : value;
    }

    public static class Mapper implements IEntityRowMapper<AlarmDataFieldMapping> {

        @Override
        public AlarmDataFieldMapping mapRow(ResultSet r) throws Exception {
            return new AlarmDataFieldMapping()
                    .setType(r.getString("d_type"))
                    .setName(r.getString("d_name"))
                    .setCaption(r.getString("d_caption"))
                    .setSuffix(r.getString("d_suffix"))
                    ;
        }
    }

}
