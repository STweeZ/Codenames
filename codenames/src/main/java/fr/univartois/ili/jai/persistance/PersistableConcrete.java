package fr.univartois.ili.jai.persistance;

public abstract class PersistableConcrete implements Persistable {
	private long id;

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersistableConcrete other = (PersistableConcrete) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
