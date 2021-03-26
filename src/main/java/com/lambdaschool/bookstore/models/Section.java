package com.lambdaschool.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "section")
public class Section
        extends Auditable
{
    // --------- Table Fields --------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long sectionid; // primary key

    private String name;

    // -------- Association Field ----------
    @OneToMany(mappedBy = "section")
    @JsonIgnoreProperties("section")
    private Set<Book> books = new HashSet<>();

    //--------- Constructor ----------
    public Section()
    {
        // default Constructor for JPA
    }

    // constructor with parameters
    public Section(String name)
    {
        this.name = name;
    }

    // --------- Getters and Setters ----------
    public long getSectionid()
    {
        return sectionid;
    }

    public void setSectionid(long sectionid)
    {
        this.sectionid = sectionid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    // --------- Association Getter and Setters -------
    public Set<Book> getBooks()
    {
        return books;
    }

    public void setBooks(Set<Book> books)
    {
        this.books = books;
    }
}