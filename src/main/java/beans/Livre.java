package beans;

import java.sql.Date;
import java.util.ArrayList;

public class Livre extends Entity implements Comparable<Object>{
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
	
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		if (titre != null) {
			this.titre = titre;
		}
	}
	public Auteur getAuteur() {
		return auteur;
	}
	public void setAuteur(Auteur auteur) {
		if (auteur != null) {
			this.auteur = auteur;
		}
	}
	public Date getDateParution() {
		return dateParution;
	}
	public void setDateParution(Date dateParution) {
		if (dateParution != null) {
			this.dateParution = dateParution;
		}
	}
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		if (tags != null) {
			this.tags = tags;
		}
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return this.getDateParution().compareTo(((Livre)o).getDateParution());
	}
}
