/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LDAPZugriff {

    public ArrayList<HashMap> LDAPSearch(String Attribut, String searchDaten) {
        ArrayList<HashMap> daten = new ArrayList<HashMap>();
        HashMap info;
        Hashtable env = new Hashtable();

        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://mailsdag.de.signintra.com:389/l=stinnes,c=de");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "uid=manfred.fischer@dbschenker.com,ou=system");
        env.put(Context.SECURITY_CREDENTIALS, "");
        DirContext ctx = null;
        NamingEnumeration results = null;
        try {
            ctx = new InitialDirContext(env);
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = ctx.search("", "(" + Attribut + "=" + searchDaten + ")", controls);
            while (results.hasMore()) {
                try {
                    info = new HashMap();
                    SearchResult searchResult = (SearchResult) results.next();
                    Attributes attributes = searchResult.getAttributes();
                    Attribute tel = attributes.get("telephoneNumber");
                    Attribute fax = attributes.get("facsimileTelephoneNumber");
                    Attribute email = attributes.get("mail");
                    Attribute firstName = attributes.get("givenName");
                    Attribute lastName = attributes.get("sn");
                    Attribute displayName = attributes.get("displayName");
                    Attribute city = attributes.get("I");
                    Attribute plz = attributes.get("postalCode");
                    Attribute street = attributes.get("streetAddress");
                    Attribute mobile = attributes.get("mobile");


                    info.put("tel", checkDaten(tel));
                    info.put("fax", checkDaten(fax));
                    info.put("email", checkDaten(email));
                    info.put("firstName", checkDaten(firstName));
                    info.put("lastName", checkDaten(lastName));
                    info.put("displayName", checkDaten(displayName));
                    info.put("ort", checkDaten(city));
                    info.put("plz", checkDaten(plz));
                    info.put("strasse", checkDaten(street));
                    info.put("mobile", checkDaten(mobile));
                    daten.add(info);
                } catch (Exception e) {
                }
            }
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                }
            }
        }
        return daten;
    }

    private String checkDaten(Attribute at) {
        try {
            if (at != null) {
                return (String) at.get();
            } else {
                return "--";
            }
        } catch (Exception e) {
            return "--";
        }
    }
}
