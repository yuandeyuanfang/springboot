/**
 * Project Name:lb-base
 * File Name:TableColumn.java
 * Package Name:com.lb.base.util
 * Date:2017-3-28上午9:22:24
 * Copyright (c) 2017, chenliang@erayt.com All Rights Reserved.
 *
*/

package com.example.demo.utils;
/**
 * ClassName:TableColumn <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017-3-28 上午9:22:24 <br/>
 * @author   lb-pc
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class TableColumn {
	
	private String tableName;
	private String tableComments;
	private String columnName;
	private String columnComments;
	private String dataType;
	private String dataLength;
	private String nullable;
	
	private String columnid;
	private String precision;
	private String scale;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableComments() {
		return tableComments;
	}
	public void setTableComments(String tableComments) {
		this.tableComments = tableComments;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnComments() {
		return columnComments;
	}
	public void setColumnComments(String columnComments) {
		this.columnComments = columnComments;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataLength() {
		return dataLength;
	}
	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}
	public String getNullable() {
		return nullable;
	}
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
	public String getColumnid() {
		return columnid;
	}
	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	
}
