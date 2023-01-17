package com.example.demo.utils;

public class ColumnsVo {
	private String columnName;
	private String columnExplain;
	private String columnType;
	private String length;
	private String nullable;
	
	
	private String table_schema;
	private String column_name;
	private String column_type;
	private String column_comment;
	private String is_key;
	private String is_index;
	private String column_key;
	private String is_nullable;
	
	
	public String getIs_nullable() {
		return is_nullable;
	}
	public void setIs_nullable(String is_nullable) {
		this.is_nullable = is_nullable;
	}
	public String getColumn_key() {
		return column_key;
	}
	public void setColumn_key(String column_key) {
		this.column_key = column_key;
	}
	public String getTable_schema() {
		return table_schema;
	}
	public void setTable_schema(String table_schema) {
		this.table_schema = table_schema;
	}
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	public String getColumn_type() {
		return column_type;
	}
	public void setColumn_type(String column_type) {
		this.column_type = column_type;
	}
	public String getColumn_comment() {
		return column_comment;
	}
	public void setColumn_comment(String column_comment) {
		this.column_comment = column_comment;
	}
	public String getIs_key() {
		return is_key;
	}
	public void setIs_key(String is_key) {
		this.is_key = is_key;
	}
	public String getIs_index() {
		return is_index;
	}
	public void setIs_index(String is_index) {
		this.is_index = is_index;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnExplain() {
		return columnExplain;
	}
	public void setColumnExplain(String columnExplain) {
		this.columnExplain = columnExplain;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getNullable() {
		return nullable;
	}
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
}
