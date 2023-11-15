
package proyectologin;

import com.mysql.cj.xdevapi.PreparableStatement;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import javax.swing.Timer;
import java.util.Timer;
import java.util.TimerTask;

public class ProyectoLogin {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, InterruptedException {
      
      try{
          String url="jdbc:mysql://localhost:3306/test";
          String usuario="root";
          String pass="";  
          Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
          Connection conexion = (Connection) DriverManager.getConnection(url, usuario, pass);
          System.out.println("Conexion exitosa");
      boolean ingreso;
      int opc=0;
      
      do{
      opc=Integer.parseInt(JOptionPane.showInputDialog("Ingrese \n1 para Login\n2 para registrarse \n3 para salir", "1"));
      if (opc==1){
        ingreso=login(conexion);
        if (ingreso==false){
            JOptionPane.showMessageDialog(null, "Usuario Bloqueado");
            Timer timer = new Timer();
           
        TimerTask tarea = new TimerTask() {
            int segundosRestantes = 15;

            @Override
            public void run() {
                if (segundosRestantes > 0) {
                    System.out.println("Segundos restantes: " + segundosRestantes);
                    segundosRestantes--;
                } else {
                    System.out.println("¡Tiempo completado!");
                    timer.cancel(); 
                }
            }
        };
        timer.scheduleAtFixedRate(tarea, 0, 1000);  

            Thread.sleep(15000);
        }
        else{
            JOptionPane.showMessageDialog(null, "Acceso concedido");
        }
      }
      else if(opc==2){
          registro(conexion);
      }
      else if (opc==3){
          break;
      }
      else{
            JOptionPane.showMessageDialog(null, "Opcion no valida, ingrese otra opcion");
        }
      }while (opc<4);
      conexion.close();
      }
      catch(SQLException e){
          System.out.println("Error al conectar la base de datos"+e.getMessage());
      }
    }
    
    private static boolean login (Connection conexion) throws SQLException{
        String user="";
        String contrasena="";
        Statement statement = conexion.createStatement();
        String contra="";
        String userQuery="";
        int cont =0;
        boolean exitoso=false;
        while (exitoso==false && cont<3){
            user=JOptionPane.showInputDialog("Usuario", user);
            contrasena=JOptionPane.showInputDialog("Contraseña", contrasena);
            ResultSet resul=statement.executeQuery("SELECT * FROM login WHERE usuario='"+user+"' or correo= '"+user+"'");
            while (resul.next()){
                userQuery=resul.getString("usuario");
                contra = resul.getString("contra");
            }
                
            if (!userQuery.equals("") && contrasena.equals(contra)){
                exitoso=true;
            }
            else {                
                JOptionPane.showMessageDialog(null, "Usuario o contrasena inconrrecta");
            }
            cont++;
        
        }
        return exitoso;
    }
    
    private static void registro (Connection conexion) throws SQLException{
        String user="";
        String correo = "";
        String contrasena="";
         user=JOptionPane.showInputDialog("Ingrese el nuevo usuario", user);
         correo=JOptionPane.showInputDialog("Ingrese el nuevo correo", correo);
         contrasena=JOptionPane.showInputDialog("Ingrese la nueva contraseña", contrasena);
         
         PreparedStatement statement = conexion.prepareStatement("Insert into login (usuario, correo, contra) values (?,?,?)");
         statement.setString(1, user);
         statement.setString(2, correo);
         statement.setString(3, contrasena);
        statement.executeUpdate();
        
        JOptionPane.showMessageDialog(null, "Usuario nuevo registrado");
               
    }
}
