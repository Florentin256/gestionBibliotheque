package beans;

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
		if (nom != null && !nom.equals("") && !nom.replaceAll("\\s+","").equals("")) {
			this.nom = nom;
		} else {
			throw new IllegalArgumentException();
		}
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
		if (prenom != null && !prenom.equals("") && !prenom.replaceAll("\\s+","").equals("")) {
			this.prenom = prenom;
		} else {
			throw new IllegalArgumentException();
		}
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
		if (login != null && !login.equals("") && !login.replaceAll("\\s+","").equals("")) {
			this.login = login;
		} else {
			throw new IllegalArgumentException();
		}
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
		if (password != null && !password.equals("") && !password.replaceAll("\\s+","").equals("")) {
			this.password = password;
		} else {
			throw new IllegalArgumentException();
		}
	}
}
