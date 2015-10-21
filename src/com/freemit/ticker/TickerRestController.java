package com.freemit.ticker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import nl.strohalm.cyclos.webservices.rest.BaseRestController;

@Controller
public class TickerRestController extends BaseRestController {

	private TickerDAO tickerDao;
	public void setTickerDao(TickerDAO tickerDao) {
		this.tickerDao = tickerDao;
	}
	
    @RequestMapping(value = "ticker", method = RequestMethod.GET)
	@ResponseBody
	public TickerVO getTicker() {
    	 return tickerDao.getCurrentVO();
	}
    
}
