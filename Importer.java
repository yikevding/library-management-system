package FinalProject;

import java.io.*;

public class Importer
{
    public void buildInventory(Library lib)
    {
        /*
        change path accordingly, otherwise it won't work
         */
        String path="/Users/amazingman/Desktop/Datasets/book inventory csv/library_inventory.csv";
        String line="";
        try
        {
            BufferedReader reader=new BufferedReader(new FileReader(path));
            reader.readLine();
            while((line=reader.readLine())!=null)
            {
                String[]fields=line.split(";");
                String title=fields[1].replaceAll("\"","");
                String author=fields[2].replaceAll("\"","");
                String genre=fields[3].replaceAll("\"","");
                String ISBN=fields[4].replaceAll("\"","");
                int searchID=Integer.parseInt(fields[5]);
                int copies=Integer.parseInt(fields[6]);
                double rating=Double.parseDouble(fields[7].replaceAll("\"",""));
                Resource resource=new Resource(title,author,genre,ISBN,searchID,copies,rating);
                lib.getResources().add(resource);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
