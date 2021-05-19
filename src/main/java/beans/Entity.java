package beans;

abstract class Entity {
	private Integer id;

	public Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
		if (id != null) {
			this.id = id;
		}
	}
}
