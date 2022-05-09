package ape.alarm.service.po;

import ape.alarm.entity.mapper.AlarmWosInfoMapper;
import ape.alarm.entity.po.AlarmWosInfo;
import ape.alarm.entity.po.AlarmWosInfoExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class AlarmWosInfoService {

    @Resource
    private AlarmWosInfoMapper alarmWosInfoMapper;


    public long countByExample(AlarmWosInfoExample example) {
        return alarmWosInfoMapper.countByExample(example);
    }


    public int deleteByExample(AlarmWosInfoExample example) {
        return alarmWosInfoMapper.deleteByExample(example);
    }


    public int deleteByPrimaryKey(Integer id) {
        return alarmWosInfoMapper.deleteByPrimaryKey(id);
    }


    public int insert(AlarmWosInfo record) {
        return alarmWosInfoMapper.insert(record);
    }


    public int insertOrUpdate(AlarmWosInfo record) {
        return alarmWosInfoMapper.insertOrUpdate(record);
    }


    public int insertOrUpdateSelective(AlarmWosInfo record) {
        return alarmWosInfoMapper.insertOrUpdateSelective(record);
    }


    public int insertSelective(AlarmWosInfo record) {
        return alarmWosInfoMapper.insertSelective(record);
    }


    public List<AlarmWosInfo> selectByExample(AlarmWosInfoExample example) {
        return alarmWosInfoMapper.selectByExample(example);
    }


    public AlarmWosInfo selectByPrimaryKey(Integer id) {
        return alarmWosInfoMapper.selectByPrimaryKey(id);
    }


    public int updateByExampleSelective(AlarmWosInfo record, AlarmWosInfoExample example) {
        return alarmWosInfoMapper.updateByExampleSelective(record, example);
    }


    public int updateByExample(AlarmWosInfo record, AlarmWosInfoExample example) {
        return alarmWosInfoMapper.updateByExample(record, example);
    }


    public int updateByPrimaryKeySelective(AlarmWosInfo record) {
        return alarmWosInfoMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(AlarmWosInfo record) {
        return alarmWosInfoMapper.updateByPrimaryKey(record);
    }


    public int updateBatch(List<AlarmWosInfo> list) {
        return alarmWosInfoMapper.updateBatch(list);
    }


    public int updateBatchSelective(List<AlarmWosInfo> list) {
        return alarmWosInfoMapper.updateBatchSelective(list);
    }


    public int batchInsert(List<AlarmWosInfo> list) {
        return alarmWosInfoMapper.batchInsert(list);
    }

    public List<AlarmWosInfo> selectAllByWosId(Integer wosId) {
        return alarmWosInfoMapper.selectAllByWosId(wosId);
    }

    public List<AlarmWosInfo> selectAllByWosIdAndSendTimeIsNull(Integer wosId) {
        return alarmWosInfoMapper.selectAllByWosIdAndSendTimeIsNull(wosId);
    }

    public List<AlarmWosInfo> selectAllByWosIdAndSendTimeIsNotNull(Integer wosId) {
        return alarmWosInfoMapper.selectAllByWosIdAndSendTimeIsNotNull(wosId);
    }

    public List<AlarmWosInfo> selectAllByAlarmId(Integer alarmId) {
        return alarmWosInfoMapper.selectAllByAlarmId(alarmId);
    }

    public int updateSendTimeByWosIdAndAlarmIdIn(LocalDateTime updatedSendTime, Integer wosId, Collection<Integer> alarmIdCollection) {
        return alarmWosInfoMapper.updateSendTimeByWosIdAndAlarmIdIn(updatedSendTime, wosId, alarmIdCollection);
    }
}
