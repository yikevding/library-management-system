package FinalProject;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class Library
{
    private final String name;
    private final ArrayList<Readers>readers;
    private final ArrayList<Readers>blacklist;
    private final ArrayList<Resource>resources;
    private final Scanner scanner=new Scanner(System.in);
    private Readers reader;

    public Library(String name)
    {
        this.name=name;
        System.out.println("-------Welcome to Oxford College's library page-------");
        System.out.println();
        readers=new ArrayList<>();
        blacklist=new ArrayList<>();
        resources =new ArrayList<>();
    }
    public void addReaders(Readers e)
    {
        readers.add(e);
    }
    public void showTopRatedBooks()
    {
        System.out.println("-----Our Top Rated Resources-----");
        System.out.println("Welcome to "+name+", the following items are our " +
                "top 10 rated resources.");
        resources.stream()
                .sorted(Comparator.comparing(Resource::getRating).reversed())
                .limit(10)
                .forEach(r-> System.out.println(r.getType()+": "+r.getName()
                +" -"+r.getAuthor()));
        System.out.println();
    }
    public void showCatgeories()
    {
        Set<String>categories=new HashSet<>();
        for(Resource item:resources)
        {
            categories.add(item.getType());
        }
        System.out.println("---Our resources are divided into the following categories---");
        for(String type:categories)
            System.out.print(type+" ");
        System.out.println();
    }
    public void showTopsInCategories()
    {
        System.out.println("Which category are you mostly interested in?");
        String type=scanner.nextLine();
        System.out.println("loading...please wait for a few seconds");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("---Top rated books in "+type+"---");
        resources.stream().filter(m->m.getType().toUpperCase().equals(type.toUpperCase()))
                .sorted(Comparator.comparing(Resource::getRating).reversed())
                .limit(5)
                .forEach(r-> System.out.println(r.getType()+": "+r.getName()
                        +" -"+r.getAuthor()));
        System.out.println();
    }
    public void borrow()
    {
        System.out.println("What would you like to borrow?");
        String book=scanner.nextLine();
        borrowBook(book,reader.getUsername());
        if(blacklist.contains(reader))
            return;
        boolean again=true;
        while(again)
        {
            System.out.println("What else would you like to borrow? (If you don't want anything, type no thanks)");
            book=scanner.nextLine();
            if(book.toUpperCase().equals("NO THANKS"))
            {
                again=false;
                System.out.println("It's my honor to serve you, "+reader.getUsername()
                        +". Have a nice day!");
            }
            else if(findMatchedBook(book)==null)
                System.out.println("Sorry, we don't have this book.");
            else
                borrowBook(book,reader.getUsername());
        }
    }
    public void borrowBook(String book, String reader)///need testing
    {
        System.out.println("-----Welcome to the resource borrow page-----");
        if(isInBlacklist(reader))
        {
            System.out.println("Sorry, you are in library's blacklist.");
            System.out.println("Please return the following overdue item(s) as soon as possible:");
            findOverdueBooks(reader);
            System.out.println();
            return;
        }
        if(findMatchedBook(book)==null)
        {
            System.out.println("We don't have "+book+" in this library.");
            System.out.println();
            return;
        }
        if(findCopies(book)==null)
        {
            System.out.println("All "+book+" have been borrowed.");
            System.out.println("You can have it at "
                    +findClosestDate(findMatchedBook(book)).plus(Period.ofDays(1)));
            System.out.println();
            return;
        }
        Readers e=findReader(reader);
        for(Resource resource:resources)
        {
            if(resource.getName().equals(book))
            {
                resource.decreaseCopies();
                try {
                    Resource copy=(Resource)resource.clone();
                    resource.getBorrowers().add(e);
                    e.getList().add(copy);
                    copy.setCheckoutDate();
                    copy.setDueDate();
                    System.out.println("You have successfully borrowed "+book);
                    System.out.println("Checkout Date: "+copy.getCheckoutDate());
                    System.out.println("Due Date: "+
                            copy.getDueDate()+"\n");
                } catch (CloneNotSupportedException cloneNotSupportedException) {
                    cloneNotSupportedException.printStackTrace();
                }
            }
        }
    }
    public void returnResource()
    {
        System.out.println("-----"+name+"'s Resources Return Page-----");
        if(reader.getList().isEmpty())
        {
            System.out.println("You don't have anything to return");
            return;
        }
        boolean wantToReturn=true;
        System.out.println("What would you like to return?");
        String response=scanner.nextLine();
        while(wantToReturn)
        {
            if(!isBorrowed(response)&& getBook(response)!=-2)
            {
                System.out.println("You didn't borrow "+response);
            }
            for(Resource resource:reader.getList())
                if(resource.getName().equals(response))
                {
                    returnBook(response,reader.getUsername());
                    break;
                }
            if(getBook(response)==-2)
                System.out.println("We don't have "+response+".");
            if(reader.getList().isEmpty())
            {
                System.out.println("You have returned all borrowed resources, thank you!");
                System.out.println();
                return;
            }
            System.out.println("What else would you like to return? (if you don't want to return anything, type no thanks)");
            response=scanner.nextLine();
            if(response.toUpperCase().equals("NO THANKS"))
            {
                System.out.println();
                return;
            }
        }
    }
    public void returnBook(String book, String reader)
    {
        for(Resource resource:resources)
        {
            if(resource.getName().equals(book))
            {
                resource.increaseCopies();
                resource.getBorrowers().remove(findReader(reader));
                System.out.println("You have successfully returned "+book+".");
            }
        }
        Readers e=findReader(reader);
        e.getList().removeIf(resource -> resource.getName().equals(book));
        if(!hasOverdueBooks(findReader(reader)))
            blacklist.remove(e);
    }
    public void sendToBlacklist(Readers e)
    {
        blacklist.add(e);
    }
    public void readersInfo(String reader)
    {
        for(Readers e:readers)
            if(e.getUsername().equals(reader))
                System.out.println(e);
    }
    private Resource findMatchedBook(String book)
    {
        for(Resource resource: resources)
            if(resource.getName().equals(book))
                return resource;
        return null;
    }
    private Resource findCopies(String book)
    {
        for(Resource resource:resources)
            if(resource.getName().equals(book)
            && resource.getCopies()!=0)
                return resource;
        return null;
    }
    private Readers findReader(String reader)
    {
        for(Readers e:readers)
            if(e.getUsername().equals(reader))
                return e;
        return null;
    }
    private boolean isInBlacklist(String reader)
    {
        if(blacklist.isEmpty())
            return false;
        for(Readers e:blacklist)
            if(e.getUsername().equals(reader))
                return true;
        return false;
    }
    private boolean hasOverdueBooks(Readers e)
    {
        for(Resource resource:e.getList())
            if(resource.getDaysLeft()<0)
                return true;
        return false;
    }
    private LocalDate findClosestDate(Resource e)
    {
        ArrayList<LocalDate>list=new ArrayList<>();
        for(Readers reader:readers)
        {
            for(Resource resource:reader.getList())
                if(resource.getName().equals(e.getName()))
                    list.add(resource.getDueDate());
        }
        Collections.sort(list);
        return list.get(0);
    }
    public void countingDown(int daysPassed)
    {
        System.out.println("-----"+daysPassed+" days passed-----");
        System.out.println();
        for(Readers reader:readers)
        {
            if(!(reader.getList().isEmpty()))
            {
                for(Resource item:reader.getList())
                {
                    item.setDaysLeft(item.getDaysLeft()-daysPassed);
                    if(item.getDaysLeft()<0
                    && !(blacklist.contains(reader)))
                        sendToBlacklist(reader);
                }
            }
        }
    }
    public void printBlacklist()
    {
        System.out.println("-----Blacklist-----");
        if(blacklist.isEmpty())
        {
            System.out.println("No one is on the blacklist now.");
            System.out.println();
            return;
        }
        for(Readers reader:blacklist)
            System.out.println(reader.getUsername());
        System.out.println();
    }
    private void findOverdueBooks(String reader)
    {
        ArrayList<Resource>overdueItems=new ArrayList<>();
        for(Resource e:findReader(reader).getList())
            if(e.getDaysLeft()<0)
                overdueItems.add(e);
        for(Resource e:overdueItems)
            System.out.println(e.getName());
    }
    public boolean isBorrowed(String bookname)
    {
        for(Resource item:reader.getList())
            if(item.getName().equals(bookname))
                return true;
        return false;
    }
    public int getBook(String bookname)
    {
        boolean isExisted=false;
        for(Resource resource: getResources())
            if(resource.getName().equals(bookname))
            {
                isExisted=true;
                break;
            }
        if(!isExisted)
            return -2;
        for(Resource resource:getResources())
            if(resource.getName().equals(bookname)
                    && resource.getCopies()!=0)
                return resource.getCopies();
        return -1;
    }
    public ArrayList<Readers> getReaders()
    {
        return readers;
    }
    public ArrayList<Resource> getResources()
    {
        return resources;
    }
    public String getName()
    {
        return name;
    }
    public void setReader(Readers reader)
    {
        this.reader=reader;
    }


}
