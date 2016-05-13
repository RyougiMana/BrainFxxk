package serviceImpl;

import java.rmi.RemoteException;

import service.UserService;

public class UserServiceImpl implements UserService{
	
	String[][] user = {
			{"user1", "123"}, 
			{"user2", "123"},
	};
	
	@Override
	public boolean login(String username, String password) throws RemoteException {
		return getUser(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		return getUser(username);
	}
	
	public boolean getUser(String username, String password){
		for(int i=0; i<user.length; i++){
			if(username.equals(user[i][0])){
				if(password.equals(user[i][1])){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean getUser(String username){
		for(int i=0; i<user.length; i++){
			if(username.equals(user[i][0])){
				return true;
			}
		}
		return false;
	}

}
