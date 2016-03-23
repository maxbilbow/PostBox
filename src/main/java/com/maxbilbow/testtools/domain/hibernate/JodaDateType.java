package com.maxbilbow.testtools.domain.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.*;

/**
 * JodaDateType.
 * 
 * @author Alistair Allen, copied from a similiar class by Alison Verkroost
 * @version 1.0
 */
public class JodaDateType implements UserType
{
  private Logger mLogger = LoggerFactory.getLogger(getClass());

  /**
   * @see org.hibernate.usertype.UserType
   * #isMutable()
   * {@inheritDoc}
   */
  public boolean isMutable()
  {
    return true;
  }

  /**
   * @see org.hibernate.usertype.UserType
   * #nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int, org.hibernate.engine.spi.SessionImplementor)
   * {@inheritDoc}
   */
  public void nullSafeSet(PreparedStatement aSt,
                          Object aValue,
                          int aIndex,
                          SessionImplementor aSession) throws HibernateException, SQLException
  {
    nullSafeSet(aSt, aValue, aIndex);
  }

  /**
   * @see org.hibernate.usertype.UserType
   * #nullSafeGet(java.sql.ResultSet, java.lang.String[], org.hibernate.engine.spi.SessionImplementor, java.lang.Object)
   * {@inheritDoc}
   */
  public Object nullSafeGet(ResultSet aResultSet,
                            String[] aNames,
                            SessionImplementor aSession,
                            Object aOwner) throws HibernateException, SQLException
  {
    return nullSafeGet(aResultSet, aNames[0]);
  }

  /**
   * Implementation taken mainly from org.hibernate.type.NullableType.
   *
   * @param aResultSet The resultset containing db data.
   * @param aName The name of the required value.
   * @return The retrieved value.
   * @throws HibernateException If a HibernateException occurs.
   * @throws SQLException If a SQLException occurs.
   */
  private Object nullSafeGet(ResultSet aResultSet, String aName) throws HibernateException, SQLException
  {
    try
    {
      Object value = get(aResultSet, aName);
      if (value == null || aResultSet.wasNull())
      {
        return null;
      }

      return value;
    }
    catch (RuntimeException re)
    {
      mLogger.info("could not read column value from result set: "
                   + aName + "; ", re);
      throw re;
    }
    catch (SQLException se)
    {
      mLogger.info("could not read column value from result set: "
                   + aName + "; ", se);
      throw se;
    }
  }

  /**
   * Implementation mainly taken from org.hibernate.type.CalendarType.
   *
   * @param aResultSet The resultset containing db data.
   * @param aName The name of the required value.
   * @return The retrieved value.
   * @throws HibernateException If a HibernateException occurs.
   * @throws SQLException If a SQLException occurs.
   */
  protected Object get(ResultSet aResultSet, String aName) throws HibernateException, SQLException
  {
    Object value = aResultSet.getObject(aName);
    if (value == null)
    {
      return null;
    }

    Timestamp rawValue = aResultSet.getTimestamp(aName);
    return new DateTime(rawValue.getTime());
  }

  /**
  /**
   * Implementation taken mainly from org.hibernate.type.NullableType.
   *
   * @param aStmt A JDBC prepared statement
   * @param aValue The object to write
   * @param aIndex Statement parameter index
   * @throws HibernateException If a HibernateException occurs.
   * @throws SQLException If a SQLException occurs.
   *
   * @see org.hibernate.usertype.UserType#
   * nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
   */
  public void nullSafeSet(PreparedStatement aStmt, Object aValue, int aIndex) throws HibernateException, SQLException
  {
    try
    {
      if (aValue == null)
      {
        aStmt.setNull(aIndex, sqlType());
      }
      else
      {
        set(aStmt, aValue, aIndex);
      }
    }
    catch (RuntimeException re)
    {
      mLogger.info("could not bind value '" + nullSafeToString(aValue) + "' to parameter: " + aIndex, re);
      throw re;
    }
    catch (SQLException se)
    {
      mLogger.info("could not bind value '" + nullSafeToString(aValue) + "' to parameter: " + aIndex, se);
      throw se;
    }
  }

  /**
   * Implementation mainly taken from org.hibernate.type.CalendarType.
   *
   * @param aStatement A JDBC prepared statement
   * @param aValue The object to write
   * @param aIndex Statement parameter index
   * @throws HibernateException If a HibernateException occurs.
   * @throws SQLException If a SQLException occurs.
   */
  protected void set(PreparedStatement aStatement, Object aValue, int aIndex) throws HibernateException, SQLException
  {
    aStatement.setTimestamp(aIndex, new Timestamp(((DateTime) aValue).getMillis()));
  }

  /**
   * Implementation mainly taken from org.hibernate.type.NullableType. A
   * null-safe version of {@link #toString(Object)}. Specifically we are
   * worried about null safeness in regards to the incoming value parameter, not
   * the return.
   *
   * @param aValue The value to convert to a string representation; may be null.
   * @return The string representation; may be null.
   * @throws HibernateException Thrown by {@link #toString(Object)}, which this
   *           calls.
   */
  private String nullSafeToString(Object aValue) throws HibernateException
  {
    if (aValue != null)
    {
      return toString(aValue);
    }
    return null;
  }

  /**
   * @param aValue value of the correct type.
   * @return A string representation of the given value.
   * @throws HibernateException If a HibernateException occurs.
   */
  private String toString(Object aValue) throws HibernateException
  {
    return StandardBasicTypes.TIMESTAMP.toString();
  }

  /**
   * @return Types.DATE
   */
  private int sqlType()
  {
    return Types.TIMESTAMP;
  }

  /**
   * @see org.hibernate.usertype.UserType
   * #returnedClass()
   * {@inheritDoc}
   */
  public Class<?> returnedClass()
  {
    return DateTime.class;
  }

  /**
   * @see org.hibernate.usertype.UserType
   * #equals(java.lang.Object, java.lang.Object)
   * {@inheritDoc}
   */
  public boolean equals(Object aX, Object aY) throws HibernateException
  {
    if (aX == null)
    {
      return aY == null;
    }
    return ((DateTime) aX).equals(aY);
  }

  /**
   * @see org.hibernate.usertype.UserType
   * #hashCode(java.lang.Object)
   * {@inheritDoc}
   */
  public int hashCode(Object aObject) throws HibernateException
  {
    if (aObject == null)
    {
      return -1;
    }
    return ((DateTime) aObject).hashCode();
  }

  /**
   * @see org.hibernate.usertype.UserType
   * #sqlTypes()
   * {@inheritDoc}
   */
  public int[] sqlTypes()
  {
    return new int[] {sqlType()};
  }

  /**
   * @see org.hibernate.usertype.UserType
   * #assemble(java.io.Serializable, java.lang.Object)
   * {@inheritDoc}
   */
  public Object assemble(Serializable aCached, Object aOwner) throws HibernateException
  {
    return aCached;
  }

  /**
   * @see org.hibernate.usertype.UserType
   * #deepCopy(java.lang.Object)
   * {@inheritDoc}
   */
  public Object deepCopy(Object aValue) throws HibernateException
  {
    if (aValue != null)
    {
      return new DateTime(((DateTime) aValue).getMillis());
    }
    return null;
  }

  /**
   * @see org.hibernate.usertype.UserType
   * #disassemble(java.lang.Object)
   * {@inheritDoc}
   */
  public Serializable disassemble(Object aValue) throws HibernateException
  {
    return (Serializable) aValue;
  }

  /**
   * @see org.hibernate.usertype.UserType
   * #replace(java.lang.Object, java.lang.Object, java.lang.Object)
   * {@inheritDoc}
   */
  public Object replace(Object aOriginal, Object aTarget, Object aOwner) throws HibernateException
  {
    return aOriginal;
  }
}