package csc3a.File;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
    private String password;
    private String role;

    public User(String name,String password, String role) {
        this.userName = name;
        this.password = password;
        this.role = role;
    }

    public String userName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void userName(String name) {
        this.userName = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

	@Override
	public int compareTo(User o) {
		if(!o.userName.equals(userName)) {
			return -1;
		}if(!o.password.equals(password)) {
			return 1;
		}
		return 0;
	}


}
