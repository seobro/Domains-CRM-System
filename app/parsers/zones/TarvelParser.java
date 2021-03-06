package parsers.zones;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

import models.Domain;

import common.CommonUtils;

public class TarvelParser {

	public static Domain parseWhois(Domain domain) throws Exception {
		String domainUrl = CommonUtils.removeWWW(domain.idn_url);
		Socket s = new Socket("whois.nic.travel", 43);
		s.getOutputStream().write((domainUrl + "\r\n").getBytes("iso-8859-1"));
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream(), "iso-8859-1"));
		while (true) {
			String line = br.readLine();
			if (line != null) {
				if (line.startsWith("Domain Registration Date:")) {
					String dateString = line.substring(25).trim();
					domain.created = new Date(dateString);
				}
				if (line.startsWith("Domain Expiration Date:")) {
					String dateString = line.substring(23).trim();
					domain.paid_till = new Date(dateString);
				}
			} else {
				break;
			}
		}
		s.close();
		return domain;
	}

}
