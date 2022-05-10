package ape.alarm.entity.mapper;

import ape.alarm.entity.po.AlarmBmacData;
import ape.alarm.entity.po.AlarmBmacDataExample;
import com.google.gson.JsonObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmBmacDataMapper {
    long countByExample(AlarmBmacDataExample example);

    int deleteByExample(AlarmBmacDataExample example);

    /**
     * delete by primary key
     *
     * @param id primaryKey
     *
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert record to table
     *
     * @param record the record
     *
     * @return insert count
     */
    int insert(AlarmBmacData record);

    int insertOrUpdate(AlarmBmacData record);

    int insertOrUpdateSelective(AlarmBmacData record);

    /**
     * insert record to table selective
     *
     * @param record the record
     *
     * @return insert count
     */
    int insertSelective(AlarmBmacData record);

    List<AlarmBmacData> selectByExample(AlarmBmacDataExample example);

    /**
     * select by primary key
     *
     * @param id primary key
     *
     * @return object by primary key
     */
    AlarmBmacData selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AlarmBmacData record, @Param("example") AlarmBmacDataExample example);

    int updateByExample(@Param("record") AlarmBmacData record, @Param("example") AlarmBmacDataExample example);

    /**
     * update record selective
     *
     * @param record the updated record
     *
     * @return update count
     */
    int updateByPrimaryKeySelective(AlarmBmacData record);

    /**
     * update record
     *
     * @param record the updated record
     *
     * @return update count
     */
    int updateByPrimaryKey(AlarmBmacData record);

    int updateRequestById(@Param("updatedRequest") JsonObject updatedRequest, @Param("id") Integer id);

    int updateCodeAndResponseById(@Param("updatedCode") Integer updatedCode, @Param("updatedResponse") JsonObject updatedResponse, @Param("id") Integer id);

    AlarmBmacData getById(@Param("id") Integer id);

    int updateRequestExceptionById(@Param("updatedRequestException") String updatedRequestException, @Param("id") Integer id);

    int updateRestoreExceptionById(@Param("updatedRestoreException") String updatedRestoreException, @Param("id") Integer id);

    int updateBatch(List<AlarmBmacData> list);

    int updateBatchSelective(List<AlarmBmacData> list);

    int batchInsert(@Param("list") List<AlarmBmacData> list);

    int UpdateCodeAndResponseById(@Param("updatedCode") Integer updatedCode, @Param("updatedResponse") JsonObject updatedResponse, @Param("id") Integer id);
}
