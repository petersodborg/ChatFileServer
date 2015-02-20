package chatfileserver;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Scanner;
/**
 *
 * @author petersodborgchristensen
 */
public class ChatFileServer {
    static int port = 8080;
    static String ip = "127.0.0.1";
    static String htmlfolder = "/html";
    
    public static void main(String[] args) throws IOException {
    if(args.length == 2){
      port = Integer.parseInt(args[0]);
      ip = args[1];
    }
    HttpServer server = HttpServer.create(new InetSocketAddress(ip,port), 0);
    server.createContext("/index", new RequestHandler());
    server.createContext("/onlineusers", new RequestHandleronlineUsers());
    server.createContext("/logfile", new RequestHandlerlogfile());
    server.createContext("/chatclient", new RequestHandlerchatclient());
    server.createContext("/dokumentation", new RequestHandlerDocuments());
    server.setExecutor(null); // Use the default executor
    server.start();
    System.out.println("Server started, listening on port: "+port);
    }
    
  static class RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {

      String response = "<h1>CA-1 Project page. Gruppe 3</h1>";
      StringBuilder sb = new StringBuilder();
      sb.append("<!DOCTYPE html>\n");
      sb.append("<html>\n");
      sb.append("<head>\n");
      sb.append("<title>My fancy Web Site</title>\n");
      sb.append("<meta charset='UTF-8'>\n");
      sb.append("</head>\n");
      sb.append("<body>\n");
      sb.append("<h2>CA-1 Gruppe 3</h2>");
      sb.append("<ul><li><a href='onlineusers'>Online Users</a></ul></li>");
      sb.append("<ul><li><a href='logfile'>logfile</a></ul></li>");
      sb.append("<h2>Get the chat client</h2>");
      sb.append("<ul><li><a href='dokumentation/ChatClient.jar'>Get the chat client</a></ul></li>");
      sb.append("<h2>Dokumentation</h2>");
      sb.append("<ul><li><a href='dokumentation/description.pdf'>Dokumentation</a></ul></li>");
      sb.append("<h2>Github link</h2>");
      sb.append("<a href='https://github.com/nikolai94/ChatClient'>Github link</a>");
      sb.append("</body>\n");
      sb.append("</html>\n");
      response = sb.toString();   
      //response += "<br>"+"URI:"+he.getRequestURI();
      Scanner scan = new Scanner(he.getRequestBody());
      while(scan.hasNext()){
          response += "<br/>" + scan.nextLine();
      }
      Headers h = he.getResponseHeaders();
      h.add("Content-Type", "text/html");
            
      he.sendResponseHeaders(200, response.length());
      try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
        pw.print(response); //What happens if we use a println instead of print --> Explain
      }
      
    }
  }

static class RequestHandlerlogfile implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            File file = new File("chatLog.txt");
            String response;

            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>Opgave 2</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            sb.append("<table border='1'><tr><td>Date/Message</td></tr>");

        try {

        Scanner sc = new Scanner(file);
        int countWhile = 0;
        String text = "";

        while (sc.hasNextLine()) {
            countWhile++;
            if(countWhile == 2){
                
            text += sc.nextLine();

            //String[] splittext = text.split(text);
                sb.append("<tr>");
                sb.append("<td>");
                sb.append(text);
                sb.append("</td>");
                
                sb.append("</tr>");
                countWhile = 0;
         }
        }
            sc.close();
        } 
        catch (FileNotFoundException e) {
        e.printStackTrace();
        }


            
            sb.append("</table>");
            sb.append("</body>\n");
            sb.append("</html>\n");
            response = sb.toString();
            

            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");
            he.sendResponseHeaders(200, response.length());
            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(response); //What happens if we use a println instead of print --> Explain
            }
        }
    }


 static class RequestHandlerDocuments implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {
            String response = null;
            String contentFolder = "public/";
            String url = "";
            url = he.getRequestURI().toString();
            System.out.println(url);
            url = url.substring(15);
            System.out.println(url);
            String doctype[] = url.split("\\.");
            File file = new File(contentFolder + url);
            byte[] bytesToSend = new byte[(int) file.length()];
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
            } catch (IOException ie) {
                response = "Den indtastede fil findes ikke";
               he.sendResponseHeaders(200, response.length());
                try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                    pw.print(response); //What happens if we use a println instead of print --> Explain
                }
                 ie.printStackTrace();
            }
            String dtype = "";
            if (doctype[1].equalsIgnoreCase("jpg")) {
                dtype = "Images/Jpg";
            } else if (doctype[1].equalsIgnoreCase("pdf")) {
                dtype = "application/pdf";
            } else if (doctype[1].equalsIgnoreCase("basic")) {
                dtype = "audio/basic";
            }

            Headers h = he.getResponseHeaders();
            h.add("Content-Type", dtype);

            he.sendResponseHeaders(200, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }

        }
    }

  static class RequestHandleronlineUsers implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {

      String response = "<h1>Online users</h1>";
      StringBuilder sb = new StringBuilder();
      sb.append("<!DOCTYPE html>\n");
      sb.append("<html>\n");
      sb.append("<head>\n");
      sb.append("<title>My fancy Web Site</title>\n");
      sb.append("<meta charset='UTF-8'>\n");
      sb.append("</head>\n");
      sb.append("<body>\n");
      sb.append("<h2>Bla bla bla bla bla</h2>\n");
      sb.append("</body>\n");
      sb.append("</html>\n");
      response = sb.toString();   
      response += "<br>"+"URI:"+he.getRequestURI();
      Scanner scan = new Scanner(he.getRequestBody());
      while(scan.hasNext()){
          response += "<br/>" + scan.nextLine();
      }
      Headers h = he.getResponseHeaders();
      h.add("Content-Type", "text/html");
            
      he.sendResponseHeaders(200, response.length());
      try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
        pw.print(response); //What happens if we use a println instead of print --> Explain
      }
      
    }
    
  }
 static class RequestHandlerchatclient implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {

      String response = "<h1>Online users</h1>";
      StringBuilder sb = new StringBuilder();
      sb.append("<!DOCTYPE html>\n");
      sb.append("<html>\n");
      sb.append("<head>\n");
      sb.append("<title>Gruppe 3</title>\n");
      sb.append("<meta charset='UTF-8'>\n");
      sb.append("</head>\n");
      sb.append("<body>\n");
      sb.append("<h2>Chat client</h2>\n");
      sb.append("</body>\n");
      sb.append("</html>\n");
      response = sb.toString();   
      response += "<br>"+"URI:"+he.getRequestURI();
      Scanner scan = new Scanner(he.getRequestBody());
      while(scan.hasNext()){
          response += "<br/>" + scan.nextLine();
      }
      Headers h = he.getResponseHeaders();
      h.add("Content-Type", "text/html");
            
      he.sendResponseHeaders(200, response.length());
      try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
        pw.print(response); //What happens if we use a println instead of print --> Explain
      }
      
    }
    
  }
}
