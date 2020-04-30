/**
 * RicaricabiliRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.dpe.ws.client.dto.ricaricabili;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@XmlRootElement(name = "RicaricabiliRequest", namespace="http://it.dpe.rtae/ricaricabili/schemas")
public class RicaricabiliRequest  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Ricaricabili ricaricabili;

    public RicaricabiliRequest() {
    }

    public RicaricabiliRequest(
           Ricaricabili ricaricabili) {
           this.ricaricabili = ricaricabili;
    }


    /**
     * Gets the ricaricabili value for this RicaricabiliRequest.
     * 
     * @return ricaricabili
     */
    public Ricaricabili getRicaricabili() {
        return ricaricabili;
    }


    /**
     * Sets the ricaricabili value for this RicaricabiliRequest.
     * 
     * @param ricaricabili
     */
    public void setRicaricabili(Ricaricabili ricaricabili) {
        this.ricaricabili = ricaricabili;
    }
    
    @XmlTransient
    private java.lang.Object __equalsCalc = null;
    @SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RicaricabiliRequest)) return false;
        RicaricabiliRequest other = (RicaricabiliRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ricaricabili==null && other.getRicaricabili()==null) || 
             (this.ricaricabili!=null &&
              this.ricaricabili.equals(other.getRicaricabili())));
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
        if (getRicaricabili() != null) {
            _hashCode += getRicaricabili().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    /*private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RicaricabiliRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://it.dpe.rtae/ricaricabili/schemas", ">RicaricabiliRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ricaricabili");
        elemField.setXmlName(new javax.xml.namespace.QName("http://it.dpe.rtae/ricaricabili/schemas", "Ricaricabili"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://it.dpe.rtae/ricaricabili/schemas", ">Ricaricabili"));
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
