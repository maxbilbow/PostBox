package com.maxbilbow.testtools.dao;

import com.maxbilbow.testtools.domain.GenericDomain;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by bilbowm (Max Bilbow) on 23/03/2016.
 */
public class GenericDao<T extends GenericDomain<? extends Number>>
{
  @Resource
  SessionFactory mSessionFactory;

  public Session getSession()
  {
    return mSessionFactory.getCurrentSession();
  }

  public T makePersistent(T aGenericDomain)
  {
    getSession().saveOrUpdate(aGenericDomain);
    return aGenericDomain;
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
}
