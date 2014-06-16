package com.htwk.app.utils;

public enum PushStatus {
	PUSH_REQUEST(1000), NEW_NEWS(2000), NEW_MAILS(4001), OTHER(1000);
	private final int status;

	private PushStatus(int status) {
		this.status = status;
	}

	public int status() {
		return this.status;
	}

	public String toString() {
		return String.valueOf(status);
	}
}
