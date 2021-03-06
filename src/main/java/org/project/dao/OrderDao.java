package org.project.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.project.entity.Customer;
import org.project.entity.Order;
import org.project.entity.Store;
import org.project.utils.HibernateUtil;

import java.util.Date;
import java.util.List;

public class OrderDao {
    private static final SessionFactory factory = HibernateUtil.getSessionFactory();

    //region [CRUD]
    // Method to CREATE an order in the database
    public Integer addOrder(Customer customer, Store store) {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer orderID = null;
        try {
            tx = session.beginTransaction();
            Order order = new Order(customer, store);
            orderID = (Integer) session.save(order);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return orderID;
    }

    // Method to READ all the orders
    public List<?> listOrders() {
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Order").list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to UPDATE name for an order
    public void updateOrder(Integer OrderID, Integer status){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Order order = session.get(Order.class, OrderID);
            order.setStatus(status);
            session.update(order);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    public void updateOrder(Integer OrderID, Integer status, Date shippedDate) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Order order = session.get(Order.class, OrderID);
            order.setStatus(status);
            order.setShippedDate(shippedDate);
            session.update(order);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Method to DELETE an order from the records
    public void deleteOrder(Integer OrderID) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Order order = session.get(Order.class, OrderID);
            session.delete(order);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    // endregion

    public Order getOrderById(Integer orderId){
        try (Session session = factory.openSession()) {
            return session.get(Order.class, orderId);
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    // region [Orders]
    // All orders
    public List<?> getOrdersByCustomer(Integer customerId){
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Order WHERE customer.id = "+customerId+"").list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
    // status = 0 -> pending
    public List<?> getOrdersPendingByCustomer(Integer customerId){
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Order WHERE customer.id = "+customerId+" AND status = 0").list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
    // status = 1 -> Processing
    public List<?> getOrdersProcessingByCustomer(Integer customerId){
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Order WHERE customer.id = "+customerId+" AND status = 1").list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
    // status = 2 -> Completed
    public List<?> getOrdersCompletedByCustomer(Integer customerId){
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Order WHERE customer.id = "+customerId+" AND status = 2").list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
    // status = 3 -> Canceled
    public List<?> getOrdersCanceledByCustomer(Integer customerId){
        try (Session session = factory.openSession()) {
            return session.createQuery("FROM Order WHERE customer.id = "+customerId+" AND status = 3").list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
    // endregion

    public int countOrderByStore(Integer storeId){
        try (Session session = factory.openSession()) {
            Query<?> query =
                    session.createQuery("select count(*) FROM Order WHERE  store.id = "+ storeId +" and status <3");
            return  ((Long) query.uniqueResult()).intValue();
        } catch (HibernateException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<?> getOrderByStoreAndStatus(Integer storeId, Integer status){
        try (Session session = factory.openSession()) {
            Query<?> query =
                    session.createQuery("FROM Order WHERE  store.id = "+ storeId +" AND status ="+ status +"");
            return  query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int count() {
        try (Session session = factory.openSession()) {
            Query<?> query = session.createQuery("SELECT COUNT(*) FROM Order ");
            return ((Long) query.uniqueResult()).intValue();
        }
        catch (HibernateException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
