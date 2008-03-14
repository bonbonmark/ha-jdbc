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
package net.sf.hajdbc.dialect;

import java.sql.SQLException;

import net.sf.hajdbc.Dialect;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Paul Ferraro
 */
@SuppressWarnings("nls")
public class TestMckoiDialect extends TestStandardDialect
{
	@Override
	protected Dialect createDialect()
	{
		return new MckoiDialect();
	}

	@Override
	@Test(dataProvider = "insert-table-sql")
	public String parseInsertTable(String sql) throws SQLException
	{
		this.replay();
		
		String table = this.dialect.parseInsertTable(sql);
		
		this.verify();

		assert table == null : table;

		return table;
	}

	@Override
	@DataProvider(name = "sequence-sql")
	Object[][] sequenceSQLProvider()
	{
		return new Object[][] {
			new Object[] { "SELECT NEXTVAL('success')" },
			new Object[] { "SELECT NEXTVAL ( 'success' )" },
			new Object[] { "SELECT CURRVAL('success')" },
			new Object[] { "SELECT CURRVAL ( 'success' )" },
			new Object[] { "SELECT NEXTVAL('success'), * FROM table" },
			new Object[] { "SELECT NEXTVAL ( 'success' ) , * FROM table" },
			new Object[] { "SELECT CURRVAL('success'), * FROM table" },
			new Object[] { "SELECT CURRVAL ( 'success' ) , * FROM table" },
			new Object[] { "INSERT INTO table VALUES (NEXTVAL('success'), 0)" },
			new Object[] { "INSERT INTO table VALUES (NEXTVAL ( 'success' ) , 0)" },
			new Object[] { "INSERT INTO table VALUES (CURRVAL('success'), 0)" },
			new Object[] { "INSERT INTO table VALUES (CURRVAL ( 'success' ) , 0)" },
			new Object[] { "UPDATE table SET id = NEXTVAL('success')" },
			new Object[] { "UPDATE table SET id = NEXTVAL ( 'success' )" },
			new Object[] { "UPDATE table SET id = CURRVAL('success')" },
			new Object[] { "UPDATE table SET id = CURRVAL ( 'success' )" },
			new Object[] { "SELECT * FROM table" },
		};
	}
}
