import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.Hashtable;

/**
 * @author <a href="kien.nguyen@niteco.se">Kien Nguyen</a>
 */
public class LdapBasicExample {
    public static void main(String[] args) {
        String userName = "khoi.nguyen";
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://ip-10-34-244-133.eu-west-1.compute.internal/dc=bonnierdigital,dc=se");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, new String(userName));
        env.put(Context.SECURITY_CREDENTIALS, "niteco0729");

        DirContext ctx = null;
        NamingEnumeration results = null;
        try {
            ctx = new InitialDirContext(env);
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = ctx.search("", "(objectclass=person)", controls);
            while (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();
                Attributes attributes = searchResult.getAttributes();
                Attribute attr = attributes.get("cn");
                String cn = (String) attr.get();
                System.out.println(" Person Common Name = " + attributes.get("cn"));
                System.out.println(" Person Display Name = " + attributes.get("displayName"));
                System.out.println(" Person logonhours = " + attributes.get("logonhours"));
                System.out.println(" Person MemberOf = " + attributes.get("memberOf"));
            }
        } catch (Throwable e) {
            e.printStackTrace();
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
    }

}
