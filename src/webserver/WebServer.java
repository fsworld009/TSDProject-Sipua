    //A Simple Web Server (WebServer.java)
package webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
    import com.sun.net.httpserver.HttpServer;
    import java.io.BufferedReader;
import java.io.File;
    import java.io.InputStreamReader;
    import java.io.PrintWriter;
    import java.net.ServerSocket;
    import java.net.Socket;
    import java.io.FileReader;
    import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
    import java.net.InetSocketAddress;
    import java.util.logging.Level;
    import java.util.logging.Logger;

    public class WebServer {

      /* WebServer constructor. */

      //ServerSocket serverSocket;  
        private HttpServer httpServer;
        private int httpPort = 9527;
        public void init(){
              try {
                  /*try {
                      serverSocket = new ServerSocket(httpPort);
                  } catch (IOException ex) {
                      Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
                  }*/
                  httpServer = HttpServer.create(new InetSocketAddress(httpPort), 0);
              } catch (IOException ex) {
                  Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
              }
              System.out.println("Webserver binds to port 9527");
              //createContent();
        }  

        private void createContent(){
            httpServer.createContext("/login.html", new Login());
            httpServer.createContext("/login_check.html", new Login_check());

        }
        
        public void start(){
            init();
            createContent();
            httpServer.start();
        }
        
        private static void WriteHTML(String filepath, HttpExchange t) throws IOException{
            OutputStream os = t.getResponseBody();
            //InputStream is = new InputStream(new FileReader("index.html"));
            File webpage = new File(filepath);
            FileReader webpageReader = new FileReader(webpage);
            BufferedReader br = new BufferedReader(webpageReader);

            t.sendResponseHeaders(200, webpage.length());

            String line;

            while((line = br.readLine()) != null)
            {
                os.write(line.getBytes());
                os.write("\r\n".getBytes());
            }

            //os.write(os.getBytes());
            os.close();
            br.close();
            webpageReader.close();
        }

        private static class Login implements HttpHandler {
            public void handle(HttpExchange t) throws IOException {
              WebServer.WriteHTML("login.html", t);

                
            }
        }
        
        private static class Login_check implements HttpHandler {
            public void handle(HttpExchange t) throws IOException {
              //String response = "Test HttpServer";
                
                InputStreamReader is = new InputStreamReader(t.getRequestBody(),"utf-8");
                BufferedReader reader = new BufferedReader(is);
                String post = reader.readLine();
                System.out.println(post);
                String password="";
                if(post != null){
                    String[] spl1 = post.split("&");
                    String[] spl2 = spl1[0].split("=");
                    //System.out.println(spl2[1]);
                    password = spl2[1];
                }
                if(password.equals("1234")){
                    System.out.println("login succeed");
                    WebServer.WriteHTML("index.html", t);
                }else{
                    WebServer.WriteHTML("login_failed.html", t);
                }
                
            }
        }

      /*
      protected void start() {
        System.out.println("Waiting for connection");
        while (true) {
          try {
            // wait for a connection
            Socket remote = serverSocket.accept();
            // remote is now the connected socket
            System.out.println("Connection, sending data.");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                remote.getInputStream()));
            PrintWriter out = new PrintWriter(remote.getOutputStream());

            // read the data sent. We basically ignore it,
            // stop reading once a blank line is hit. This
            // blank line signals the end of the client HTTP
            // headers.
            String str = ".";
            while (!str.equals(""))
              str = in.readLine();

            // Send the response
            // Send the headers
            out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: text/html");
            out.println("Server: Bot");
            // this blank line signals the end of the headers
            out.println("");

            // Send the HTML page
            BufferedReader n = new BufferedReader(new FileReader("index.html"));
            String line;
            while((line = n.readLine()) != null)
            {
                out.println(line);
            }

            out.flush();
            remote.close();
          } catch (Exception e) {
            System.out.println("Error: " + e);
          }
        }
      }*/

      /* Start the application. Command line parameters are not used. */
      /*public static void main(String args[]) {
        WebServer ws = new WebServer();
        ws.start();
      }*/
        
        
    }