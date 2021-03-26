package com.lambdaschool.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "author")
public class Author
        extends Auditable
{
    // ---------- Table Fields ----------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnoreProperties("authorid")
    private long authorid; // primary key

    private String fname;
    private String lname;

    // ------- Association Fields ----------
    @OneToMany(mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnoreProperties("author")
    private Set<Wrote> wrotes = new HashSet<>();

    // ------ Constructors ---------
    public Author()
    {
        // default constructor to be used with JPA
    }
    // constructor with parameters
    public Author(String fname,
                  String lname)
    {
        this.fname = fname;
        this.lname = lname;
    }

    // ------ Getters and Setters ------
    public long getAuthorid()
    {
        return authorid;
    }

    public void setAuthorid(long authorid)
    {
        this.authorid = authorid;
    }

    public String getFname()
    {
        return fname;
    }

    public void setFname(String fname)
    {
        this.fname = fname;
    }

    public String getLname()
    {
        return lname;
    }

    public void setLname(String lname)
    {
        this.lname = lname;
    }

    // -------- Association Getters and Setters -------
    public Set<Wrote> getWrotes()
    {
        return wrotes;
    }

    public void setWrotes(Set<Wrote> wrotes)
    {
        this.wrotes = wrotes;
    }
}