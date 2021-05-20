package beans;

import org.apache.commons.lang3.StringUtils;

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
}
