//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 generiert 
// Siehe <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.11.26 um 01:58:23 AM CET 
//


package generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für basicService complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="basicService"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Node"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="quadruple" type="{}quadruple" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "basicService", propOrder = {
    "quadruple"
})
public class BasicService
    extends Node
{

    @XmlElement(required = true)
    protected List<Quadruple> quadruple;

    /**
     * Gets the value of the quadruple property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the quadruple property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuadruple().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Quadruple }
     * 
     * 
     */
    public List<Quadruple> getQuadruple() {
        if (quadruple == null) {
            quadruple = new ArrayList<Quadruple>();
        }
        return this.quadruple;
    }

}
