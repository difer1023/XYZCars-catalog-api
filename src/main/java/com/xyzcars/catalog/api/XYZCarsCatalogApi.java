package com.xyzcars.catalog.api;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;

/**
 * @author difer
 */
public interface XYZCarsCatalogApi {
	
	public JSONArray getAllProducts();
	
	public JSONObject getProductById(long productId);
	
	public JSONObject saveCustomerProductRequest(JSONObject productRequestDetails);
}