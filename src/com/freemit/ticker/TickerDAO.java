package com.freemit.ticker;

import nl.strohalm.cyclos.dao.BaseDAO;

public interface TickerDAO extends BaseDAO<TickerEntity> /*, InsertableDAO<TickerEntity> */{

	/**@return the most recently polled Ticker, if available, or attempts to get the most recent from the DB */
	TickerEntity getCurrent();

	TickerVO getCurrentVO();
	
	// TODO ArrayList<Ticker> queryForTimeRange(Calendar start, Calendar end);
	
	// TODO Ticker closest(Calendar timestamp);

	// TODO Currency support
}
