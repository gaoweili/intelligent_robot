package com.intelligent_robot.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.intelligent_robot.bean.ChatMessage;
import com.intelligent_robot.bean.Result;
import com.intelligent_robot.bean.ChatMessage.Type;

public class HttpUtils {

	private static final String URL = "http://www.tuling123.com/openapi/api";
	private static final String API_KEY = "7edbd2826722eda085e611ce2fc65256";

	/**
	 * 得到聊天消息
	 * 
	 * @param msg
	 * @return
	 */
	public static ChatMessage sendMessage(String msg) {
		ChatMessage chatMsg = new ChatMessage();
		String jsonResult = doGet(msg);
		Gson gson = new Gson();
		Result result = null;
		try {
			result = gson.fromJson(jsonResult, Result.class);
			chatMsg.setMsg(result.getText());
		} catch (JsonSyntaxException e) {
			chatMsg.setMsg("服务器繁忙，请稍后再试");
		}
		chatMsg.setType(Type.InComing);
		chatMsg.setDate(new java.util.Date());
		return chatMsg;
	}

	private static String doGet(String msg) {
		String result = "";
		String url = setParams(msg);
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			java.net.URL urlNet = new java.net.URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlNet
					.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			is = conn.getInputStream();
			int len = -1;
			byte[] bytes = new byte[128];
			baos = new ByteArrayOutputStream();
			while ((len = is.read(bytes)) != -1) {
				baos.write(bytes, 0, len);
			}
			baos.flush();
			result = new String(baos.toByteArray());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static String setParams(String msg) {
		String url = "";
		try {
			url = URL + "?key=" + API_KEY + "&info="
					+ URLEncoder.encode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

}
