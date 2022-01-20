package FinalProject;

public class LibrarySystem
{
    public static void main(String[]args) throws InterruptedException
    {
        ///set up
        Library oxfordLibrary=new Library("Oxford College Library");
        Importer importer=new Importer();
        importer.buildInventory(oxfordLibrary);
        Readers reader1=new Readers();
        Readers reader2=new Readers();

        ///reader1 borrow book first time
        reader1.register(oxfordLibrary);
        reader1.login(oxfordLibrary);
        oxfordLibrary.borrow();
        reader1.exit();

        ///reader2 coming in
        System.out.println("---Two hours later---");
        System.out.println();
        Thread.sleep(5000);

        reader2.register(oxfordLibrary);
        reader2.login(oxfordLibrary);
        oxfordLibrary.borrow();
        reader2.exit();

        ///50 days later
        Thread.sleep(5000);
        oxfordLibrary.countingDown(50);
        oxfordLibrary.readersInfo(reader1.getUsername());
        oxfordLibrary.readersInfo(reader2.getUsername());
        oxfordLibrary.printBlacklist();

        ///reader1 borrow denied and return
        reader1.login(oxfordLibrary);
        oxfordLibrary.borrow();
        oxfordLibrary.returnResource();
        reader1.exit();
        oxfordLibrary.printBlacklist();

        ///reader2 return and borrow
        reader2.login(oxfordLibrary);
        oxfordLibrary.returnResource();
        oxfordLibrary.borrow();
        reader2.exit();
        oxfordLibrary.printBlacklist();
        oxfordLibrary.readersInfo(reader1.getUsername());
        oxfordLibrary.readersInfo(reader2.getUsername());
    }
}
