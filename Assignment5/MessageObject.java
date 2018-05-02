public class MessageObject {
		public String command = "";
		public String name = "";
		public String ssn = "";
		public String addr = "";
		public String zip = "";
		public MessageObject(String a) {
				int temp ;
				command = a.substring(0,a.indexOf(" ") > 0 ? a.indexOf(" "):a.length());
				if((temp = a.indexOf("name: '")) > 0){name = a.substring(temp+ 7,a.indexOf("'", temp+7));}
				if((temp = a.indexOf("ssn: '")) > 0){ssn = a.substring(temp + 6,a.indexOf("'", temp+6));}
				if((temp = a.indexOf("addr: '")) > 0){addr = a.substring(temp + 7,a.indexOf("'", temp+7));}
				if((temp = a.indexOf("zip: '")) > 0){zip = a.substring(temp + 6,a.indexOf("'", temp+6));}
		}
}