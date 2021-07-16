package it.polito.tdp.denvercrimes;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import it.polito.tdp.denvercrimes.model.Adiacenza;
import it.polito.tdp.denvercrimes.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Integer> boxAnno;

    @FXML
    private ComboBox<Integer> boxMese;

    @FXML
    private ComboBox<Integer> boxGiorno;

    @FXML
    private Button btnCreaReteCittadina;

    @FXML
    private Button btnSimula;

    @FXML
    private TextField txtN;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	int anno = this.boxAnno.getValue();
    	this.model.creaGrafo(anno);
    	
    	for (Integer distretto : this.model.getDistrict()) {
    		txtResult.appendText("\n\nVICINI DEL DISTRETTO: "+distretto+"\n");
    		for(Adiacenza a : this.model.adiacenze(distretto)) {
    			this.txtResult.appendText(a.getDistretto()+", "+a.getPeso()+"\n");
    		}
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.setText("");
    	int N = Integer.parseInt(this.txtN.getText());
    	if(N<0 || N>10) {
    		this.txtResult.setText("Valore di N non valido!\nInserire un numero intero compreso tra 0 e 10.");
    		return;
    	}
    	Integer anno = this.boxAnno.getValue();
    	Integer mese = this.boxMese.getValue();
    	Integer giorno = this.boxGiorno.getValue();
    	
    	if (anno==null || mese==null || giorno==null) {
    		this.txtResult.setText("Data non corretta!\n");
    		return;
    	}
    	
    	try {
    		LocalDate.of(anno, mese, giorno);
    	}
    	catch (DateTimeException e) {
    		this.txtResult.appendText("Data non corretta!\n");
    	}
    	
    	this.txtResult.appendText("Simulo con "+N+" agenti.\n");
    	this.txtResult.appendText("\nCRIMINI MAL GESTITI: "+this.model.simula(anno, mese, giorno, N));
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		
		ObservableList<Integer> anno = FXCollections.observableArrayList();
		for (Integer y : this.model.getAllYears()) {
			anno.add(y);
		}
		this.boxAnno.setItems(anno);
		
		ObservableList<Integer> mese = FXCollections.observableArrayList();
		for (int i=1; i<13; i++) {
			mese.add(i);
		}
		this.boxMese.setItems(mese);
		
		ObservableList<Integer> giorno = FXCollections.observableArrayList();
		for (int i=1; i<32; i++) {
			giorno.add(i);
		}
		this.boxGiorno.setItems(giorno);
		
	}
}
