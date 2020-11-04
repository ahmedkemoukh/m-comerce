package BD;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Utilisateur;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.Compte;

import fonction.Fonction;




public class ConfigBD {
	static SessionFactory factor=new Configuration().configure().buildSessionFactory();
	public  Session CurrentSession;
	public  void connexionBD()
	{
		CurrentSession=factor.openSession();
		CurrentSession.beginTransaction();
	
		
	}
	
	
	public static void main(String[] args) {
		
		
		
	/*	ConfigBD bd=new ConfigBD();bd.connexionBD();
		Query query=bd.CurrentSession.createQuery("from Vente v where  v.article.user.id_C=:cod");
		query.setParameter("cod",1);
		List<Vente> us1 = (List<Vente>)query.list();
		bd.CurrentSession.close();
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		Query query=bd.CurrentSession.createQuery("from Utilisateur where  id_C=:cod");
		query.setParameter("cod",1);
		Utilisateur us1 = (Utilisateur)query.uniqueResult();
		ArrayList<Vente> vent= new ArrayList<Vente>(us1.getListVent());
		bd.CurrentSession.close();
		System.out.println(vent.get(0).getUser());*/
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		Compte c=bd.CurrentSession.get(Compte.class,1);
		Compte c2=bd.CurrentSession.get(Compte.class,2);
		System.out.println(c.getMony());
		c2.setMony(c2.getMony()+1000);
		c.setMony(3000);
		bd.CurrentSession.update(c);
		bd.CurrentSession.update(c2);
		bd.CurrentSession.getTransaction().commit();
		bd.CurrentSession.close();
 		
	
		

	}

}
