package com.freemit;

import javax.naming.OperationNotSupportedException;

import nl.strohalm.cyclos.entities.accounts.Currency;
import nl.strohalm.cyclos.utils.EntityWrapper;

public enum Currencies implements EntityWrapper {
	BTC(1, "Bitcoin", "#amount# BTC"), 
	USD(2, "U.S. Dollars", "$#amount#"), 
	EUR(3, "Euros", "€#amount#"), 
	CNY(4, "Renminbi", "¥#amount#");
	
    private Currencies(int id, final String name, final String pattern) {
    	this.id = id;
        this.name = name;
        this.pattern = pattern;
    	this.symbol = this.name();
    }
    
    /** the id of this currency in the DB */
    public final long		  id;
    
    public final String       name;
    public final String       symbol;
    public final String       pattern;
    private Currency currency;

    public void setEntity(Currency currency) throws OperationNotSupportedException {
		if (!create().equals(currency)) {
    		throw new OperationNotSupportedException(id + ":" + this + " vs. " + currency);
		}
    	this.currency = currency;
    }

    /**@return a new Currency Entity based on the fields of this enum */
    public Currency create() {
		Currency result = new Currency();
		result.setId(id);
		result.setName(name);
        result.setPattern(pattern);
        result.setSymbol(symbol);
        return result;
    }
    
    /**@return the actual DB entity */
    public Currency getEntity() {
    	if (currency == null) { // before DB initialization
    		return create();
    	}
    	return currency;
    }
    
    @Override
    public String toString() {
    	return name + "(" + symbol + ")";
    }
}
