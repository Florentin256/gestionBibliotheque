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
	
	public User(String nom, String prenom, String login, String password) {
		this.setNom(nom);
		this.setPrenom(prenom);
		this.setLogin(login);
		this.setPassword(password);
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
		if (StringUtils.isBlank(nom)) {
			throw new IllegalArgumentException("name cannot be null");
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
		if (StringUtils.isBlank(prenom)) {
			throw new IllegalArgumentException("firstname cannot be null");
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
		if (StringUtils.isBlank(login)) {
			throw new IllegalArgumentException("login cannot be null");
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
		if (StringUtils.isBlank(password)) {
			throw new IllegalArgumentException("password cannot be null");
		}
		this.password = password;
	}
}
