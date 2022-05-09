package ape.alarm.service.po;

import ape.alarm.entity.mapper.AlarmWosMapper;
import ape.alarm.entity.po.AlarmWos;
import ape.alarm.entity.po.AlarmWosExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AlarmWosService {

    @Resource
    private AlarmWosMapper alarmWosMapper;


    public long countByExample(AlarmWosExample example) {
        return alarmWosMapper.countByExample(example);
    }


    public int deleteByExample(AlarmWosExample example) {
        return alarmWosMapper.deleteByExample(example);
    }


    public int deleteByPrimaryKey(Integer id) {
        return alarmWosMapper.deleteByPrimaryKey(id);
    }


    public int insert(AlarmWos record) {
        return alarmWosMapper.insert(record);
    }


    public int insertOrUpdate(AlarmWos record) {
        return alarmWosMapper.insertOrUpdate(record);
    }


    public int insertOrUpdateSelective(AlarmWos record) {
        return alarmWosMapper.insertOrUpdateSelective(record);
    }


    public int insertSelective(AlarmWos record) {
        return alarmWosMapper.insertSelective(record);
    }


    public List<AlarmWos> selectByExample(AlarmWosExample example) {
        return alarmWosMapper.selectByExample(example);
    }


    public AlarmWos selectByPrimaryKey(Integer id) {
        return alarmWosMapper.selectByPrimaryKey(id);
    }


    public int updateByExampleSelective(AlarmWos record, AlarmWosExample example) {
        return alarmWosMapper.updateByExampleSelective(record, example);
    }


    public int updateByExample(AlarmWos record, AlarmWosExample example) {
        return alarmWosMapper.updateByExample(record, example);
    }


    public int updateByPrimaryKeySelective(AlarmWos record) {
        return alarmWosMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(AlarmWos record) {
        return alarmWosMapper.updateByPrimaryKey(record);
    }


    public int updateBatch(List<AlarmWos> list) {
        return alarmWosMapper.updateBatch(list);
    }


    public int updateBatchSelective(List<AlarmWos> list) {
        return alarmWosMapper.updateBatchSelective(list);
    }


    public int batchInsert(List<AlarmWos> list) {
        return alarmWosMapper.batchInsert(list);
    }

    public AlarmWos selectFirstByUuid(String uuid) {
        return alarmWosMapper.selectFirstByUuid(uuid);
    }

    public List<AlarmWos> selectAllByCloseTimeIsNull() {
        return alarmWosMapper.selectAllByCloseTimeIsNull();
    }
}
