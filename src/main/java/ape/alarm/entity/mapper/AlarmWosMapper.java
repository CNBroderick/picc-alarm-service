package ape.alarm.entity.mapper;

import ape.alarm.entity.po.AlarmWos;
import ape.alarm.entity.po.AlarmWosExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmWosMapper {
    long countByExample(AlarmWosExample example);

    int deleteByExample(AlarmWosExample example);

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
    int insert(AlarmWos record);

    int insertOrUpdate(AlarmWos record);

    int insertOrUpdateSelective(AlarmWos record);

    /**
     * insert record to table selective
     *
     * @param record the record
     *
     * @return insert count
     */
    int insertSelective(AlarmWos record);

    List<AlarmWos> selectByExample(AlarmWosExample example);

    /**
     * select by primary key
     *
     * @param id primary key
     *
     * @return object by primary key
     */
    AlarmWos selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AlarmWos record, @Param("example") AlarmWosExample example);

    int updateByExample(@Param("record") AlarmWos record, @Param("example") AlarmWosExample example);

    /**
     * update record selective
     *
     * @param record the updated record
     *
     * @return update count
     */
    int updateByPrimaryKeySelective(AlarmWos record);

    /**
     * update record
     *
     * @param record the updated record
     *
     * @return update count
     */
    int updateByPrimaryKey(AlarmWos record);

    AlarmWos selectFirstByUuid(@Param("uuid") String uuid);

    List<AlarmWos> selectAllByCloseTimeIsNull();

    int updateBatch(List<AlarmWos> list);

    int updateBatchSelective(List<AlarmWos> list);

    int batchInsert(@Param("list") List<AlarmWos> list);
}
