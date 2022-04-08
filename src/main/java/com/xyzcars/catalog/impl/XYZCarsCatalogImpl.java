package com.xyzcars.catalog.impl;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.xyzcars.catalog.api.XYZCarsCatalogApi;
import com.xyzcars.catalog.config.XYZCarsCatalogConfiguration;
import com.xyzcars.catalog.exception.ServiceConsumptionException;
import com.xyzcars.catalog.rest.RestServiceConsumer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

@Component(service = XYZCarsCatalogApi.class, configurationPid = "com.xyz.cars.catalog.XYZCarsCatalogConfiguration")
public class XYZCarsCatalogImpl implements XYZCarsCatalogApi {

	private static Log LOGGER = LogFactoryUtil.getLog(XYZCarsCatalogImpl.class);

	@Override
	public JSONArray getAllProducts() {
		try {
			return (JSONArray) restServiceConsumer.consumeServiceByGet(xyzCarsCatalogConfiguration.productsServiceUrl(),
					JSONFactoryUtil.createJSONArray());
		} catch (ServiceConsumptionException e) {
			LOGGER.error(e);
			return JSONFactoryUtil.createJSONArray();
		}
	}

	@Override
	public JSONObject getProductById(long productId) {
		try {
			return (JSONObject) restServiceConsumer.consumeServiceByGet(
					String.format(xyzCarsCatalogConfiguration.productDetailServiceUrl(), productId),
					JSONFactoryUtil.createJSONObject());
		} catch (ServiceConsumptionException e) {
			LOGGER.error(e);
			return JSONFactoryUtil.createJSONObject();
		}
	}

	@Override
	public JSONObject saveCustomerProductRequest(JSONObject productRequestDetails) {
		try {
			StringBuilder urlBuilder = new StringBuilder(
					xyzCarsCatalogConfiguration.customerProductRequestServiceUrl());
			urlBuilder.append(StringPool.QUESTION).append("productId").append(StringPool.EQUAL)
					.append(productRequestDetails.getString("productId")).append(StringPool.AMPERSAND)
					.append("customerName").append(StringPool.EQUAL)
					.append(productRequestDetails.getString("customerName")).append(StringPool.AMPERSAND)
					.append("customerLastName").append(StringPool.EQUAL)
					.append(productRequestDetails.getString("customerLastName")).append(StringPool.AMPERSAND)
					.append("customerQuestions").append(StringPool.EQUAL)
					.append(URLEncoder.encode(productRequestDetails.getJSONArray("customerQuestions").join(StringPool.COMMA), StandardCharsets.UTF_8.toString()));
			return restServiceConsumer.consumeServiceByPost(urlBuilder.toString());
		} catch (ServiceConsumptionException | JSONException | UnsupportedEncodingException e) {
			LOGGER.error(e);
			return JSONFactoryUtil.createJSONObject();
		}
	}

	@Reference
	private RestServiceConsumer restServiceConsumer;

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		xyzCarsCatalogConfiguration = ConfigurableUtil.createConfigurable(XYZCarsCatalogConfiguration.class,
				properties);
	}

	private volatile XYZCarsCatalogConfiguration xyzCarsCatalogConfiguration;

}
