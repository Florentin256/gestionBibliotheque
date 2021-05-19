package beans;

public class User extends Entity{
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
	public void setNom(String nom) {
		if (nom != null) {
			this.nom = nom;
		}
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		if (prenom != null) {
			this.prenom = prenom;
		}
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		if (login != null) {
			this.login = login;
		}
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if (password != null) {
			this.password = password;
		}
	}
}
