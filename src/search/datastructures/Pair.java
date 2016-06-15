package search.datastructures;

import java.io.Serializable;

public final class Pair<F, S> implements Serializable
{
	private static final long serialVersionUID = 1L;
	public final F f;
	public final S s;

	public Pair(F f, S s)
	{
		this.f = f;
		this.s = s;
	}

	@Override
	public String toString()
	{
		return String.format("pair(%s, %s)", f.toString(), s.toString());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		return result;
	}
	
	public long hashCode64()
	{
		final long prime = 31;
		long result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Pair<F, S> other = (Pair<F, S>) obj;
		if (f == null)
		{
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		if (s == null)
		{
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}

}
