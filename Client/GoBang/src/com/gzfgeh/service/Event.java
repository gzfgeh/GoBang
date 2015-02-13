package com.gzfgeh.service;

import java.util.HashMap;

public class Event {
	public static class RegisterEvent{
		public HashMap<String, Object> message;
	}
	
	public static class LoginEvent{
		public HashMap<String, Object> message;
	}
	
	public static class CommandEvent{
		public HashMap<String, Object> message;
	}
	
	public static class SendRegister{
		public String message;
	}
	
	public static class SendLogin{
		public String message;
	}

	public static class SendCommand{
		public String message;
	}

}
