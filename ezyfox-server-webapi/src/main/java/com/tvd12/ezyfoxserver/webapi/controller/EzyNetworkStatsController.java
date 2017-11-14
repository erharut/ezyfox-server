package com.tvd12.ezyfoxserver.webapi.controller;

import java.util.function.Function;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvd12.ezyfoxserver.databridge.statistics.EzyNetworkPoint;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkRoStats;
import com.tvd12.ezyfoxserver.statistics.EzySocketStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyWebSocketStatistics;

@RestController
@RequestMapping("admin/network-stats")
public class EzyNetworkStatsController extends EzyController {

	@GetMapping("/total-read-bytes")
	public long getTotalReadBytes() {
		return sumStatistics(stats -> stats.getReadBytes());
	}
	
	@GetMapping("/read-bytes-per-hour")
	public long getReadBytesPerHour() {
		return sumStatistics(stats -> stats.getReadBytesPerHour());
	}
	
	@GetMapping("/read-bytes-per-minute")
	public long getReadBytesPerMinute() {
		return sumStatistics(stats -> stats.getReadBytesPerMinute());
	}
	
	@GetMapping("/read-bytes-per-second")
	public long getReadBytesPerSecond() {
		return sumStatistics(stats -> stats.getReadBytesPerSecond());
	}
	
	@GetMapping("/written-bytes-per-hour")
	public long getWrittenBytesPerHour() {
		return sumStatistics(stats -> stats.getWrittenBytesPerHour());
	}
	
	@GetMapping("/written-bytes-per-minute")
	public long getWrittenBytesPerMinute() {
		return sumStatistics(stats -> stats.getWrittenBytesPerMinute());
	}
	
	@GetMapping("/written-bytes-per-second")
	public long getWrittenBytesPerSecond() {
		return sumStatistics(stats -> stats.getWrittenBytesPerSecond());
	}
	
	@GetMapping("/read-written-bytes-per-hour")
	public EzyNetworkPoint getReadWrittenBytesPerHour() {
		return getNetworkPoint(
				stats -> stats.getReadBytesPerHour(),
				stats -> stats.getWrittenBytesPerHour());
	}
	
	@GetMapping("/read-written-bytes-per-minute")
	public EzyNetworkPoint getReadWrittenBytesPerMinute() {
		return getNetworkPoint(
				stats -> stats.getReadBytesPerMinute(),
				stats -> stats.getWrittenBytesPerMinute());
	}
	
	@GetMapping("/read-written-bytes-per-second")
	public EzyNetworkPoint getReadWrittenBytesPerSecond() {
		return getNetworkPoint(
				stats -> stats.getReadBytesPerSecond(),
				stats -> stats.getWrittenBytesPerSecond());
	}
	
	protected long sumStatistics(Function<EzyNetworkRoStats, Long> function) {
		EzySocketStatistics socketStats = getSocketStatistics();
		EzyWebSocketStatistics webSocketStats = getWebSocketStatistics();
		EzyNetworkRoStats socketSetworkStats = socketStats.getNetworkStats();
		EzyNetworkRoStats webSocketNetworkStats = webSocketStats.getNetworkStats();
		long sum = function.apply(socketSetworkStats) + function.apply(webSocketNetworkStats);
		return sum;
	}
	
	protected EzyNetworkPoint getNetworkPoint(
			Function<EzyNetworkRoStats, Long> readFunction,
			Function<EzyNetworkRoStats, Long> writtenFunction) {
		EzySocketStatistics socketStats = getSocketStatistics();
		EzyWebSocketStatistics webSocketStats = getWebSocketStatistics();
		EzyNetworkRoStats socketSetworkStats = socketStats.getNetworkStats();
		EzyNetworkRoStats webSocketNetworkStats = webSocketStats.getNetworkStats();
		long sumRead = readFunction.apply(socketSetworkStats) 
				+ readFunction.apply(webSocketNetworkStats);
		long sumWritten = writtenFunction.apply(socketSetworkStats) 
				+ writtenFunction.apply(webSocketNetworkStats);
		EzyNetworkPoint point = new EzyNetworkPoint();
		point.setInputBytes(sumRead);
		point.setOutputBytes(sumWritten);
		return point;
	}
	
	protected EzyStatistics getStatistics() {
		return getServer().getStatistics();
	}
	
	protected EzySocketStatistics getSocketStatistics() {
		return getStatistics().getSocketStats();
	}
	
	protected EzyWebSocketStatistics getWebSocketStatistics() {
		return getStatistics().getWebSocketStats();
	}
}
