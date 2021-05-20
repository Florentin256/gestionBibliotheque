package beans;

abstract class Entity<Type> {
	private Type id;

	public Type getId() {
		return id;
	}

	public void setId(Type id) {
		if (id != null) {
			this.id = id;
		} else {
			throw new IllegalArgumentException();
		}
	}
}
