/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Opponents;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    	double minimo=0;
    	try {
    		minimo= Double.parseDouble(txtGoals.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("inserire un numero!");
    		return;
    	}
    	model.creaGrafo(minimo);
    	txtResult.appendText("Grafo creato\n");
    	txtResult.appendText("# vertici: "+ model.numVertici()+"\n");
    	txtResult.appendText("# archi: "+ model.numArchi()+ "\n");
    }

    @FXML
    void doDreamTeam(ActionEvent event) {

    	txtResult.clear();
    	if(!model.grafoCreato()) {
    		txtResult.appendText("creare prima il grafo");
    		return;
    	}
    	int k;
    	try {
    		k= Integer.parseInt(txtK.getText());
    		
    	}catch(NumberFormatException e) {
    		txtResult.appendText("inserire un numero");
    		return;
    	}
    	model.ricorsione(k);
    	txtResult.appendText("Dream team trovato con peso = "+model.getGradoMax()+"\n");
    	for(Player p: model.getSoluzione()) {
    		txtResult.appendText(p+"\n");
    	}
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	txtResult.clear();
    	
    	Player top= model.topPlayer();
    	if(top==null) {
    		txtResult.appendText("non è stato possibile define un top player, ricordarsi di creare il grafo");
    		return;
    	}
    	txtResult.appendText("TOP PLAYER: "+ top+ "\n\n");
    	List<Opponents> opp= model.getBattuti(top);
    	for(Opponents o: opp) {
    		txtResult.appendText(o.getPlayer()+ " |"+ o.getNum()+ "\n");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
