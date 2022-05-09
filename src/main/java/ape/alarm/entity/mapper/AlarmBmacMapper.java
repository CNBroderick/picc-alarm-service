package ape.alarm.entity.mapper;

import ape.alarm.entity.po.AlarmBmac;
import ape.alarm.entity.po.AlarmBmacExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmBmacMapper {
    long countByExample(AlarmBmacExample example);

    int deleteByExample(AlarmBmacExample example);

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
    int insert(AlarmBmac record);

    int insertOrUpdate(AlarmBmac record);

    int insertOrUpdateSelective(AlarmBmac record);

    /**
     * insert record to table selective
     *
     * @param record the record
     *
     * @return insert count
     */
    int insertSelective(AlarmBmac record);

    List<AlarmBmac> selectByExample(AlarmBmacExample example);

    /**
     * select by primary key
     *
     * @param id primary key
     *
     * @return object by primary key
     */
    AlarmBmac selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AlarmBmac record, @Param("example") AlarmBmacExample example);

    int updateByExample(@Param("record") AlarmBmac record, @Param("example") AlarmBmacExample example);

    /**
     * update record selective
     *
     * @param record the updated record
     *
     * @return update count
     */
    int updateByPrimaryKeySelective(AlarmBmac record);

    /**
     * update record
     *
     * @param record the updated record
     *
     * @return update count
     */
    int updateByPrimaryKey(AlarmBmac record);

    AlarmBmac getById(@Param("id") Integer id);

    int updateStatusById(@Param("updatedStatus") String updatedStatus, @Param("id") Integer id);

    int updateBatch(List<AlarmBmac> list);

    int updateBatchSelective(List<AlarmBmac> list);

    int batchInsert(@Param("list") List<AlarmBmac> list);
}
