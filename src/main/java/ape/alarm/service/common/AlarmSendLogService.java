package ape.alarm.service.common;

import ape.alarm.entity.alarm.AlarmSendLog;
import ape.master.entity.alarm.po.AlarmSendLogPO;
import ape.master.entity.alarm.po.AlarmSendStatus;
import ape.master.repository.AlarmSendLogPORepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlarmSendLogService {

    @Resource
    private AlarmSendLogPORepository alarmSendLogPORepository;

    public void saveAll(List<AlarmSendLog> alarmSendLogs) {
        alarmSendLogPORepository.saveAll(alarmSendLogs.stream().map(AlarmSendLog::createPO).collect(Collectors.toList()));
    }

    public AlarmSendLog save(AlarmSendLog alarmSendLog) {
        return alarmSendLog.setAid(alarmSendLogPORepository.saveAndFlush(alarmSendLog.createPO()).getId());
    }

    public List<AlarmSendLogPO> findAllByStatusIn(AlarmSendStatus... sendStatuses) {
        return findAllByStatusIn(Set.of(sendStatuses));
    }

    public List<AlarmSendLogPO> findAllByStatusIn(Set<AlarmSendStatus> sendStatuses) {
        return alarmSendLogPORepository.findAllByStatusIn(sendStatuses);
    }
}
