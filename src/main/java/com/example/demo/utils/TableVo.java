package com.example.demo.utils;

import java.util.List;

public class TableVo {
	private String tableName;
	private String tableExplain;
	private String pk;
	private String index;
	private List<ColumnsVo> clist;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableExplain() {
		return tableExplain;
	}
	public void setTableExplain(String tableExplain) {
		this.tableExplain = tableExplain;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public List<ColumnsVo> getClist() {
		return clist;
	}
	public void setClist(List<ColumnsVo> clist) {
		this.clist = clist;
	}
}
