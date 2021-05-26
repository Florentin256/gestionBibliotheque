package beans;

public abstract class Entity<Type> {
	private Type id;

	public Type getId() {
		return id;
	}

	public void setId(Type id) {
		this.id = id;
	}
}
