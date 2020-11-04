package fonction;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import com.allandroidprojects.ecomsample.Mcommerce.Ach1;
import com.allandroidprojects.ecomsample.Mcommerce.ArticlRech;
import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Utilisateur;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.Compte;
import com.allandroidprojects.ecomsample.Mcommerce.LingeCommand;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;

import BD.ConfigBD;
import Container.MainContainer;


public class Fonction {


	
	public List<Article> ListArticle(String s,int user)
	{
		ConfigBD bd=new ConfigBD();
		
		bd.connexionBD();
		Query query=bd.CurrentSession.createQuery("from Article a where a.markeplace.id=:user and 	libCat_p=:lib");
		query.setParameter("user",user);
		query.setParameter("lib",s);
		List<Article> list=(List<Article>)query.list();
		for(Article a:list)
		{
		
			a.setImags_p(new ArrayList<String>(a.getImags_p()));
			a.getMarkeplace().setListArticle(null);
			a.getMarkeplace().getUser().setListVentAchteur(null);
		    a.getMarkeplace().getUser().setListVentVendeur(null);
			

		}
		bd.CurrentSession.close();

		
	
	/*	ArrayList<Article> list1=new ArrayList<Article>();
		 
		for(Article A:list)
		{
			Article A1=new Article();
			/*A1.setUser(A.getUser());
			A1.setQuanSt_p(A.getQuanSt_p());
			A1.setPrix_P(A.getPrix_P());
			A1.setNom_p(A.getNom_p());
			A1.setMarque_p(A.getMarque_p());
			A1.setLibCat_p(A.getLibCat_p());
			A1.setImags_p(A.getImags_p());
			A1.setId_P(A.getId_P());
			A1.setDescription_p(A.getDescription_p());
			A1.setNom_p(A.getNom_p());
			A1.setImags_p(A.getImags_p());
			A1.setUser(A.getUser());
			System.out.println(A.getUser());
			list1.add(A1);
			
		}*/
	
		return list;
		//return list;
	}
	
	public List<Article> recharcher(ArticlRech A,int user)
	{
	ConfigBD bd=new ConfigBD();bd.connexionBD();
	String req="a.markeplace.id=:user ";
	if(A.getPrix()!=0)
	{
		req=req+"and a.Prix_P<=:prix ";
	}
	if(!A.getMarque().isEmpty())
	{
		req=req+"and a.Marque_p= :marq "; 
	}
	
	if(!A.getNom().isEmpty())
	{
		req=req+"and a.Nom_p= :nom "; 	
	}
	if(!A.getGategori().isEmpty())
	{
		req=req+"and a.libCat_p= :categori "; 	
	}
	
	
		Query query=bd.CurrentSession.createQuery("from Article a where "+req);
		if(!A.getMarque().isEmpty())
		{
		query.setParameter("marq",A.getMarque());
		}
		if(A.getPrix()!=0)
		{
		query.setParameter("prix",A.getPrix());
		}
		query.setParameter("user",user);
		if(!A.getGategori().isEmpty())
		{
		query.setParameter("categori",A.getGategori());
		}
		if(!A.getNom().isEmpty())
		{
		query.setParameter("nom",A.getNom());
		}
	  System.out.println(query.getQueryString());
		List<Article> list=(List<Article>)query.list();
		System.out.println("resulta"+list.isEmpty()+" "+A.getMarque()+" "+A.getPrix()+" "+user);
		for(Article a:list)
		{
		
			a.setImags_p(new ArrayList<String>(a.getImags_p()));
			a.getMarkeplace().setListArticle(null);
			a.getMarkeplace().getUser().setListVentAchteur(null);
		    a.getMarkeplace().getUser().setListVentVendeur(null);
			

		}
		bd.CurrentSession.close();
		return list;
		
	}
	
	public void listPaiment(ArrayList<Article> a)
	{
		
	}
	
	public Utilisateur authentifier(Utilisateur u)
	{
	ConfigBD bd=new ConfigBD();bd.connexionBD();
		Query query=bd.CurrentSession.createQuery("from Utilisateur where Email_U=:email and Password_U=:pas");
		query.setParameter("email",u.getEmail_U());
		query.setParameter("pas",u.getPassword_U());
		Utilisateur us = (Utilisateur)query.uniqueResult();
		if(us!=null)
		{
			//**us.setListMarkplace(null);
		   us.setListVentAchteur(null);
		   us.setListVentVendeur(null);
		   if(us.getMarkplace()!=null)
			{
			   us.getMarkplace().setUser(null);
			   us.getMarkplace().setListArticle(null);
			}
		}
		bd.CurrentSession.close();
		return us;
		
	}
	
	public Utilisateur insription(Utilisateur u)
	{
	ConfigBD bd=new ConfigBD();bd.connexionBD();
	try
	{
		bd.CurrentSession.saveOrUpdate(u);
		bd.CurrentSession.getTransaction().commit();
		bd.CurrentSession.close();
	}
	catch(Exception e){
		System.out.print("email exist");
		u=null;
	}
		return u;
	}
	
	public Utilisateur getUtilisateur(String u)
	{
	ConfigBD bd=new ConfigBD();bd.connexionBD();
		Query query=bd.CurrentSession.createQuery("from Utilisateur u where CONCAT(u.Nom_U,' ',u.Prenom_U)=:user");
		query.setParameter("user",u);
	
		Utilisateur us = (Utilisateur)query.uniqueResult();
		if(us!=null)
		{
			//**us.setListMarkplace(null);
		   us.setListVentAchteur(null);
		   us.setListVentVendeur(null);
		if(us.getMarkplace()!=null)
		{
		   us.getMarkplace().setUser(null);
		   us.getMarkplace().setListArticle(null);
		}
		 
		}
		bd.CurrentSession.close();
		return us;	
	}
	
	public Utilisateur verCodPost(String cod)
	{
	ConfigBD bd=new ConfigBD();bd.connexionBD();
		Query query=bd.CurrentSession.createQuery("from Utilisateur where  Codepostal_U=:cod");
		query.setParameter("cod",cod);
		Utilisateur us = (Utilisateur)query.uniqueResult();
		bd.CurrentSession.close();
		if(us!=null)
		{
			//**us.setListArticle(null);
		    //**us.setListVent(null);
		}
		return us;
	}
	
	public int calculprix(ArrayList<Article> listA)
	{
		int prix=0;
		for(Article a:listA)
		{
			prix=prix+a.getPrix_P();
		}
		return prix;
	}
	
	public Command paiment(Command v)
	{
		

		//**Utilisateur ach=verCodPost(v.getUser().getCodepostal_C());
		/**Utilisateur vend=verCodPost(v.getArticle().getUser().getCodepostal_C());
		if(ach!=null&& vend!=null)
		{
	 vend.setMony(vend.getMony()+v.getArticle().getPrix_P());
	 ach.setMony(ach.getMony()-v.getArticle().getPrix_P());
	ConfigBD bd=new ConfigBD();bd.connexionBD();
	 bd.CurrentSession.update(ach);
	 bd.CurrentSession.update(vend);
	bd.CurrentSession.getTransaction().commit();
	bd.CurrentSession.close();
	v.setUser(ach);
	v.getArticle().setUser(vend);
	v.setEtat(1);
	v.setMessageerror("suceess achat");
	System.out.println("sucess");
		}
		else
		{
			v.setEtat(0);
			v.setMessageerror("error achat");
			
			System.err.println("error payment");
		}*/
		   //a.getUser().setMony(a.getUser().getMony()+a.getPrix_P());
		
		  // us.setMony(us.getMony()-a.getPrix_P());
		  // bd.CurrentSession.update(a.getUser());
		
		
return v;
	}
	
	
	
	public void vendeur()
	{
	ConfigBD bd=new ConfigBD();bd.connexionBD();
		Query query=bd.CurrentSession.createQuery("select  CONCAT(u.Nom_U,' ',u.Prenom_U) from Utilisateur u where u.type=1");
		MainContainer.listVendeur = (List<String>)query.getResultList();
		bd.CurrentSession.close();
	
	}

	public void saveVent(Command vent) {
		ConfigBD bd=new ConfigBD();bd.connexionBD();
	
		bd.CurrentSession.save(vent);
		for(LingeCommand lineC:vent.getLignsCommand())
		{
			lineC.setCommand(vent);
		bd.CurrentSession.save(lineC);
		}
		bd.CurrentSession.getTransaction().commit();
		bd.CurrentSession.close();
		vent.setLignsCommand(new ArrayList(vent.getLignsCommand()));
	}
	
	public String addImage(byte[] image)
	{
		 String id="";
		try
		{
		ByteArrayInputStream input_stream= new ByteArrayInputStream(image);
	      BufferedImage final_buffered_image = ImageIO.read(input_stream);
	      Calendar c=Calendar.getInstance();
	     id=c.getTimeInMillis()+".jpg";
	      ImageIO.write(final_buffered_image , "jpg", new File("E:\\projetPhp\\"+id) );
	      System.out.println("Converted Successfully!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return id;
	}
	
	public void saveProduit(Article a)
	{
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		bd.CurrentSession.saveOrUpdate(a);
		bd.CurrentSession.getTransaction().commit();
		bd.CurrentSession.close();
	}
	
	
	public void suprimerArticle(Article a)
	{
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		bd.CurrentSession.delete(a);
		bd.CurrentSession.getTransaction().commit();
		bd.CurrentSession.close();	
	}
	
	
	public Utilisateur listVent(Utilisateur us)
	{
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		Utilisateur u=bd.CurrentSession.get(Utilisateur.class,us.getId_U());
	//**	query.setParameter("cod",us.getId_C());
		  System.out.println(u.getListVentAchteur().getClass()+"*******listvent*****************");
		
		if(u!=null)
		{
		 u.setListVentAchteur(null);
		   u.setListVentVendeur(new ArrayList(u.getListVentVendeur()));
		u.getMarkplace().setUser(null);
		u.getMarkplace().setListArticle(null);
	u.setCompte(null);

		  for(Command c:u.getListVentVendeur())
		  {
			  System.out.println("*******listlisventtcomand*****************");
			
			
			  
			 c.getAchteur().setMarkplace(null);
			
			  c.getAchteur().setCompte(null);
	       c.getAchteur().setListVentVendeur(null);
	          c.getAchteur().setListVentAchteur(null);
			  
	        
	        
			  c.getVendeur().getMarkplace().setListArticle(null);
			  c.getVendeur().getMarkplace().setUser(null);
			  c.getVendeur().setCompte(null);
			   c.getVendeur().setListVentAchteur(null);
		         
		          c.setLignsCommand(new ArrayList(c.getLignsCommand()));
		          for(LingeCommand lin:c.getLignsCommand())
		          {
		        	 
		        	  lin.setCommand(null);
		        	 lin.getArticle().getMarkeplace().setListArticle(null);
		        	 lin.getArticle().getMarkeplace().setUser(null);
		        	 
		       
		        	 lin.getArticle().setImags_p((new ArrayList<String>( lin.getArticle().getImags_p())));
		          }
      		  
	
			
			  
			//  c.getArticle().setMarkeplace(null);
			//  c.getArticle().setImags_p(new ArrayList<String>(c.getArticle().getImags_p()));
		  }
		  
		 
		}
		
		
		return u;
	
		
	}
	
	
	public Boolean addMarkPlace(Markeplace markplace)
	{
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		try
		{

		bd.CurrentSession.saveOrUpdate(markplace);
		bd.CurrentSession.getTransaction().commit();
		return true;
		}
		catch(Exception e)
		{
			
		return false;	
		}
		finally {
			bd.CurrentSession.close();	
		}
	}
	
	
	public Utilisateur listAchat(Utilisateur us)
	{
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		Utilisateur u=bd.CurrentSession.get(Utilisateur.class,us.getId_U());
	//**	query.setParameter("cod",us.getId_C());
		  System.out.println(u.getListVentAchteur().getClass()+"*******listlist*****************");
		
		if(u!=null)
		{
		 u.setListVentVendeur(null);
		   u.setListVentAchteur(new ArrayList(u.getListVentAchteur()));
		if(u.getMarkplace()!=null)
		{
		   u.getMarkplace().setUser(null);
		u.getMarkplace().setListArticle(null);
		}
	u.setCompte(null);

		  for(Command c:u.getListVentAchteur())
		  {
			  System.out.println("*******listlistcomand*****************");
			
			  c.getAchteur().setListVentVendeur(null);
			  if(c.getAchteur().getMarkplace()!=null)
				{
			  c.getAchteur().getMarkplace().setListArticle(null);
			  c.getAchteur().getMarkplace().setUser(null);
				}
			  c.getAchteur().setCompte(null);
	       
	         
			  
	        
	        
			  c.getVendeur().getMarkplace().setListArticle(null);
			  c.getVendeur().getMarkplace().setUser(null);
			  c.getVendeur().setCompte(null);
			   c.getVendeur().setListVentAchteur(null);
		          c.getVendeur().setListVentVendeur(null);
		          c.setLignsCommand(new ArrayList(c.getLignsCommand()));
		          for(LingeCommand lin:c.getLignsCommand())
		          {
		        	 
		        	  lin.setCommand(null);
		        	 lin.getArticle().getMarkeplace().setListArticle(null);
		        	 lin.getArticle().getMarkeplace().setUser(null);
		        	 
		       
		        	 lin.getArticle().setImags_p((new ArrayList<String>( lin.getArticle().getImags_p())));
		          }
      		  
	
			
			  
			//  c.getArticle().setMarkeplace(null);
			//  c.getArticle().setImags_p(new ArrayList<String>(c.getArticle().getImags_p()));
		  }
		  
		 
		}
		
		
		return u;
	
		}
	
	public String payment(ArrayList<Command>  comnd)
	{
		float pay=0;
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		Utilisateur ach=comnd.get(0).getAchteur();
		Query query=bd.CurrentSession.createQuery("from Compte where cod_post=:codpost and password=:pas");
        System.out.println("*******************"+ach.getCompte().getCod_post());
		query.setParameter("codpost",ach.getCompte().getCod_post());
		query.setParameter("pas",ach.getCompte().getPassword());
		Compte compte = (Compte)query.uniqueResult();
		if(compte!=null)
		{
		for(Command c:comnd)
		{
         pay=c.payTotal();
		}
		if(compte.getMony()>=pay)
		{

			for(Command c:comnd)
			{
		          Query query1=bd.CurrentSession.createQuery("from Compte where cod_post=:codpost and password=:pas");

	          compte.setMony(compte.getMony()-c.payTotal());
	  		//query.setParameter("cod_post",c.getVendeur().getCompte().getCod_post());
	  		//query.setParameter("password",c.getVendeur().getCompte().getPassword());
	          query1.setParameter("codpost",2);
	          System.out.println("ddddd*********************************");
		  		query1.setParameter("pas","123");
	          Compte compte1 = (Compte)query1.uniqueResult();
	  		compte1.setMony(compte1.getMony()+c.payTotal());
	  		bd.CurrentSession.save(compte1);
	  	  System.out.println("ddddd111*********************************");
			}
			bd.CurrentSession.save(compte);
			bd.CurrentSession.getTransaction().commit();
			
		}
		else
		{
			bd.CurrentSession.close();
			return "0";
		}
		}
		else
		{
			bd.CurrentSession.close();
			return "1";
		}
		
		
		
		bd.CurrentSession.close();
		
		return "2";
	}
	
	
	public void updateqtArt(Command c)
	{
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		for(LingeCommand l:c.getLignsCommand())
		{
			l.getArticle().setQuanSt_p(l.getArticle().getQuanSt_p()-l.getQuant());
			l.setCommand(c);
			bd.CurrentSession.update(l.getArticle());
		}
		
	
		bd.CurrentSession.getTransaction().commit();
		bd.CurrentSession.close();
	}
	
	public void updateqtArtR(Command c)
	{
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		for(LingeCommand l:c.getLignsCommand())
		{
			l.getArticle().setQuanSt_p(l.getArticle().getQuanSt_p()+l.getQuant());
			l.setCommand(c);
			bd.CurrentSession.update(l.getArticle());
		}
		
	
		bd.CurrentSession.getTransaction().commit();
		bd.CurrentSession.close();
	}
	
	
	public void changeEtat(String etat)
	{
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		int id=Integer.parseInt(etat);
		Command c=bd.CurrentSession.get(Command.class,id);
		c.setEtat("livre");
		bd.CurrentSession.update(c);
		bd.CurrentSession.getTransaction().commit();
		bd.CurrentSession.close();
	}
	
	public ArrayList<Utilisateur> listVendeur()
	{
		
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		Query query1=bd.CurrentSession.createQuery("from Utilisateur where markplace.etatMarkplace!=2 ");
		//query1.setParameter("type",1);
		List<Utilisateur> listvendeur=(List<Utilisateur>)query1.getResultList();
		 bd.CurrentSession.close();
		return new ArrayList<Utilisateur>(listvendeur);
	}
	
	
	public void changeEtatMrk(Utilisateur us,int type)
	{
		ConfigBD bd=new ConfigBD();bd.connexionBD();
		if(type==0)
		us.getMarkplace().setEtatMarkplace(-1);
		else
		{
			us.setType(1);
			us.getMarkplace().setEtatMarkplace(1);
		}
		  bd.CurrentSession.update(us);
          bd.CurrentSession.update(us.getMarkplace());
          bd.CurrentSession.getTransaction().commit();
          bd.CurrentSession.close();
	}
	
}
