package mihnayan.divetojava.base;

public class UserId {
	
	private int id;

	public UserId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserId))
			return false;
		UserId other = (UserId) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ((Integer) id).toString();
	}
	
	

}
