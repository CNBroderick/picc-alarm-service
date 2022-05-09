package ape.alarm.entity.typehandler;

import com.google.gson.JsonObject;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.bklab.quark.util.json.GsonJsonObjectUtil;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonObjectTypeHandler implements TypeHandler<JsonObject> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, JsonObject jsonObject, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, jsonObject == null ? null : jsonObject.toString());
    }

    @Override
    public JsonObject getResult(ResultSet resultSet, String s) throws SQLException {
        return new GsonJsonObjectUtil(resultSet.getString(s)).get();
    }

    @Override
    public JsonObject getResult(ResultSet resultSet, int i) throws SQLException {
        return new GsonJsonObjectUtil(resultSet.getString(i)).get();
    }

    @Override
    public JsonObject getResult(CallableStatement callableStatement, int i) throws SQLException {
        return new GsonJsonObjectUtil(callableStatement.getString(i)).get();
    }
}
