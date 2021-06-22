package tech.kitucode.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class App
{

    public static void main(String[] args )
    {

        int port = 12000;

        final Map<String, String> countries = new HashMap<>();

        countries.put("Kenya","Nairobi");
        countries.put("Tanzania","Dodoma");
        countries.put("Uganda","Kampala");
        countries.put("Somalia","Mogadishu");
        countries.put("England","London");
        countries.put("Nigeria","Lagos");

        System.out.println(countries);

        try {
            final ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("demo|server connected with port : "+serverSocket.getLocalPort());

            System.out.println("demo|waiting for connections:");

            while(true){

                final Socket socket = serverSocket.accept();

                System.out.println("demo|starting a connection to : "+socket.getInetAddress() + " at port : "+socket.getPort());

                // replaced 'new Runnable' with a lambda
                Thread thread = new Thread(() -> {
                    try {

                        System.out.println("demo|client connected with port : "+socket.getPort()+" on thread : "+Thread.currentThread().getName());

                        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        String inputLine;

                        while((inputLine=in.readLine())!=null){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            out.println(countries.getOrDefault(capitalize(inputLine),"not found"));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                thread.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String capitalize(String originalString){
        return originalString.substring(0,1).toUpperCase() + originalString.substring(1).toLowerCase();
    }
}
