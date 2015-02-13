package com.gzfgeh.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

public class OperationData {
	
	public static String getData(byte[] jsonByte)
            throws JSONException {
        
        String message = String.valueOf(jsonByte);
        JSONObject object = new JSONObject(message);
        String status = object.getString("status");
        
        return status;
    }

	public static byte[] sendData(String command,HashMap<String, Object> map)
			throws Exception{
		
		String content;
		JSONObject serverValue = new JSONObject();
		Iterator<Entry<String, Object>> iterable = map.entrySet().iterator();
		while (iterable.hasNext()){
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterable.next();
			serverValue.put(entry.getKey(), entry.getValue());
		}
		content = String.valueOf(serverValue);
		JSONObject serverKey = new JSONObject();
		serverKey.put(command, content);
		return (String.valueOf(serverKey) + "\r\n").getBytes("UTF-8");
	}
}
