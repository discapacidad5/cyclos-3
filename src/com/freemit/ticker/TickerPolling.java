package com.freemit.ticker;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.strohalm.cyclos.annotations.Inject;
import nl.strohalm.cyclos.scheduling.polling.PollingTask;

public class TickerPolling extends PollingTask {
	private static final Logger log = LoggerFactory.getLogger(TickerPolling.class);
	
    private final HttpClient client = new HttpClient();

    /** btc-e and bitstamp do not accept Java User-Agent, mask to Chrome */
    Header USER_AGENT = new Header("User-Agent", 
    		"Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36");
    static final String STAMP_URL = "https://www.bitstamp.net/api/ticker/";
    static final String BTCE_URL = "https://btc-e.com/api/2/btc_usd/ticker";
    static final String FINEX_URL = "https://api.bitfinex.com/v1/pubticker/btcusd";

	TickerDAOImpl tickerDao;
	@Inject public void setTickerDao(TickerDAO tickerDao) {
		this.tickerDao = (TickerDAOImpl)tickerDao;
	}
	
	@Override
	protected boolean runTask() {
    	double total = 0;
    	int count = 0;
    	GetMethod get;

    	get = new GetMethod(FINEX_URL);
    	try {
    		double finex = quoteBitfinex(get);
    		if (finex < 1) {
    			throw new Exception("invalid quote: " + finex);
    		}
			total += finex;
			count++;
    	} catch (Exception e) {
    		log.warn(e.getMessage() + " Bitfinex");
    	} finally {
    		get.releaseConnection();
    	}

    	get = new GetMethod(STAMP_URL);
        try {
        	double stamp = quoteBitstamp(get);
        	if (stamp < 1) {
        		throw new Exception("invalid quote: " + stamp);
        	}
        	total += stamp;
        	count++;
    	} catch (Exception e) {
    		log.warn(e.getMessage() + " Bitstamp");
    	} finally {
    		get.releaseConnection();
    	}

        get = new GetMethod(BTCE_URL);
        try {
        	double btce = quoteBtce(get);
        	if (btce < 1) {
        		throw new Exception("invalid quote: " + btce);
        	}
        	total += btce;
        	count++;
    	} catch (Exception e) {
    		log.warn(e.getMessage() + " BTC-e");
    	} finally {
    		get.releaseConnection();
    	}
    	
        if (count == 0) {
        	log.error("No Ticker responses");
        	return false;
        }
    	
        // add to database
        try {
        	BigDecimal value = new BigDecimal(total / count).setScale(2, RoundingMode.HALF_DOWN);
        	tickerDao.save(new TickerEntity(value));
        	TickerEntity current = tickerDao.save(new TickerEntity(value));
        	log.trace(current.toString());
        } catch (Throwable t) {
        	log.error(t.getMessage(), t);
        }
		return false; // wait for next polling
	}

    private double quoteBitfinex(GetMethod get) throws IOException {
        get.setRequestHeader(USER_AGENT);
        int code = client.executeMethod(get);
        if (code != 200) {
            throw new HttpException("Error Code " + code + " getting ticker.");// new String(get.getResponseBody())
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readValue(get.getResponseBody(), JsonNode.class);
        return root.path("mid").asDouble();
    }
    
    private double quoteBitstamp(GetMethod get) throws IOException {
        get.setRequestHeader(USER_AGENT);
        int code = client.executeMethod(get);
        if (code != 200) {
            throw new HttpException("Error Code " + code + " getting ticker.");// new String(get.getResponseBody())
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readValue(get.getResponseBody(), JsonNode.class);
        return root.path("last").asDouble();
    }
    
    private double quoteBtce(GetMethod get) throws IOException {
        get.setRequestHeader(USER_AGENT);
        int code = client.executeMethod(get);
        if (code != 200) {
            throw new HttpException("Error Code " + code + " getting ticker.");// new String(get.getResponseBody())
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readValue(get.getResponseBody(), JsonNode.class);
        return root.path("ticker").path("last").getDoubleValue();
    }

}
