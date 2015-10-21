package com.freemit.ticker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import nl.strohalm.cyclos.webservices.rest.BaseRestController;

public class TickerVO extends BaseRestController implements Serializable {
	private static final long serialVersionUID = -4008681065754281671L;
	
	public final float rate;
	public final Calendar timestamp;
	
	public TickerVO(BigDecimal rate, Calendar timestamp) {
		this.rate = rate.floatValue();
		this.timestamp = timestamp;
	}
	
}
