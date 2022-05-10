package ape.alarm.service.po;

import ape.alarm.entity.mapper.AlarmBmacDataMapper;
import ape.alarm.entity.po.AlarmBmacData;
import ape.alarm.entity.po.AlarmBmacDataExample;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AlarmBmacDataService {

    @Resource
    private AlarmBmacDataMapper alarmBmacDataMapper;


    public long countByExample(AlarmBmacDataExample example) {
        return alarmBmacDataMapper.countByExample(example);
    }


    public int deleteByExample(AlarmBmacDataExample example) {
        return alarmBmacDataMapper.deleteByExample(example);
    }


    public int deleteByPrimaryKey(Integer id) {
        return alarmBmacDataMapper.deleteByPrimaryKey(id);
    }


    public int insert(AlarmBmacData record) {
        return alarmBmacDataMapper.insert(record);
    }


    public int insertOrUpdate(AlarmBmacData record) {
        return alarmBmacDataMapper.insertOrUpdate(record);
    }


    public int insertOrUpdateSelective(AlarmBmacData record) {
        return alarmBmacDataMapper.insertOrUpdateSelective(record);
    }


    public int insertSelective(AlarmBmacData record) {
        return alarmBmacDataMapper.insertSelective(record);
    }


    public List<AlarmBmacData> selectByExample(AlarmBmacDataExample example) {
        return alarmBmacDataMapper.selectByExample(example);
    }


    public AlarmBmacData selectByPrimaryKey(Integer id) {
        return alarmBmacDataMapper.selectByPrimaryKey(id);
    }


    public int updateByExampleSelective(AlarmBmacData record, AlarmBmacDataExample example) {
        return alarmBmacDataMapper.updateByExampleSelective(record, example);
    }


    public int updateByExample(AlarmBmacData record, AlarmBmacDataExample example) {
        return alarmBmacDataMapper.updateByExample(record, example);
    }


    public int updateByPrimaryKeySelective(AlarmBmacData record) {
        return alarmBmacDataMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(AlarmBmacData record) {
        return alarmBmacDataMapper.updateByPrimaryKey(record);
    }


    public int updateRequestById(JsonObject updatedRequest, Integer id) {
        return alarmBmacDataMapper.updateRequestById(updatedRequest, id);
    }


    public int UpdateCodeAndResponseById(Integer updatedCode, JsonObject updatedResponse, Integer id) {
        return alarmBmacDataMapper.UpdateCodeAndResponseById(updatedCode, updatedResponse, id);
    }


    public int updateBatch(List<AlarmBmacData> list) {
        return alarmBmacDataMapper.updateBatch(list);
    }


    public int updateBatchSelective(List<AlarmBmacData> list) {
        return alarmBmacDataMapper.updateBatchSelective(list);
    }


    public int batchInsert(List<AlarmBmacData> list) {
        return alarmBmacDataMapper.batchInsert(list);
    }

    public AlarmBmacData getById(Integer id) {
        return alarmBmacDataMapper.getById(id);
    }

    public int updateCodeAndResponseById(Integer updatedCode, JsonObject updatedResponse, Integer id) {
        return alarmBmacDataMapper.updateCodeAndResponseById(updatedCode, updatedResponse, id);
    }

    public int updateRequestExceptionById(String updatedRequestException, Integer id) {
        return alarmBmacDataMapper.updateRequestExceptionById(updatedRequestException, id);
    }

    public int updateRestoreExceptionById(String updatedRestoreException, Integer id) {
        return alarmBmacDataMapper.updateRestoreExceptionById(updatedRestoreException, id);
    }
}

