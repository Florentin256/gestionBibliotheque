package dao;

import java.util.List;
import beans.Entity;

/**
 * Interface CRUD
 *
 * @param <T>
 * 			Type de l'objet correspondant � la classe DAO
 * 			qui impl�meente l'interface
 * @param <IdType>
 * 			Type de l'id de la classe 'Entity' dont la classe
 * 			DAO h�rite
 */
public interface DAO<T extends Entity<IdType>, IdType> {
	/**
	 * Recherche par Id
	 * 
	 * @param id
	 * 			identifiant 'id' de l'objet � retourner
	 * @return
	 * 			objet identifi� par 'id' dans la base
	 * @throws DaoException
	 */
	T getById(IdType id) throws DaoException;
	
	/**
	 * Liste par pagination
	 * 
	 * @param pagination
	 * 			Class contenant les param�tres de pagination
	 * @return
	 * 			la liste des objets dans la base sous forme de
	 * 			pagination
	 * @throws DaoException
	 */
	List<T> getAll(Pagination pagination) throws DaoException;
	
	/**
	 * Ajoute l'entite en base
	 * 
	 * @param entity
	 * 			Objet � ajouter en base
	 * @throws DaoException
	 */
	void add(T entity) throws DaoException;
	
	/**
	 * Supprime l'entite en base
	 * 
	 * @param id
	 * 			id de l'objet � supprimer en base
	 * @throws DaoException
	 */
	void remove(IdType id) throws DaoException;
	
	/**
	 * Met � jour l'entite en base
	 * 
	 * @param entity
	 * 			Objet � modifier en base
	 * @throws DaoException
	 */
	void update(T entity) throws DaoException;
}
