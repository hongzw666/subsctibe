package com.hong.weixin.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.domain.ResponseError;
import com.hong.domain.ResponseMessage;
import com.hong.domain.ResponseToken;

@Service
public class TokenManagerImpl implements TokenManager {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public String getToken(String account) {
		
		String appid = "wx250bebfedb705aa0";
		String appsecret = "64d8f905d9833d473d51d62851d99cbf";

		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"//
				+ "&appid=" + appid//
				+ "&secret=" + appsecret;

		HttpClient hc = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)//HTTP的协议版本号
				.build();
		HttpRequest request = HttpRequest.newBuilder(URI.create(url))
				.GET()// 发送GET请求
				.build();

		ResponseMessage rm;
		try {
			
			HttpResponse<String> response = hc.send(request, BodyHandlers.ofString(Charset.forName("UTF-8")));

			String body = response.body();

			if (body.contains("errcode")) {
				// 出现了错误
				rm = objectMapper.readValue(body, ResponseError.class);
				rm.setStatus(2);
			} else {
				// 成功
				rm = objectMapper.readValue(body, ResponseToken.class);
				rm.setStatus(1);
			}
			// return rm;
			if (rm.getStatus() == 1) {
				return ((ResponseToken) rm).getAccessToken();
			}
		} catch (Exception e) {
			throw new RuntimeException("无法获取令牌，因为：" + e.getLocalizedMessage());
		}

		throw new RuntimeException("无法获取令牌，因为：错误代码=" //
				+ ((ResponseError) rm).getErrorCode() //
				+ "错误描述=" + ((ResponseError) rm).getErrorMessage());
	}
}
