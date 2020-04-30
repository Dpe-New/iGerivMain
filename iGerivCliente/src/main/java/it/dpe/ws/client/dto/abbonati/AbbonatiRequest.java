/**
 * AbbonatiRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.dpe.ws.client.dto.abbonati;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@XmlRootElement(name = "AbbonatiRequest", namespace="http://it.dpe.rtae/abbonati/schemas")
public class AbbonatiRequest  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private it.dpe.ws.client.dto.abbonati.Abbonati abbonati;

    public AbbonatiRequest() {
    }

    public AbbonatiRequest(
           it.dpe.ws.client.dto.abbonati.Abbonati abbonati) {
           this.abbonati = abbonati;
    }


    /**
     * Gets the abbonati value for this AbbonatiRequest.
     * 
     * @return abbonati
     */
    public it.dpe.ws.client.dto.abbonati.Abbonati getAbbonati() {
        return abbonati;
    }


    /**
     * Sets the abbonati value for this AbbonatiRequest.
     * 
     * @param abbonati
     */
    public void setAbbonati(it.dpe.ws.client.dto.abbonati.Abbonati abbonati) {
        this.abbonati = abbonati;
    }
    
    @XmlTransient
    private java.lang.Object __equalsCalc = null;
    @SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AbbonatiRequest)) return false;
        AbbonatiRequest other = (AbbonatiRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.abbonati==null && other.getAbbonati()==null) || 
             (this.abbonati!=null &&
              this.abbonati.equals(other.getAbbonati())));
        __equalsCalc = null;
        return _equals;
    }
    
    @XmlTransient
    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAbbonati() != null) {
            _hashCode += getAbbonati().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
   /*@XmlTransient
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AbbonatiRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://it.dpe.rtae/abbonati/schemas", ">AbbonatiRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("abbonati");
        elemField.setXmlName(new javax.xml.namespace.QName("http://it.dpe.rtae/abbonati/schemas", "Abbonati"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://it.dpe.rtae/abbonati/schemas", ">Abbonati"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }*/

    /**
     * Return type metadata object
     */
    /*public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }*/

    /**
     * Get Custom Serializer
     */
    /*public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }*/

    /**
     * Get Custom Deserializer
     */
    /*public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }*/

}
