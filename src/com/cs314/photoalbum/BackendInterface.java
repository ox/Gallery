package com.cs314.photoalbum;

public interface BackendInterface {

	Object load(String id);
	Object loadAll();
	boolean save(Object o);
	boolean delete(String id);
}