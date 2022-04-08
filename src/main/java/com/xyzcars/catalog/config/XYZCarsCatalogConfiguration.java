package com.xyzcars.catalog.config;

import com.liferay.petra.string.StringPool;
import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(id = "com.xyz.cars.catalog.XYZCarsCatalogConfiguration")
public interface XYZCarsCatalogConfiguration {
	@Meta.AD(
        deflt = StringPool.BLANK,
        required = false
    )
    public String productsServiceUrl();
	
	@Meta.AD(
	        deflt = StringPool.BLANK,
	        required = false
	    )
	    public String productDetailServiceUrl();
	
	@Meta.AD(
        deflt = StringPool.BLANK,
        required = false
    )
    public String customerProductRequestServiceUrl();
}
