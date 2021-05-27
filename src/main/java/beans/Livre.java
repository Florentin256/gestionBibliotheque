package beans;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Livre extends Entity<Integer> implements Comparable<Object>{
	private String titre;
	private Auteur auteur;
	private Date dateParution;
	private List<String> tags = new ArrayList<String>();
	
	public Livre(Integer id, String titre, Auteur auteur, Date dateParution, List<String> tags) {
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
		if (StringUtils.isBlank(titre)) {
			throw new IllegalArgumentException("title cannot be null");
		}
		this.titre = titre;
	}
	
	public Auteur getAuteur() {
		return auteur;
	}
	public void setAuteur(Auteur auteur) {
		if (auteur == null) {
			throw new IllegalArgumentException("author cannot be null");
		} 
		this.auteur = auteur;
	}
	
	public Date getDateParution() {
		return dateParution;
	}
	public void setDateParution(Date dateParution) {
		if (dateParution == null) {
			throw new IllegalArgumentException("publishing date cannot be null");
		}
		this.dateParution = dateParution;
	}
	
	public List<String> getTags() {
		return tags;
	}
	protected void setTags(List<String> tags) {
		if (tags == null) {
			throw new IllegalArgumentException("tags cannot be null");
		}
		this.tags = tags;
	}

	@Override
	public int compareTo(Object o) {
		return this.getDateParution().compareTo(((Livre)o).getDateParution());
	}
}
