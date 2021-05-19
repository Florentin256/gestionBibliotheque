package beans;

public class Auteur extends Entity {
	private String nom;
	private String prenom;
	
	public Auteur(String nom, String prenom, Integer id) {
		this.setNom(nom);
		this.setPrenom(prenom);
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
}
