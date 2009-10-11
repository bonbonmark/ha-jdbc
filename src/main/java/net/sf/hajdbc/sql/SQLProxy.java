/*
 * HA-JDBC: High-Availability JDBC
 * Copyright (c) 2004-2007 Paul Ferraro
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your 
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Contact: ferraro@users.sourceforge.net
 */
package net.sf.hajdbc.sql;

import java.util.Map;
import java.util.Set;

import net.sf.hajdbc.Database;
import net.sf.hajdbc.DatabaseCluster;
import net.sf.hajdbc.ExceptionFactory;

/**
 * @author Paul Ferraro
 * @since 2.0
 */
public interface SQLProxy<Z, D extends Database<Z>, T, E extends Exception>
{
	public DatabaseCluster<Z, D> getDatabaseCluster();
	
	public Set<Map.Entry<D, T>> entries();
	
	public void addChild(SQLProxy<Z, D, ?, ?> child);

	public void removeChild(SQLProxy<Z, D, ?, ?> child);
	
	public void removeChildren();

	public SQLProxy<Z, D, ?, ? extends Exception> getRoot();
	
	public T getObject(D database);
	
	public void retain(Set<D> databaseSet);
	
	public ExceptionFactory<E> getExceptionFactory();
}