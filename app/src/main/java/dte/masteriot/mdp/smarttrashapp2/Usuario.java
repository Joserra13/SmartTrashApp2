package dte.masteriot.mdp.smarttrashapp2;

public class Usuario {
    private String username;
    private String password;

    public Usuario(String Name, String Password){
        this.username = Name;
        this.password = Password;
    }

    public String getName(){
        return username;
    }
    public String getPassword(){
        return password;
    }

}
