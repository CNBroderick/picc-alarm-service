package ape.alarm.entity.mapper;

import ape.alarm.entity.po.AlarmWosInfo;
import ape.alarm.entity.po.AlarmWosInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Mapper
public interface AlarmWosInfoMapper {
    long countByExample(AlarmWosInfoExample example);

    int deleteByExample(AlarmWosInfoExample example);

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
    int insert(AlarmWosInfo record);

    int insertOrUpdate(AlarmWosInfo record);

    int insertOrUpdateSelective(AlarmWosInfo record);

    /**
     * insert record to table selective
     *
     * @param record the record
     *
     * @return insert count
     */
    int insertSelective(AlarmWosInfo record);

    List<AlarmWosInfo> selectByExample(AlarmWosInfoExample example);

    /**
     * select by primary key
     *
     * @param id primary key
     *
     * @return object by primary key
     */
    AlarmWosInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AlarmWosInfo record, @Param("example") AlarmWosInfoExample example);

    int updateByExample(@Param("record") AlarmWosInfo record, @Param("example") AlarmWosInfoExample example);

    /**
     * update record selective
     *
     * @param record the updated record
     *
     * @return update count
     */
    int updateByPrimaryKeySelective(AlarmWosInfo record);

    /**
     * update record
     *
     * @param record the updated record
     *
     * @return update count
     */
    int updateByPrimaryKey(AlarmWosInfo record);

    List<AlarmWosInfo> selectAllByWosId(@Param("wosId") Integer wosId);

    List<AlarmWosInfo> selectAllByWosIdAndSendTimeIsNull(@Param("wosId") Integer wosId);

    List<AlarmWosInfo> selectAllByWosIdAndSendTimeIsNotNull(@Param("wosId") Integer wosId);

    List<AlarmWosInfo> selectAllByAlarmId(@Param("alarmId") Integer alarmId);

    int updateSendTimeByWosIdAndAlarmIdIn(@Param("updatedSendTime") LocalDateTime updatedSendTime, @Param("wosId") Integer wosId, @Param("alarmIdCollection") Collection<Integer> alarmIdCollection);

    int updateBatch(List<AlarmWosInfo> list);

    int updateBatchSelective(List<AlarmWosInfo> list);

    int batchInsert(@Param("list") List<AlarmWosInfo> list);
}
