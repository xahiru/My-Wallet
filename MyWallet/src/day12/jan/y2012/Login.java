package day12.jan.y2012;
/*
 * This class is used to authenticate user. To create an object of this class
 * a username and a password should be provided.
 * Two method exist in this class. 
 */

public class Login {
	
	

	
	private String storedUsername = MyWalletApplication.username;
	private String storedPassword= MyWalletApplication.password;
	private String passedUsername;
	private String passedPassword;
	
	
	public Login( String passedUsername, String passedPassword) {
		super();

		
	
//		this.storedUsername = MyWalletApplication.username;
	
//		this.storedPassword = MyWalletApplication.password;
		
		this.passedUsername = passedUsername;
		this.passedPassword = passedPassword;
	}
	
	
	/*
	 * This method checks wether the given user name and password matches with
	 * the one stored in the preference
	 * If both username and password matches true is returned, otherwise false is returned
	 * calling object should handle the rest
	 */
	public boolean authenticate() {
		

		if(storedUsername.equals(passedUsername)){
			if(storedPassword.equals(passedPassword))
				return true;
			else
			{
//				Toast.makeText(null, R.string.loginFailed, Toast.LENGTH_SHORT).show();
				return false;
			}
		}else
		{
//			Toast.makeText(null, R.string.loginFailed, Toast.LENGTH_SHORT).show();
			return false;
			
		}
		
		
		
	}	
	
}
