package dao;

import java.util.ArrayList;

public interface StandardCRUD<T> {
	T getById(Object id) throws DAOException;
	ArrayList<T> getAll() throws DAOException;
	ArrayList<T> getAll(int offset) throws DAOException;
	void add(T obj) throws DAOException;
	void remove(T obj) throws DAOException;
	void update(T obj) throws DAOException;
}
