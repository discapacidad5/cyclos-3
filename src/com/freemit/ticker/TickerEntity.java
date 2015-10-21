package com.freemit.ticker;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.persistence.Column;

import nl.strohalm.cyclos.entities.Entity;


public class TickerEntity extends Entity {
	private static final long serialVersionUID = 157155306122918977L;

	static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("MM/dd HH:mm.ss");
	static final NumberFormat DOLLAR_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

	@Column BigDecimal rate;
	@Column Calendar timestamp;
	
	// TODO @OneToOne currency; // USD or GBP/JPY/EUR/etc
	
	public TickerEntity() {
	}
	
	public TickerEntity(BigDecimal rate) {
		this.rate = rate;
		timestamp = Calendar.getInstance();
	}

	public BigDecimal getRate() {
		return rate;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}
	void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	/** pretty print the exchange rate: $XX.XX/BTC*/
	public String quote() {
		return DOLLAR_FORMAT.format(rate) + " / BTC";
	}

	@Override
	public String toString() {
		return new StringBuffer(quote()).append(" at ").append(DATE_TIME.format(timestamp.getTime()))
				.append(" ticker.id:").append(getId()).toString();
	}

}
