package com.tvd12.ezyfoxserver.webapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.databridge.statistics.EzyTrafficDetails;
import com.tvd12.ezyfoxserver.statistics.EzySocketStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyUserStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyWebSocketStatistics;

@RestController
@RequestMapping("admin/traffic")
public class EzyTrafficController extends EzyStatisticsController {

	@GetMapping("/details")
	public EzyTrafficDetails getTrafficDetails() {
		EzyTrafficDetails details = new EzyTrafficDetails();
		EzyUserStatistics userStatistics = getUserStatistics();
		EzySocketStatistics socketStatistics = getSocketStatistics();
		EzyWebSocketStatistics webSocketStatistics = getWebSocketStatistics();
		
		details.setMaxUser(userStatistics.getMaxUsers());
		details.setTotalUser(userStatistics.getTotalUsers());
		
		details.setSocketMaxSession(socketStatistics.getSessionStats().getMaxSessions());
		details.setSocketTotalSession(socketStatistics.getSessionStats().getTotalSessions());
		details.setSocketTransferredInputData(socketStatistics.getNetworkStats().getReadBytes());
		details.setSocketTransferredOutputData(socketStatistics.getNetworkStats().getWrittenBytes());
		details.setSocketCurrentInputDataTransferRate(socketStatistics.getNetworkStats().getReadBytesPerSecond());
		details.setSocketCurrentOutputDataTransferRate(socketStatistics.getNetworkStats().getWrittenBytesPerSecond());
		
		details.setWebSocketMaxSession(webSocketStatistics.getSessionStats().getMaxSessions());
		details.setWebSocketTotalSession(webSocketStatistics.getSessionStats().getTotalSessions());
		details.setWebSocketTransferredInputData(webSocketStatistics.getNetworkStats().getReadBytes());
		details.setWebSocketTransferredOutputData(webSocketStatistics.getNetworkStats().getWrittenBytes());
		details.setWebSocketCurrentInputDataTransferRate(webSocketStatistics.getNetworkStats().getReadBytesPerSecond());
		details.setWebSocketCurrentOutputDataTransferRate(webSocketStatistics.getNetworkStats().getWrittenBytesPerSecond());
		return details;
	}
	
}
