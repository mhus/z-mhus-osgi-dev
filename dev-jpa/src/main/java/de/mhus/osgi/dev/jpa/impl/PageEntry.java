package de.mhus.osgi.dev.jpa.impl;

import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "page_entries")
public class PageEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String linkName;
    private URL linkDestination;
    
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public URL getLinkDestination() {
		return linkDestination;
	}
	public void setLinkDestination(URL linkDestination) {
		this.linkDestination = linkDestination;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

    // gets & setters omitted
}
