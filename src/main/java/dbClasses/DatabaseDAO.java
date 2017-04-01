package dbClasses;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("databaseDAO")
public class DatabaseDAO {
    @Autowired
    private SessionFactory sessionFactory;

    private String isNameExists(String name){
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "from " + Record.class.getName() + " where name = :name";
            Query q = session.createQuery(hql).setParameter("name", name);
            List<Record> list = q.getResultList();
            if (list.size()>0){
                int i=1;
                while(i<Integer.MAX_VALUE){
                    hql = "from " + Record.class.getName() + " where name = :name";
                    q = session.createQuery(hql).setParameter("name", name+" ("+i+")");
                    list = q.getResultList();
                    if (list.size()==0) return name+" ("+i+")";
                }
            }
            return name;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return name;
        } finally {
            session.close();
        }
    }

    public void createRecord(Record record){
        String newName = isNameExists(record.getName());
        if (record.getName()!=newName)
        record.setName(isNameExists(record.getName()));

        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(record);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<Record> readAllRecords(){
        Session session = sessionFactory.openSession();
        List<Record> list = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Record");
            list = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            return list;
        }
    }

    public List<Record> readRecordsWithCondition(String condition){
        Session session = sessionFactory.openSession();
        List<Record> list = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session
                    .createQuery("FROM Record where upper(name) like :name")
                    .setParameter("name", "%" + condition.toUpperCase() + "%");
            list = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            return list;
        }
    }

    public void updateRecord(Record record){
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(record);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void deleteRecord(Integer id){
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "delete " + Record.class.getName() + " where id = :id";
            Query q = session.createQuery(hql).setParameter("id", id);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
