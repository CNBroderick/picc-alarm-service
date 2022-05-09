package ape.alarm.service.po;

import ape.alarm.entity.mapper.AlarmBmacMapper;
import ape.alarm.entity.po.AlarmBmac;
import ape.alarm.entity.po.AlarmBmacExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AlarmBmacService {

    @Resource
    private AlarmBmacMapper alarmBmacMapper;


    public long countByExample(AlarmBmacExample example) {
        return alarmBmacMapper.countByExample(example);
    }


    public int deleteByExample(AlarmBmacExample example) {
        return alarmBmacMapper.deleteByExample(example);
    }


    public int deleteByPrimaryKey(Integer id) {
        return alarmBmacMapper.deleteByPrimaryKey(id);
    }


    public int insert(AlarmBmac record) {
        return alarmBmacMapper.insert(record);
    }


    public int insertOrUpdate(AlarmBmac record) {
        return alarmBmacMapper.insertOrUpdate(record);
    }


    public int insertOrUpdateSelective(AlarmBmac record) {
        return alarmBmacMapper.insertOrUpdateSelective(record);
    }


    public int insertSelective(AlarmBmac record) {
        return alarmBmacMapper.insertSelective(record);
    }


    public List<AlarmBmac> selectByExample(AlarmBmacExample example) {
        return alarmBmacMapper.selectByExample(example);
    }


    public AlarmBmac selectByPrimaryKey(Integer id) {
        return alarmBmacMapper.selectByPrimaryKey(id);
    }


    public int updateByExampleSelective(AlarmBmac record, AlarmBmacExample example) {
        return alarmBmacMapper.updateByExampleSelective(record, example);
    }


    public int updateByExample(AlarmBmac record, AlarmBmacExample example) {
        return alarmBmacMapper.updateByExample(record, example);
    }


    public int updateByPrimaryKeySelective(AlarmBmac record) {
        return alarmBmacMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(AlarmBmac record) {
        return alarmBmacMapper.updateByPrimaryKey(record);
    }


    public AlarmBmac getById(Integer id) {
        return alarmBmacMapper.getById(id);
    }


    public int updateStatusById(String updatedStatus, Integer id) {
        return alarmBmacMapper.updateStatusById(updatedStatus, id);
    }


    public int updateBatch(List<AlarmBmac> list) {
        return alarmBmacMapper.updateBatch(list);
    }


    public int updateBatchSelective(List<AlarmBmac> list) {
        return alarmBmacMapper.updateBatchSelective(list);
    }


    public int batchInsert(List<AlarmBmac> list) {
        return alarmBmacMapper.batchInsert(list);
    }

}
