/*
 * $Id: EdgeEvent.java 1807 2010-02-05 22:20:02Z scott $
 * Copyright (C) 2007 Scott Martin
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version. The GNU Lesser General Public License is
 * distributed with this software in the file COPYING.
 */
package edu.osu.ling.pep;


/**
 * An event generated when an {@link Edge edge} is added to a
 * {@link Chart chart} in {@link EarleyParser#parse(Iterable, Category) 
 * Earley parsing}. 
 * @author <a href="http://www.ling.osu.edu/~scott/">Scott Martin</a>
 * @version $LastChangedRevision: 1807 $
 * @see Chart
 * @see EarleyParser
 * @see ParserListener
 */
public class EdgeEvent extends ParserEvent {
	private static final long serialVersionUID = 1L;
	
	Edge edge;
	Integer index;
	
	/**
	 * Creates a new edge event generated by the specified parser for the
	 * given edge at the specified index. 
	 * @param edge The edge whose addition generated this event.
	 * @param index The string index where the event was generated.
	 * @see ParserEvent#EarleyParserEvent(EarleyParser, Integer)
	 */
	EdgeEvent(EarleyParser earleyParser, Integer index, Edge edge) {
		super(earleyParser);
		this.edge = edge;
		this.index = index;
	}
	
	/**
	 * Gets the edge whose addition generated this event.
	 * @return The edge specified when this event was created.
	 */
	public Edge getEdge() {
		return edge;
	}

	/**
	 * Gets the index where this event occurred.
	 * @return The index.
	 */
	public Integer getIndex() {
		return index;
	}
}
