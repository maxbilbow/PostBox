package com.maxbilbow.testtools.dao;

import com.maxbilbow.testtools.domain.DataReceived;
import com.maxbilbow.testtools.domain.GenericDomain;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by bilbowm (Max Bilbow) on 23/03/2016.
 */
public class GenericDao<T extends GenericDomain<? extends Number>>
{
  @Resource
  SessionFactory mSessionFactory;

  protected Class<T> mClassType;

  public GenericDao() {
    Class clazz;
    for(clazz = this.getClass(); !(clazz.getGenericSuperclass() instanceof ParameterizedType); clazz = clazz.getSuperclass()) {
      ;
    }

    this.mClassType = (Class)((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[0];
  }

  protected final String getClassName() {
    return this.mClassType.getSimpleName();
  }

  public Session getSession()
  {
    return mSessionFactory.getCurrentSession();
  }

  public T makePersistent(T aGenericDomain)
  {
    getSession().saveOrUpdate(aGenericDomain);
    return aGenericDomain;
  }

  public List<DataReceived> findAll()
  {
    return getSession().createQuery("from " + getClassName()).list();
  }

  public T makeTransient(T aGenericDomain)
  {
    getSession().delete(aGenericDomain);
    return aGenericDomain;
  }

  public List<T> batchPersist(List<T> aGenericDomains)
  {
    for (T genericDomain : aGenericDomains)
    {
      getSession().saveOrUpdate(genericDomain);
    }
    return aGenericDomains;
  }

  public List<T> batchMakeTransient(List<T> aGenericDomains)
  {
    for (T genericDomain : aGenericDomains)
    {
      getSession().delete(genericDomain);
    }
    return aGenericDomains;
  }

  public <T> T findByPk(final Long aPk)
  {
    return (T) getSession().createQuery("FROM " + getClassName() + " WHERE pk = :pk")
            .setLong("pk",aPk)
            .uniqueResult();
  }
}
