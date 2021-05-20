package beans;

public class Auteur extends Entity<Integer> {
	private String nom;
	private String prenom;
	
	public Auteur(String nom, String prenom, Integer id) {
		this.setNom(nom);
		this.setPrenom(prenom);
		this.setId(id);
	}
	
	public Auteur(String nom, String prenom) {
		this.setNom(nom);
		this.setPrenom(prenom);
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
}
