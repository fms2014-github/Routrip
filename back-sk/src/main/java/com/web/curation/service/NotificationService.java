package com.web.curation.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.web.curation.model.user.Alarm;
import com.web.curation.model.user.StreamDataSet;
import com.web.curation.model.user.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableScheduling
public class NotificationService {
	private final ConcurrentHashMap<String, StreamDataSet> eventMap = new ConcurrentHashMap<>();
	
	void addEmitter(final String uuid, final StreamDataSet dataSet) {
		eventMap.put(uuid, dataSet);
	}
	
	void removeEmitter(final String uuid) {
		eventMap.remove(uuid);
	}
	
	@Scheduled(initialDelay=2000L, fixedDelay=5000L)
	public void fetch() {
		if(eventMap.size() == 0) {
			return;
		}
		this.handleAlert();
	}
	
	@Transactional
	public void handleAlert() {
		List<String> toBeRemoved = new ArrayList<>(eventMap.size());
		List<Long> alertIdList = new ArrayList<>();
		
		for(Map.Entry<String, StreamDataSet> entry : eventMap.entrySet()) {
			final String uniqueKey = entry.getKey();
			final StreamDataSet dataSet = entry.getValue();
			
			final User user = dataSet.getUser();
			try {
				final List<Alarm> receivingAlert = new UserService().getAlarmNoRead(user.getUid());
				final int nonReadCount = receivingAlert.size();
				
				if(nonReadCount == 0) {
					continue;
				}
				
				final SseEmitter emitter = dataSet.getSseEmitter();
				
				final List<Alarm> alertList = getListAnMinuteAndAlertFalse(receivingAlert);
				
				if(alertList.size()==0) {
					continue;
				}
				
				//NotificationA
			} catch (Exception e) {
				System.out.println("알림 과정 중 뭔가가 실패");
			}
		}
	}
	
	private ArrayList<Alarm> getListAnMinuteAndAlertFalse(List<Alarm> paramList){
		ArrayList<Alarm> alertList = new ArrayList<Alarm>();
		LocalDateTime beforeTime = LocalDateTime.now().minusMinutes(30);
		for(Alarm alarm : paramList) {
			boolean isAlert = alarm.getIsread() == 0 ? false : true;
			LocalDateTime createdAt = LocalDateTime.parse(alarm.getAlarmtime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			
			if(createdAt.isBefore(beforeTime)||isAlert) {
				continue;
			}
			
			alertList.add(alarm);
		}
		return alertList;
	}
	
	private void updateIsAlert(List<Integer> alertIds) {
		for(int i : alertIds) {
			try {
				new UserService().updateAlarmByAlarmId(i);
			} catch (Exception e) {
				System.out.println("알림 업데이트 실패");
			}
		}
	}
}
