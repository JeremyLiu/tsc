package com.jec.utils.lang;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Field <T> {

	private Manager<T> manager = null;
	private T value = null;
	private String description = null;

	private Field(Manager<T> manager) {
		this.manager = manager;
	}

	public T getValue() {
		return this.value;
	}

	public String getDescription() {
		return this.description;
	}

	/*
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {

		if(obj == null) {
			return false;
		}

		if(obj.getClass().equals(value.getClass())) {
			return obj.equals(value);
		}

		if(obj.getClass().equals(this.getClass())) {

			Field<T> f = (Field<T>)obj;
			if(this.manager != f.manager) {
				return false;
			}

			if(!this.getValue().equals(f.getValue()))
				return false;

			return true;

		}

		return false;
	}
	*/




	@Override
	public String toString() {
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((manager == null) ? 0 : manager.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Field<T> other = (Field<T>) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (manager == null) {
			if (other.manager != null)
				return false;
		} else if (!manager.equals(other.manager))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public static class Manager<T> {

		private List<Field<T>> values = new LinkedList<Field<T>>();

		private Field<T> defaultValue = null;

		public Manager() {
		}

		public Manager<T> addValue(T value, String description, boolean isDefault) {

			if(value != null && description != null) {

				final Field<T> entry = new Field<T>(this);
				entry.value = value;
				entry.description = description;
				values.add(entry);

				if(isDefault) {
					this.defaultValue = entry;
				}

			}
			return this;
		}

		public Field<T> getDefault() {
			return defaultValue;
		}

		public Collection<Field<T>> getFields() {
			return values;
		}

		public Field<T> getField(T value) {
			Iterator<Field<T>> it = values.iterator();
			while(it.hasNext()) {
				Field<T> entry = it.next();
				if(entry.value.equals(value)) {
					return entry;
				}
			}
			return defaultValue;
		}

	}
}
