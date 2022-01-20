package FinalProject;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

public class Resource implements Cloneable
{
    private final String name;
    private final String author;
    private final String type;
    private final String ISBN;
    private final int searchID;
    private int copies;
    private final double rating;
    private final ArrayList<Readers>borrowers;
    private int daysLeft;
    private LocalDate checkoutDate;
    private LocalDate dueDate;

    public Resource(String name, String author, String type, String ISBN, int searchID, int copies,double rating)
    {
        this.name=name;
        this.author=author;
        this.type=type;
        this.ISBN=ISBN;
        this.searchID=searchID;
        this.copies=copies;
        this.rating=rating;
        borrowers=new ArrayList<>();
        daysLeft=borrowingPeriod();

    }
    private int borrowingPeriod()
    {
        return new Random().nextInt(40)+10;
    }
    public Object clone() throws CloneNotSupportedException
    {
        return (Resource)super.clone();
    }
    public String getName()
    {
        return name;
    }
    public String getAuthor()
    {
        return author;
    }
    public String getType()
    {
        return type;
    }
    public String getISBN()
    {
        return ISBN;
    }
    public int getSearchID()
    {
        return searchID;
    }
    public int getCopies()
    {
        return copies;
    }
    public double getRating()
    {
        return rating;
    }
    public ArrayList<Readers> getBorrowers()
    {
        return borrowers;
    }
    public int getDaysLeft()
    {
        return daysLeft;
    }
    public LocalDate getCheckoutDate()
    {
        return checkoutDate;
    }
    public LocalDate getDueDate()
    {
        return dueDate;
    }
    public void increaseCopies()
    {
        copies++;
    }
    public void decreaseCopies()
    {
        copies--;
    }
    public void setCheckoutDate()
    {
        checkoutDate=LocalDate.now();
    }
    public void setDueDate()
    {
        dueDate=checkoutDate.plus(Period.ofDays(daysLeft));
    }
    public void setDaysLeft(int day)
    {
        daysLeft=day;
    }

}
