package com.htwk.app.repository;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.EvictingQueue;
import com.google.common.util.concurrent.AtomicLongMap;

@Repository
public class StatisticRepository {

	private AtomicLong totalRequests;
	
	private AtomicLong staffRequests;
	private AtomicLong buildingRequests;
	private AtomicLong sportRequests;
	private AtomicLong newsRequests;
	private AtomicLongMap<Integer> mensaRequests;
	private AtomicLong mailboxRequests;
	private AtomicLong weatherRequests;
	private AtomicLong timetableRequests;

	private AtomicLong errorRequests;
	private AtomicLongMap<RequestMethod> requestType;

	private EvictingQueue<Long> executionTimes;

	@PostConstruct
	public void init() {
		totalRequests = new AtomicLong(0);
		staffRequests = new AtomicLong(0);
		buildingRequests = new AtomicLong(0);
		sportRequests = new AtomicLong(0);
		newsRequests = new AtomicLong(0);
		mailboxRequests = new AtomicLong(0);
		weatherRequests = new AtomicLong(0);
		timetableRequests = new AtomicLong(0);
		mensaRequests = AtomicLongMap.create();
		errorRequests = new AtomicLong(0);
		requestType = AtomicLongMap.create();
		executionTimes = EvictingQueue.create(10000);
	}

	public long incrementStaffRequest() {
		return this.staffRequests.incrementAndGet();
	}

	public long incrementBuildingRequest() {
		return this.buildingRequests.incrementAndGet();
	}

	public long incrementSportRequest() {
		return this.sportRequests.incrementAndGet();
	}

	public long incrementNewsRequest() {
		return this.newsRequests.incrementAndGet();
	}

	public long incrementMailBoxRequest() {
		return this.mailboxRequests.incrementAndGet();
	}

	public long incrementWeatherRequest() {
		return this.weatherRequests.incrementAndGet();
	}

	public long incrementTimetableRequest() {
		return this.timetableRequests.incrementAndGet();
	}

	public long incrementMensaRequest(int location) {
		return this.mensaRequests.incrementAndGet(location);
	}

	public long incrementErrorRequest() {
		return this.errorRequests.incrementAndGet();
	}

	public long incrementTotalRequests() {
		return this.totalRequests.incrementAndGet();
	}

	public long incrementRequestType(RequestMethod type) {
		return this.requestType.incrementAndGet(type);
	}
	

	public void addExecutionTime(long executionTime) {
		totalRequests.incrementAndGet();
		this.executionTimes.add(executionTime);
	}

	/**
	 * @return the staffRequests
	 */
	public synchronized final AtomicLong getStaffRequests() {
		return staffRequests;
	}

	/**
	 * @return the buildingRequests
	 */
	public synchronized final AtomicLong getBuildingRequests() {
		return buildingRequests;
	}

	/**
	 * @return the sportRequests
	 */
	public synchronized final AtomicLong getSportRequests() {
		return sportRequests;
	}

	/**
	 * @return the newsRequests
	 */
	public synchronized final AtomicLong getNewsRequests() {
		return newsRequests;
	}

	/**
	 * @return the mensaRequests
	 */
	public synchronized final AtomicLongMap<Integer> getMensaRequests() {
		return mensaRequests;
	}

	/**
	 * @return the mailboxRequests
	 */
	public synchronized final AtomicLong getMailboxRequests() {
		return mailboxRequests;
	}

	/**
	 * @return the weatherRequests
	 */
	public synchronized final AtomicLong getWeatherRequests() {
		return weatherRequests;
	}

	/**
	 * @return the timetableRequests
	 */
	public synchronized final AtomicLong getTimetableRequests() {
		return timetableRequests;
	}

	/**
	 * @return the errorRequests
	 */
	public synchronized final AtomicLong getErrorRequests() {
		return errorRequests;
	}

	/**
	 * @return the requestType
	 */
	public synchronized final AtomicLongMap<RequestMethod> getRequestType() {
		return requestType;
	}

	/**
	 * @return the executionTimes
	 */
	public synchronized final EvictingQueue<Long> getExecutionTimes() {
		return executionTimes;
	}

	/**
	 * @return the executionTimes
	 */
	public synchronized final AtomicLong getTotalRequests() {
		return totalRequests;
	}
}
