package net.sujee.hadoop;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * This one use the JNDI lookup as used in Hadoop, HBase.
 * HBase doesn't pass any environment to the dirContext, which is a huge bummer.
 *
 * @author shyam334
 *
 */
public class CheckDNSJNDI {

    public static void main(String[] args) throws Exception {

        System.out.println("# self check...");
        InetAddress inet = checkHost(null);
        System.out.println("# end self check\n");

        if (args.length > 0)
        {
            if (inet != null)
                System.out.println("==== Running on : " + inet + " =====");
            else
                System.out.println("==== Running on : unknown host =====");
        }

            String[] hosts = readLinesFromFile(args[0], true);
            for (String host : hosts) {
                checkHost(host);
                System.out.println("");
            }
    }

    public static InetAddress checkHost(String host) throws Exception {
        if (host != null)
            System.out.println("-- host : " + host);

        InetAddress inet = null;
        try {
            if (host != null) {
                Long start = System.currentTimeMillis();
                inet = InetAddress.getByName(host);
                System.out.println("Time took for Inetaddress lookup in ms : "+(System.currentTimeMillis()-start));
            }
            else
            {
                inet = InetAddress.getLocalHost();
                host = inet.getHostName();
                System.out.println("-- host : " + host);
            }
            String ip = inet.getHostAddress();
            System.out.println("   host lookup : success (" + ip + ")");
        } catch (Exception e) {
            System.out.println("   host lookup : *** failed ***");
            System.err.println(e);
        }

        // reverse
        if (inet != null) {

            String ns=null;

                //Copied from hadoop org.apache.hadoop.net.DNS
                String[] parts = inet.getHostAddress().split("\\.");
                String reverseIP = parts[3] + "." + parts[2] + "." + parts[1] + "."
                        + parts[0] + ".in-addr.arpa";

                DirContext ictx = new InitialDirContext();
                Attributes attribute = null;
                Long start = System.currentTimeMillis();
                try {
                    attribute = ictx.getAttributes("dns://"
                            // Use "dns:///" if the default
                            + ((ns == null) ? "" : ns) +
                            // nameserver is to be used
                            "/" + reverseIP, new String[] { "PTR" });
                    System.out.println("Time took for lookup in ms : "+(System.currentTimeMillis()
                            -start));
                } catch(Exception e) {
                    System.out.println("*FAILED*: Lookup failed with an exception. Time took in ms : "+(System
                            .currentTimeMillis()-start));
                    System.err.println(e);
                }
                finally {
                    ictx.close();
                }
                if(attribute != null) {
                    String hostname = attribute.get("PTR").get().toString();
                    int hostnameLength = hostname.length();
                    if (hostname.charAt(hostnameLength - 1) == '.') {
                        hostname = hostname.substring(0, hostnameLength - 1);
                    }
                    System.out.println("JNDI resolved entry : " + hostname);
                }
            }

        return inet;
    }

    public static String[] readLinesFromFile(String fileName,
                                             boolean skipCommentsAndBlanks) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String line = null;
        while ((line = reader.readLine()) != null) {
            if (skipCommentsAndBlanks) {
                line = line.trim();
                if (line.length() == 0)
                    continue;

                if (line.startsWith("#"))
                    continue;
            }
            lines.add(line);
        }

        String[] arrLines = new String[lines.size()];
        lines.toArray(arrLines);
        return arrLines;
    }

}
