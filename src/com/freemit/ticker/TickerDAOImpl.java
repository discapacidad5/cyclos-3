package com.freemit.ticker;


import nl.strohalm.cyclos.dao.BaseDAO;
import nl.strohalm.cyclos.dao.BaseDAOImpl;
import nl.strohalm.cyclos.entities.exceptions.DaoException;
import nl.strohalm.cyclos.entities.exceptions.UnexpectedEntityException;

public class TickerDAOImpl extends BaseDAOImpl<TickerEntity> implements TickerDAO {

	private static TickerEntity current;
	private static TickerVO currentVo;

	/**@return the current ticker or null if polling has not started.
	 * @see {@linkplain #getCurrent()} - non-static method attempts to recover last Ticker from DB if polling is not started*/   
	public static TickerEntity get() {
		return current;
	}
	
    public TickerDAOImpl() {
        super(TickerEntity.class);
    }

	/**@param entityClass */
	public TickerDAOImpl(Class<TickerEntity> entityClass) {
		super(entityClass);
	}

	/**Wrapper around {@link BaseDAO#insert()} that keeps a current ticker available to services in memory
	 * @param ticker to insert
	 * @return the saved ticker 
	 * @throws DaoException if any problem happens in the underlying implementation  
	 * @throws UnexpectedEntityException If the entity is null or persistent */
	TickerEntity save(TickerEntity ticker) throws UnexpectedEntityException, DaoException {
		if (current == null) {
			logger.info("Current Ticker initializing at " + ticker.quote());
		}
		current = insert(ticker);
		currentVo = new TickerVO(current.rate, current.timestamp);
		return current;
	}

	@Override
	public TickerEntity getCurrent() {
		if (current != null) {
			return current; // TODO check timestamp for 'stale' Ticker?
		}
		logger.info("getting last Ticker from DB...");
		current = uniqueResult("from " + getEntityType().getName() + " order by id DESC", null);
		logger.info("    Success: " + current);
		return current;
	}

	@Override
	public TickerVO getCurrentVO() {
		if (currentVo != null) {
			return currentVo;
		}
		getCurrent();
		return currentVo;
	}
	
}
