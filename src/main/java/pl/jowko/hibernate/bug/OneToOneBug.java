package pl.jowko.hibernate.bug;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Created by Piotr on 2018-02-19.
 */
public class OneToOneBug {
	
	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;
	
	public static void main(String[] args) {
		OneToOneBug one = new OneToOneBug();
		// Add data first
		//one.addTestData();
		
		// After adding data, comment addTestData method and uncomment findAll
		// After inserting entities, hibernate gets entities from cache? and perform only 1 query, so problem is not visible
		// Also remove "create" value from hibernate.hbm2ddl.auto after data insertion
		one.findAll();
		
		one.close();
	}
	
	private OneToOneBug() {
		entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-bug");
		entityManager = entityManagerFactory.createEntityManager();
	}
	
	private void findAll() {
		// It is important to not run both queries at once(or addData and query)
		// Hibernate probably fetches data from sessions and don't perform additional queries
		
		
		// This gives 7 queries total. 1 to get all Kennels list, then 1 query for each Kennel and Dog.
		// It is not n+1 problem but 2n+1 problem
		// OneToMany bidirectional relations have only n+1 queries with such case
		
		//Query query = entityManager.createQuery("from Kennel");
		
		
		// Because of that, I tried to perform fetch join for such relation
		// It solves n+1 issue for OneToMany relations and gets data in one query
		// For OneToOne bidirectional relations it solves 2n+1 problem
		// But n+1 problem still exists, so now 4 queries were executed
		// Worst thing is, that first select get all needed data
		// And other selects get data for each row with were already fetched
		
		Query query = entityManager.createQuery("from Kennel k join fetch k.dog");
		
		// For ManyToMany relations, fetch join also do not solve n+1 issue
		
		System.out.println(query.getResultList());
	}
	
	private void addTestData() {
		Dog dog = new Dog("Piesel");
		Dog dog2 = new Dog("Second Piesel");
		Dog dog3 = new Dog("Third Piesel");
		
		Kennel kennel = new Kennel("Kennel 1", dog);
		Kennel kennel2 = new Kennel("Kennel 2", dog2);
		Kennel kennel3 = new Kennel("Kennel 3", dog3);
		
		entityManager.getTransaction().begin();
		entityManager.persist(dog);
		entityManager.persist(dog2);
		entityManager.persist(dog3);
		entityManager.persist(kennel);
		entityManager.persist(kennel2);
		entityManager.persist(kennel3);
		entityManager.getTransaction().commit();
	}
	
	private void close() {
		entityManager.close();
		entityManagerFactory.close();
	}
	
}
