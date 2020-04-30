/**
 * Ricaricabili.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.dpe.ws.client.dto.ricaricabili;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@XmlRootElement(name="Ricaricabili", namespace="http://it.dpe.rtae/ricaricabili/schemas")
public class Ricaricabili  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name="Tessera", namespace="http://it.dpe.rtae/ricaricabili/schemas")
	private Long tessera;
	
	@XmlElement(name="IdEditore", namespace="http://it.dpe.rtae/ricaricabili/schemas")
    private Integer idEditore;

	@XmlElement(name="IdRivendita", namespace="http://it.dpe.rtae/ricaricabili/schemas")
    private Integer idRivendita;

	@XmlElement(name="CodFiegDl", namespace="http://it.dpe.rtae/ricaricabili/schemas")
    private Integer codFiegDl;
	
	@XmlElement(name="Barcode", namespace="http://it.dpe.rtae/ricaricabili/schemas")
    private Long barcode;

	@XmlElement(name="Edizione", namespace="http://it.dpe.rtae/ricaricabili/schemas")
    private java.lang.String edizione;

	@XmlElement(name="TipologiaMinicard", namespace="http://it.dpe.rtae/ricaricabili/schemas")
    private Integer tipologiaMinicard;

	@XmlElement(name="Operation", namespace="http://it.dpe.rtae/ricaricabili/schemas")
    private java.lang.String operation;
	
	@XmlElement(name="IdProdotto", namespace="http://it.dpe.rtae/ricaricabili/schemas")
    private Integer idProdotto;

    public Ricaricabili() {
    }

    public Ricaricabili(
    	   Long tessera,
           Integer idEditore,
           Integer idRivendita,
           Integer codFiegDl,
           Long barcode,
           java.lang.String edizione,
           Integer tipologiaMinicard,
           java.lang.String operation) {
           this.tessera = tessera;
           this.idEditore = idEditore;
           this.idRivendita = idRivendita;
           this.codFiegDl = codFiegDl;
           this.barcode = barcode;
           this.edizione = edizione;
           this.tipologiaMinicard = tipologiaMinicard;
           this.operation = operation;
    }


    /**
     * Gets the tessera value for this Ricaricabili.
     * 
     * @return tessera
     */
    public Long getTessera() {
        return tessera;
    }


    /**
     * Sets the tessera value for this Ricaricabili.
     * 
     * @param tessera
     */
    public void setTessera(Long tessera) {
        this.tessera = tessera;
    }


    /**
     * Gets the idEditore value for this Ricaricabili.
     * 
     * @return idEditore
     */
    public Integer getIdEditore() {
        return idEditore;
    }


    /**
     * Sets the idEditore value for this Ricaricabili.
     * 
     * @param idEditore
     */
    public void setIdEditore(Integer idEditore) {
        this.idEditore = idEditore;
    }


    /**
     * Gets the idRivendita value for this Ricaricabili.
     * 
     * @return idRivendita
     */
    public Integer getIdRivendita() {
        return idRivendita;
    }


    /**
     * Sets the idRivendita value for this Ricaricabili.
     * 
     * @param idRivendita
     */
    public void setIdRivendita(Integer idRivendita) {
        this.idRivendita = idRivendita;
    }


    /**
     * Gets the codFiegDl value for this Ricaricabili.
     * 
     * @return codFiegDl
     */
    public Integer getCodFiegDl() {
        return codFiegDl;
    }


    /**
     * Sets the codFiegDl value for this Ricaricabili.
     * 
     * @param codFiegDl
     */
    public void setCodFiegDl(Integer codFiegDl) {
        this.codFiegDl = codFiegDl;
    }


    /**
     * Gets the barcode value for this Ricaricabili.
     * 
     * @return barcode
     */
    public Long getBarcode() {
        return barcode;
    }


    /**
     * Sets the barcode value for this Ricaricabili.
     * 
     * @param barcode
     */
    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }


    /**
     * Gets the edizione value for this Ricaricabili.
     * 
     * @return edizione
     */
    public java.lang.String getEdizione() {
        return edizione;
    }


    /**
     * Sets the edizione value for this Ricaricabili.
     * 
     * @param edizione
     */
    public void setEdizione(java.lang.String edizione) {
        this.edizione = edizione;
    }


    /**
     * Gets the tipologiaMinicard value for this Ricaricabili.
     * 
     * @return tipologiaMinicard
     */
    public Integer getTipologiaMinicard() {
        return tipologiaMinicard;
    }


    /**
     * Sets the tipologiaMinicard value for this Ricaricabili.
     * 
     * @param tipologiaMinicard
     */
    public void setTipologiaMinicard(Integer tipologiaMinicard) {
        this.tipologiaMinicard = tipologiaMinicard;
    }


    /**
     * Gets the operation value for this Ricaricabili.
     * 
     * @return operation
     */
    public java.lang.String getOperation() {
        return operation;
    }


    /**
     * Sets the operation value for this Ricaricabili.
     * 
     * @param operation
     */
    public void setOperation(java.lang.String operation) {
        this.operation = operation;
    }
    
    /**
     * Gets the idProdotto value for this Ricaricabili.
     * 
     * @return idProdotto
     */
    public Integer getIdProdotto() {
        return idProdotto;
    }


    /**
     * Sets the idProdotto value for this Ricaricabili.
     * 
     * @param idProdotto
     */
    public void setIdProdotto(Integer idProdotto) {
        this.idProdotto = idProdotto;
    }

    
    private java.lang.Object __equalsCalc = null;
    @SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Ricaricabili)) return false;
        Ricaricabili other = (Ricaricabili) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.tessera == other.getTessera() &&
            ((this.idEditore==null && other.getIdEditore()==null) || 
             (this.idEditore!=null &&
              this.idEditore.equals(other.getIdEditore()))) &&
            ((this.idRivendita==null && other.getIdRivendita()==null) || 
             (this.idRivendita!=null &&
              this.idRivendita.equals(other.getIdRivendita()))) &&
            ((this.codFiegDl==null && other.getCodFiegDl()==null) || 
             (this.codFiegDl!=null &&
              this.codFiegDl.equals(other.getCodFiegDl()))) &&
            this.barcode == other.getBarcode() &&
            ((this.edizione==null && other.getEdizione()==null) || 
             (this.edizione!=null &&
              this.edizione.equals(other.getEdizione()))) &&
            ((this.tipologiaMinicard==null && other.getTipologiaMinicard()==null) || 
             (this.tipologiaMinicard!=null &&
              this.tipologiaMinicard.equals(other.getTipologiaMinicard()))) &&
            ((this.operation==null && other.getOperation()==null) || 
             (this.operation!=null &&
              this.operation.equals(other.getOperation())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += new Long(getTessera()).hashCode();
        if (getIdEditore() != null) {
            _hashCode += getIdEditore().hashCode();
        }
        if (getIdRivendita() != null) {
            _hashCode += getIdRivendita().hashCode();
        }
        if (getCodFiegDl() != null) {
            _hashCode += getCodFiegDl().hashCode();
        }
        _hashCode += new Long(getBarcode()).hashCode();
        if (getEdizione() != null) {
            _hashCode += getEdizione().hashCode();
        }
        if (getTipologiaMinicard() != null) {
            _hashCode += getTipologiaMinicard().hashCode();
        }
        if (getOperation() != null) {
            _hashCode += getOperation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }
   

}
