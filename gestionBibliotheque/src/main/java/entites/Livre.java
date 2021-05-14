package entites;

import java.sql.Date;

public class Livre implements Comparable<Object>{
	private String titre;
	private Auteur auteur;
	private Date dateParution;
	private Integer id;
	
	public Livre(String titre, Auteur auteur, Date dateParution) {
		super();
		this.titre = titre;
		this.auteur = auteur;
		this.dateParution = dateParution;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public Auteur getAuteur() {
		return auteur;
	}
	public void setAuteur(Auteur auteur) {
		this.auteur = auteur;
	}
	public Date getDateParution() {
		return dateParution;
	}
	public void setDateParution(Date dateParution) {
		this.dateParution = dateParution;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return this.getDateParution().compareTo(((Livre)o).getDateParution());
	}
}
