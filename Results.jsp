<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.sql.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1 align="center">Search Page</h1>
	<br>
	<table align="center">
		<tr>
			<td><input type="text" name="fname" id="textbox"
				placeholder="Enter your query."></td>
		</tr>
		<tr></tr>
		<tr>
			<td align="center"><input type="submit" value="Search"
				onClick=myfunct()></td>
		</tr>
	</table>

	<script type="text/javascript">
		function myfunct() {
			var value = document.getElementById('textbox').value;
			window.location = "Results.jsp?value=" + value;
		}
	</script>

	<%
		String value = request.getParameter("value").replaceAll("%20", " ")
				.replaceAll("/", " ");
	String[] query_token;
	query_token = value.split(" ");
	out.println(query_token.length);
//	out.println(query_token[0]);
	%>
	<%
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			out.println("can't load mysql driver");
			out.println(e.toString());
		}
	%>
	<%
		try {
			String url = "jdbc:mysql://127.0.0.1:3306/gallery";
			String id = "gallery";
			String pwd = "eecs221";
			Connection con = DriverManager.getConnection(url, id, pwd);

			Statement stmt;
			PreparedStatement pstmt;
			ResultSet rs;
//			ResultSet rs1;
			stmt = con.createStatement(); // Statements allow to issue SQL queries to the database

			out.println("<br>");
			out.println("<table border=\"1\" align=center>");
			out.println("<tr>");
			out.println("<th>Results</th>");
			out.println("</tr>");

// 			String sql = "select * from data where token=? order by score desc limit 10";
// 			pstmt = con.prepareStatement(sql,
// 					Statement.RETURN_GENERATED_KEYS);
// 			pstmt.setString(1, value);
// 			pstmt.execute();
			
			InputStream file = new FileInputStream("D:/CrawlerData/title.ser");
    			InputStream buffer = new BufferedInputStream(file);
    			ObjectInput input = new ObjectInputStream (buffer);
    			@SuppressWarnings("unchecked")
    			HashMap<String,ArrayList<String>> titletable =
    			(HashMap<String, ArrayList<String>>) input.readObject();
    			input.close();
			
			HashMap<String, Float> finalrank = new HashMap<String, Float>();
			HashMap<String, Float> currtoken = new HashMap<String, Float>();
			int i=0;
			List<Map.Entry<String, Float>> obj=null;
			float score=0;
			while(i<query_token.length){
			i++;
			
			pstmt = con
					.prepareStatement("select URL,score from store where token=? order by score desc limit 10");
			pstmt.setString(1, query_token[i-1]);
			rs = pstmt.executeQuery();
			//=================================================
			while (rs.next()) {
				String url1 = rs.getString("URL");
				String scorein = rs.getString("score");
				currtoken.put(url1,Float.parseFloat(scorein));
			}
			
				
			
// 			for(String mistake:tokens){
// 				if(table.containsKey(mistake))
// 					flag=true;
// 			}
			for (Map.Entry<String, Float> me : currtoken.entrySet()){ // assuming your map is Map<String, String>
						if(finalrank.containsKey(me.getKey())){
							score = currtoken.get(me.getKey());
							score = score + finalrank.get(me.getKey());
							finalrank.put(me.getKey(), score);
						}
						else{
							finalrank.put(me.getKey(), currtoken.get(me.getKey()));
						}
					//currtoken.clear();        
			}
			score=0;
			ArrayList<String> tempurl = new ArrayList<String>();
			for (Map.Entry<String, Float> me : finalrank.entrySet()){ // assuming your map is Map<String, String>
				
				if(titletable.containsKey(me.getKey())){
					tempurl = titletable.get(me.getKey());
					if(tempurl.contains(query_token[i-1])){
					score = finalrank.get(me.getKey())*5;
					finalrank.put(me.getKey(), score);
					}
				}
			}
				//=================sorting final map==============
				obj = new ArrayList<Map.Entry<String, Float>>(finalrank.entrySet());
				Collections.sort(obj, new Comparator<Map.Entry<String, Float>>(){
					public int compare(Map.Entry<String, Float> entry1, Map.Entry<String, Float> entry2) {
						Float ent1 = entry1.getValue();
						Float ent2 = entry2.getValue();
						return ent2.compareTo(ent1);
					}});
			//===========subdomain============================
				score=0;
				String subdomainedit;
				String[] qwerty;
				String finurl;
				String upgrade;// = new String(query_token[i-1]);
				for (Map.Entry<String, Float> me1 : finalrank.entrySet()){
					subdomainedit = me1.getKey();
					upgrade = query_token[i-1];
					if(subdomainedit.contains(upgrade)){
						qwerty = subdomainedit.split("/");
						if(qwerty[1].contains(upgrade)){
							finurl=("http:/").concat(qwerty[1]).concat("/");
							score = finalrank.get(me1.getKey())*(float)8;
							finalrank.put(finurl,score);
							finurl="";
							break;
						}
						if(qwerty[2].contains(upgrade)){
							finurl=("http:/").concat(qwerty[1]).concat("/").concat(qwerty[2]).concat("/");
							score = finalrank.get(me1.getKey())*(float)7;
							finalrank.put(finurl,score);
							finurl="";
							break;
						}
						if(qwerty[3].contains(upgrade)){
							finurl=("http:/").concat(qwerty[1]).concat("/").concat(qwerty[2]).concat("/").concat(qwerty[3]).concat("/");
							score = finalrank.get(me1.getKey())*(float)7;
							finalrank.put(finurl,score);
							finurl="";
							break;
						}
						
					}
				}
				
			//=================================================
			}
			obj = new ArrayList<Map.Entry<String, Float>>(finalrank.entrySet());
			Collections.sort(obj, new Comparator<Map.Entry<String, Float>>(){
				public int compare(Map.Entry<String, Float> entry1, Map.Entry<String, Float> entry2) {
					Float ent1 = entry1.getValue();
					Float ent2 = entry2.getValue();
					return ent2.compareTo(ent1);
				}});
			
// 			int bound=0;
// 			while (bound<finalrank.size()) {
// 				out.println("<tr>");
// 				String temp = rs.getString("URL");
				
// 				out.println("<td><a href="+temp+">" + temp + "</a></td>");
// 				out.println("</tr>");
// 				bound++;
// 			}
			int j=0;
			while(true){
				if(obj.size()==0){
					//System.out.println("not present in this domain");
					//JOptionPane.showMessageDialog(null, "Not present!!!");
					break;
				}
				if(j>5){
					break;
				}
				if(j>obj.size()-1){
					break;
				}
				String[] URL = obj.get(j).toString().split("[=]");
				out.println("</tr>");
				out.println("<td><a href="+URL[0]+">" + URL[0] + "</a></td>");
				out.println("</tr>");

				//=======================Text Snippet=========================
				//if(table.containsKey(arg0))
				//========================end====================================
				j++;
			}
			out.println("</table>");

			con.close();
		} catch (Exception e) {
			out.println(e.toString());
		}
	%>

</body>
</html>