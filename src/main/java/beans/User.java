package beans;

import org.apache.commons.lang3.StringUtils;

public class User extends Entity<Integer> {
	private String nom;
	private String prenom;
	private String login;
	private String password;
	
	public User(String nom, String prenom, String login, String password, Integer id) {
		this.setNom(nom);
		this.setPrenom(prenom);
		this.setLogin(login);
		this.setPassword(password);
		this.setId(id);
	}
	
	public String getNom() {
		return nom;
	}
	/**
	 * Met a jour le nom
	 * 
	 * @param nom
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le parametre est null, constitue une chaine vide ou est remplie de caracteres d'espacements
	 */
	public void setNom(String nom) {
		if (nom == null || StringUtils.isBlank(nom) || StringUtils.isEmpty(nom)) {
			throw new IllegalArgumentException();
		}
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	/**
	 * Met a jour le prenom
	 * 
	 * @param prenom
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le parametre est null, constitue une chaine vide ou est remplie de caracteres d'espacements
	 */
	public void setPrenom(String prenom) {
		if (prenom == null || StringUtils.isBlank(prenom) || StringUtils.isEmpty(prenom)) {
			throw new IllegalArgumentException();
		}
		this.prenom = prenom;
	}
	
	public String getLogin() {
		return login;
	}
	/**
	 * Met a jour le login
	 * 
	 * @param login
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le parametre est null, constitue une chaine vide ou est remplie de caracteres d'espacements
	 */
	public void setLogin(String login) {
		if (login == null || StringUtils.isBlank(login) || StringUtils.isEmpty(login)) {
			throw new IllegalArgumentException();
		}
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	/**
	 * Met a jour le password
	 * 
	 * @param password
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le parametre est null, constitue une chaine vide ou est remplie de caracteres d'espacements
	 */
	public void setPassword(String password) {
		if (password == null || StringUtils.isBlank(password) || StringUtils.isEmpty(password)) {
			throw new IllegalArgumentException();
		}
		this.password = password;
	}
}
