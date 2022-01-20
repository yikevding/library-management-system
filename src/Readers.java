package FinalProject;

import java.util.ArrayList;
import java.util.Scanner;

public class Readers
{
    private String username;
    private String password;
    private ArrayList<Resource>list;//list of resources borrowed for each reader
    private Library lib;
    private final Scanner scanner=new Scanner(System.in);

    public void register(Library lib)
    {
        System.out.println("-----"+lib.getName()+"'s reader registration page-----");
        System.out.println("Create username: ");
        username=scanner.nextLine();
        while(!isValidUsername(lib))
        {
            System.out.println("This username is taken, enter a new one: ");
            username=scanner.nextLine();
        }
        System.out.println
                ("Create password (at least 8 characters long, " +
                        "at least 1 uppercase letter, " +
                        "1 lowercase letter, " +
                        "and 1 digit): ");
        password=scanner.nextLine();
        while(!isValidPassword())
        {
            System.out.println("Try a new password (see the requirements above): ");
            password=scanner.nextLine();
        }
        lib.addReaders(this);
        System.out.println("Congrats! You have successfully registered at "+lib.getName()+".");
        System.out.println();
        list=new ArrayList<>();
        lib.showTopRatedBooks();
    }
    public void login(Library lib)
    {
        System.out.println("-----"+lib.getName()+"'s reader log in page-----");
        System.out.println("Username: ");
        String name=scanner.nextLine();
        Readers matchReader=findMatchedReader(name,lib);
        while(matchReader==null)
        {
            System.out.println("Username does not exist, try another one: ");
            name=scanner.nextLine();
            matchReader=findMatchedReader(name,lib);
        }
        System.out.println("Password: ");
        String pass=scanner.nextLine();
        while(!(pass.equals(matchReader.password)))
        {
            System.out.println("Password is not correct, try again: ");
            pass=scanner.nextLine();
        }
        System.out.println("Congrats! You have successfully logged into your account!");
        System.out.println();
        this.lib=lib;
        lib.setReader(this);
        lib.showCatgeories();
        lib.showTopsInCategories();
    }
    public void exit() throws InterruptedException
    {
        System.out.println("Signning off...");
        Thread.sleep(5000);
        System.out.println("Thanks for visiting "+lib.getName());
        System.out.println();
    }
    public int getBook(String bookname)
    {
        boolean isExisted=false;
        for(Resource resource:lib.getResources())
            if(resource.getName().equals(bookname))
            {
                isExisted=true;
                break;
            }
        if(!isExisted)
            return -2;
        for(Resource resource:lib.getResources())
            if(resource.getName().equals(bookname)
            && resource.getCopies()!=0)
                return resource.getCopies();
        return -1;
    }
    private Readers findMatchedReader(String name,Library lib)
    {
        for(Readers reader:lib.getReaders())
            if(reader.username.equals(name))
                return reader;
        return null;
    }
    private boolean isValidUsername(Library lib)
    {
        if(lib.getReaders().isEmpty())
            return true;
        for(int i=0;i<lib.getReaders().size();i++)
        {
            if(username.equals(lib.getReaders().get(i).username))
                return false;
        }
        return true;
    }
    private boolean isValidPassword()
    {
        if(password.length()>=8)
            if(password.matches(".*\\d.*"))
                if(password.matches(".*[A-Z].*"))
                    if(password.matches(".*[a-z].*"))
                        return true;
        return false;
    }
    public String toString()
    {
        String page="-----Welcome to the reader's information page-----"+"\n";
        String information="Reader's username: "+username+"\n";
        String intro="---Resource(s) borrowed---\n";
        String items="";
        for(Resource resource:list)
        {
            items+= resource.getType()+": "+
                    resource.getName()+"\n"+
                    "Checkout Date: "+resource.getCheckoutDate()+"\n"+
                    "Due Date: "+resource.getDueDate()+"\n";
            if(resource.getDaysLeft()<0)
                items+=resource.getName()+" is "+Math.abs(resource.getDaysLeft())+" days overdue.\n";
            else
                items+=resource.getDaysLeft()+" days until due date.\n";
            items+="\n";
        }
        return page+information+intro+items;
    }
    public String getUsername()
    {
        return username;
    }
    public ArrayList<Resource> getList()
    {
        return list;
    }
}
