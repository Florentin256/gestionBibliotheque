package beans;

import java.sql.Date;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Livre extends Entity<Integer> implements Comparable<Object>{
	private String titre;
	private Auteur auteur;
	private Date dateParution;
	private ArrayList<String> tags = new ArrayList<String>();
	
	public Livre(Integer id, String titre, Auteur auteur, Date dateParution, ArrayList<String> tags) {
		this.setTitre(titre);
		this.setAuteur(auteur);
		this.setDateParution(dateParution);
		this.setTags(tags);
		this.setId(id);
	}
	
	public Livre(String titre, Auteur auteur, Date dateParution) {
		this.setTitre(titre);
		this.setAuteur(auteur);
		this.setDateParution(dateParution);
	}
	
	public String getTitre() {
		return titre;
	}
	/**
	 * Met a jour le titre
	 * 
	 * @param titre
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le parametre est null, constitue une chaine vide ou est remplie de caracteres d'espacements
	 */
	public void setTitre(String titre) {
		if (titre == null || StringUtils.isBlank(titre) || StringUtils.isEmpty(titre)) {
			throw new IllegalArgumentException();
		}
		this.titre = titre;
	}
	
	public Auteur getAuteur() {
		return auteur;
	}
	public void setAuteur(Auteur auteur) {
		if (auteur == null) {
			throw new IllegalArgumentException();
		} 
		this.auteur = auteur;
	}
	
	public Date getDateParution() {
		return dateParution;
	}
	public void setDateParution(Date dateParution) {
		if (dateParution == null) {
			throw new IllegalArgumentException();
		}
		this.dateParution = dateParution;
	}
	
	public ArrayList<String> getTags() {
		return tags;
	}
	protected void setTags(ArrayList<String> tags) {
		if (tags == null) {
			throw new IllegalArgumentException();
		}
		this.tags = tags;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return this.getDateParution().compareTo(((Livre)o).getDateParution());
	}
}
