package eps.scp;

/**
 * Created by Nando on 8/10/19.
 */
public class Query {

    public static void main(String[] args) {
        InvertedIndex hash;
        String queryString=null, indexDirectory=null, fileName=null;

        if (args.length <3 || args.length>5)
            System.err.println("Error in Parameters. Usage: Query <Query_String> <Index_Directory> <filename> <Threads_Number> [<Key_Size>]");
        if (args.length > 0)
            queryString = args[0];
        if (args.length > 1)
            indexDirectory = args[1];
        if (args.length > 2)
            fileName = args[2];
        if (args.length > 4)
            hash = new InvertedIndex(Integer.parseInt(args[4]));
        else
            hash = new InvertedIndex();

        int num_threads = Integer.parseInt(args[3]);
        hash.SetNumThreads(num_threads);
        hash.SetPercentage(10);

        hash.LoadIndex(indexDirectory);
        hash.SetFileName(fileName);
        //hash.PrintIndex();
        hash.Query(queryString);
        //Liberar memoria
        System.gc();
    }
}
