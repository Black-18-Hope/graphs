package csc3a.File;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import csc3a.model.Graph;
import csc3a.model.MobileClinic;


public class FileHandler {
	
	/**
	 * function for validating user credentials
	 * @param Username the user name of client
	 * @param PassWord the password of the client
	 * @param TYPE type of client or user
	 * @return boolean value whether the credentials were valid or not
	 */
	
	public static boolean AuthenticateUser(String Username, String PassWord,String TYPE) {
		Scanner Input = null;
		try 
		{ 
			if(TYPE.equals("user")) {
				Input = new Scanner(new File("./resources/data/logins/users.txt"));
			}else {
				Input = new Scanner(new File("./resources/data/logins/admins.txt"));
			}
			String User = "";
			while(Input.hasNextLine())
			{
				User = Input.nextLine();
				String [] UserCredentials = User.split(" ");
				if(UserCredentials[0].equals(Username) && UserCredentials[1].equals(PassWord)){
					TYPE = UserCredentials[2];
					return true;
				}
			}
			return false;
		}catch(Exception ex){
			System.err.println(ex.getMessage());
		}finally {
			if(Input != null){
				Input.close();
			}
		}
		return false;
	}
	
	/**
	 * Function to create user credentials.
	 * @param username the user name of the client
	 * @param password the password of the client
	 * @param type the type of the client
	 */
	public static boolean createUserCredentials(String userName,String password, String type) {
		try {
			if (userName == null || userName.isEmpty()  || password == null || password.isEmpty() || type == null || type.isEmpty()) {
		        return false;
		    }
			User user = new User(userName, password, type);
			FileWriter writer;
			if(type.equals("user")) {
				writer=new FileWriter("./resources/data/logins/users.txt", true);
			}else {
				writer=new FileWriter("./resources/data/logins/admins.txt", true);

			}
			PrintWriter printer = new PrintWriter(writer);
			printer.println(user.userName()+" "+ user.getPassword() + " " + user.getRole());
			printer.close();
			writer.close();
			System.out.println("User credentials created successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Saves the graph in a binary file
	 */
	/**
	 * @param 
	 */
	/**
	 * @param graph the current existing graph
	 * @param file, the file that data is saved on
	 * @return true or false based on whether the data has been saved or not
	 */
	public static boolean saveAll(Graph<MobileClinic> graph, File file)
	{
		ObjectOutputStream os =null;

		try {
			FileOutputStream fos = new FileOutputStream(file);

			BufferedOutputStream bos = new BufferedOutputStream(fos);

			os = new ObjectOutputStream(bos);

			os.writeObject(graph);


		} catch (Exception e) {

			System.err.println(e.getMessage());
			return false;
		}finally {

			if(os != null)
			{
				try {
					os.close();
					return true;//for success
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		return false;

	}
	/**
	 * @param file, file read from
	 * @return the graph instance
	 */
	@SuppressWarnings("unchecked")
	public static Graph<MobileClinic> readAll(File file)
	{
		ObjectInputStream os =null;
		Graph<MobileClinic> graph = null;

		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);

			os = new ObjectInputStream(bis);

			Object object = os.readObject();
			if(object instanceof Graph)
			{
				graph = (Graph<MobileClinic>)object;
			}

			
		} catch (Exception e) {

			System.err.println(e.getMessage());
			return null;
		}finally {

			if(os != null)
			{
				try {
					os.close();
					return graph;// success
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		return null;

	}
	
	public static void logUser(String username, String date, double d) {
		Random random= new Random();
	    double lat = d + (random.nextDouble() - 0.5) * 1.0;
	    double lon = d + (random.nextDouble() - 0.5) * 1.0;
	    String latStr = String.format("%.6f", lat);
	    String lonStr = String.format("%.6f", lon);
	    String gpsCoord = "[" + latStr + "," + lonStr + "]";

	    try {
	        PrintWriter writer = new PrintWriter(new FileWriter("./resources/data/log.txt", true));
	        writer.printf("%s\t%s\t%s\n", username, date, gpsCoord);
	        writer.close();
	    } catch (IOException e) {
	        System.err.println("Error writing log file: " + e.getMessage());
	    }
	}


}
