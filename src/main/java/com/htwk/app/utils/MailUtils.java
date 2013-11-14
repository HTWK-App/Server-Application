package com.htwk.app.utils;


public class MailUtils {

	public static String decryptMail(String mail){
		return decryptMailAdress(mail, 3);
	}
	
	private static String decryptMailAdress(String enc, int offset) {
		String dec = "";
		int len = enc.length();
		for (int i = 0; i < len; i++) {
			char n = enc.charAt(i);
			if (n >= 0x2B && n <= 0x3A) {
				dec += decryptCharcode(n, 0x2B, 0x3A, offset);
			} else if (n >= 0x40 && n <= 0x5A) {
				dec += decryptCharcode(n, 0x40, 0x5A, offset);
			} else if (n >= 0x61 && n <= 0x7A) {
				dec += decryptCharcode(n, 0x61, 0x7A, offset);
			} else {
				dec += enc.charAt(i);
			}
		}
		return dec;
	}

	private static char decryptCharcode(char c, int start, int end, int offset) {
		int n= (int) c;
		n = n + offset;
		if (offset > 0 && n > end) {
			n = start + (n - end - 1);
		} else if (offset < 0 && n < start) {
			n = end - (start - n - 1);
		}
		return (char)n;
	}
	
}
