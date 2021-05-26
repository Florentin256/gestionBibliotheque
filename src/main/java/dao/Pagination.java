package dao;

/**
 * Class contenant les paramètres de pagination
 * 
 * @param offset
 * 			numéro de page
 * @param limit
 * 			nombre de résultats par page
 * @param orderBy
 * 			nom de la colonne dans la base permettant de trier
 */
public class Pagination {
	private int offset;
	private int limit;
	private String orderBy = null;
	
	public Pagination(int offset, int limit) {
		super();
		this.offset = offset;
		this.limit = limit;
	}
	
	public Pagination(int offset, int limit, String orderBy) {
		super();
		this.offset = offset;
		this.limit = limit;
		this.orderBy = orderBy;
	}
	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
