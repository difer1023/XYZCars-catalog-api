package com.xyzcars.catalog.rest;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.xyzcars.catalog.exception.ServiceConsumptionException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.osgi.service.component.annotations.Component;

@Component(service=RestServiceConsumer.class)
public class RestServiceConsumer {

	@SuppressWarnings("unchecked")
	public <T extends Object> T consumeServiceByGet(String url, T returnType) throws ServiceConsumptionException {
		
		try {
			switch (returnType.getClass().getName()) {
			case "com.liferay.portal.json.JSONObjectImpl":
				return (T) JSONFactoryUtil.createJSONObject(consumeRestServiceByGet(url));
			case "com.liferay.portal.json.JSONArrayImpl":
				return (T) JSONFactoryUtil.createJSONArray(consumeRestServiceByGet(url));
			default:
				throw new ServiceConsumptionException("Error in consumeServiceByGet");
			}
			
		} catch (JSONException | IOException | InterruptedException e) {
			throw new ServiceConsumptionException("Error in consumeServiceByGet");
		}
		
	}
	
	public JSONObject consumeServiceByPost(String url) throws ServiceConsumptionException {
		try {
			return JSONFactoryUtil.createJSONObject(consumeRestServiceByPost(url));
		} catch (JSONException | IOException | InterruptedException e) {
			throw new ServiceConsumptionException("Error in consumeServiceByPost");
		}
		
	}
	
	private String consumeRestServiceByGet(String url) throws IOException, InterruptedException {

		HttpClient httpClient = HttpClient.newBuilder().build();

		HttpRequest httpRequest = HttpRequest.newBuilder()
		            .GET()
		            .uri(URI.create(url))
		            .build();

		HttpResponse<String> response = httpClient
					.send(httpRequest, BodyHandlers.ofString());
		return response.body();
	}
	
	private String consumeRestServiceByPost(String url) throws IOException, InterruptedException {

		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest httpRequest = HttpRequest.newBuilder()
					.headers("Content-Type", "text/plain;charset=UTF-8")
		            .POST(HttpRequest.BodyPublishers.ofString(StringPool.BLANK))
		            .uri(URI.create(url))
		            .build();

		HttpResponse<String> response = httpClient
					.send(httpRequest, BodyHandlers.ofString());
		return response.body();
	}
}
