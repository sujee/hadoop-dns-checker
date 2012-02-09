package net.sujee.hadoop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class CheckDNS
{

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length < 1)
        {
            System.out.println ("needs hosts file(s)");
            System.exit(1);
        }
        
        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println ("==== Running on : " + localhost + " =====");
        for (String s : args)
        {
            String[] hosts = readLinesFromFile(s, true);
            for (String host : hosts)
            {
                System.out.println ("-- host : " + host);
                
                InetAddress inet = null;
                try
                {
                    inet = InetAddress.getByName(host);
                    String ip = inet.getHostAddress();
                    System.out.println ("   host lookup : success (" + ip + ")");
                } catch (UnknownHostException e)
                {
                    System.out.println ("   host lookup : *** failed ***" );
                    e.printStackTrace();
                }
                
                // reverse
                if (inet != null)
                {
                    try
                    {
                        InetAddress revHost = InetAddress.getByAddress(inet.getAddress());
                        String revHostName = revHost.getHostName();
                        System.out.println ("   reverse lookup : success (" + revHostName + ")");
                    }
                    catch (UnknownHostException e)
                    {
                        System.out.println ("   reverse lookup : ***failed***" );
                        
                    }
                    
                    if (inet.isReachable(10))
                        System.out.println ("   is reachable : yes");
                    else
                        System.out.println ("   is reachable : *** no ***");
                    
                }
                System.out.println ("");
            } // end for loop on hosts
        }
    }
    
    public static String [] readLinesFromFile(String fileName, boolean skipCommentsAndBlanks) throws IOException
    {
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        
        String line = null;
        while ( (line = reader.readLine()) != null)
        {
            if (skipCommentsAndBlanks)
            {
                line = line.trim();
                if (line.length() == 0 ) continue;
                
                if (line.startsWith("#")) continue;
            }
            lines.add(line);
        }
        
        String [] arrLines = new String[lines.size()];
        lines.toArray(arrLines);
        return arrLines;
    }

}
