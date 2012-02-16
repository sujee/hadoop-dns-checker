package net.sujee.hadoop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * check dns and reverse dns of hosts in a given file
 * 
 * @author sujee@sujee.net
 * 
 */
public class CheckDNS {

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
		
		for (String s : args) {
			String[] hosts = readLinesFromFile(s, true);
			for (String host : hosts) {
				checkHost(host);
				System.out.println("");
			} 
		}
	}

	public static InetAddress checkHost(String host) {
		if (host != null)
			System.out.println("-- host : " + host);

		InetAddress inet = null;
		try {
			if (host != null)
				inet = InetAddress.getByName(host);
			else
            {
				inet = InetAddress.getLocalHost();
                host = inet.getHostName();
                System.out.println("-- host : " + host);
            }
			String ip = inet.getHostAddress();
			System.out.println("   host lookup : success (" + ip + ")");
		} catch (UnknownHostException e) {
			System.out.println("   host lookup : *** failed ***");
			e.printStackTrace();
		}

		// reverse
		if (inet != null) {
			try {
				InetAddress revHost = InetAddress.getByAddress(inet
						.getAddress());
				String revHostName = revHost.getHostName();
				if (host.equals(revHostName))
					System.out.println("   reverse lookup : success ("
							+ revHostName + ")");
				else
					System.out.println("   reverse lookup : ***failed*** ("
							+ revHostName + " != " + host + " (expected) )");
			} catch (UnknownHostException e) {
				System.out.println("   reverse lookup : ***failed***");

			}
			try {
				if (inet.isReachable(10))
					System.out.println("   is reachable : yes");
				else
					System.out.println("   is reachable : *** failed ***");
			} catch (IOException e) {
				System.out.println("   is reachable  : *** failed ***");
				e.printStackTrace();
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
