/*
 * HA-JDBC: High-Availability JDBC
 * Copyright (C) 2004 Paul Ferraro
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
package net.sf.hajdbc.synch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author  Paul Ferraro
 * @version $Revision$
 * @since   1.0
 */
public class Index
{
	private String name;
	private String table;
	private List columnList = new LinkedList();
	
	private String formatSQL(String pattern)
	{
		StringBuffer buffer = new StringBuffer();
		Iterator columns = this.columnList.iterator();
		
		while (columns.hasNext())
		{
			String column = (String) columns.next();
			buffer.append(column);
			
			if (columns.hasNext())
			{
				buffer.append(",");
			}
		}
			
		return MessageFormat.format(pattern, new Object[] { this.name, this.table, buffer.toString() });
	}
	
	public boolean equals(Object object)
	{
		Index index = (Index) object;
		
		return (index != null) && (index.name != null) && index.name.equals(this.name);
	}
	
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	public static Collection getIndexes(Connection connection, String table) throws SQLException
	{
		Map indexMap = new HashMap();
		ResultSet resultSet = connection.getMetaData().getIndexInfo(null, null, table, false, true);
		
		while (resultSet.next())
		{
			if (resultSet.getBoolean("NON_UNIQUE"))
			{
				String name = resultSet.getString("INDEX_NAME");
				String column = resultSet.getString("COLUMN_NAME");
				
				Index index = (Index) indexMap.get(name);
				
				if (index == null)
				{
					index = new Index();
					
					index.name = name;
					index.table = table;
					
					indexMap.put(name, index);
				}
				
				index.columnList.add(column);
			}
		}
		
		resultSet.close();
		
		return indexMap.values();
	}
	
	public static void executeSQL(Connection connection, Collection indexCollection, String sqlPattern) throws java.sql.SQLException
	{
		Statement statement = connection.createStatement();
		
		Iterator indexes = indexCollection.iterator();
		
		while (indexes.hasNext())
		{
			Index index = (Index) indexes.next();
			
			String sql = index.formatSQL(sqlPattern);
			
			statement.addBatch(sql);
		}

		statement.executeBatch();
		statement.close();
	}
}
