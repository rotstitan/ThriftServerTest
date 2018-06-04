/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.vngcorp.log.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2018-05-29")
public class LogEntry implements org.apache.thrift.TBase<LogEntry, LogEntry._Fields>, java.io.Serializable, Cloneable, Comparable<LogEntry> {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("LogEntry");

    private static final org.apache.thrift.protocol.TField GAME_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("gameId", org.apache.thrift.protocol.TType.STRING, (short)1);
    private static final org.apache.thrift.protocol.TField SERVICE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("serviceId", org.apache.thrift.protocol.TType.STRING, (short)2);
    private static final org.apache.thrift.protocol.TField MESSAGE_FIELD_DESC = new org.apache.thrift.protocol.TField("message", org.apache.thrift.protocol.TType.STRING, (short)3);

    private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
    static {
      schemes.put(StandardScheme.class, new LogEntryStandardSchemeFactory());
      schemes.put(TupleScheme.class, new LogEntryTupleSchemeFactory());
    }
    public String gameId; // required
    public String serviceId; // required
    public String message; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    GAME_ID((short)1, "gameId"),
    SERVICE_ID((short)2, "serviceId"),
    MESSAGE((short)3, "message");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // GAME_ID
          return GAME_ID;
        case 2: // SERVICE_ID
          return SERVICE_ID;
        case 3: // MESSAGE
          return MESSAGE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.GAME_ID, new org.apache.thrift.meta_data.FieldMetaData("gameId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.SERVICE_ID, new org.apache.thrift.meta_data.FieldMetaData("serviceId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.MESSAGE, new org.apache.thrift.meta_data.FieldMetaData("message", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(LogEntry.class, metaDataMap);
  }

  public LogEntry() {
  }

  public LogEntry(
    String gameId,
    String serviceId,
    String message)
  {
    this();
    this.gameId = gameId;
    this.serviceId = serviceId;
    this.message = message;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public LogEntry(LogEntry other) {
    if (other.isSetGameId()) {
      this.gameId = other.gameId;
    }
    if (other.isSetServiceId()) {
      this.serviceId = other.serviceId;
    }
    if (other.isSetMessage()) {
      this.message = other.message;
    }
  }

  public LogEntry deepCopy() {
    return new LogEntry(this);
  }

  @Override
  public void clear() {
    this.gameId = null;
    this.serviceId = null;
    this.message = null;
  }

  public String getGameId() {
    return this.gameId;
  }

  public LogEntry setGameId(String gameId) {
    this.gameId = gameId;
    return this;
  }

  public void unsetGameId() {
    this.gameId = null;
  }

  /** Returns true if field gameId is set (has been assigned a value) and false otherwise */
  public boolean isSetGameId() {
    return this.gameId != null;
  }

  public void setGameIdIsSet(boolean value) {
    if (!value) {
      this.gameId = null;
    }
  }

  public String getServiceId() {
    return this.serviceId;
  }

  public LogEntry setServiceId(String serviceId) {
    this.serviceId = serviceId;
    return this;
  }

  public void unsetServiceId() {
    this.serviceId = null;
  }

  /** Returns true if field serviceId is set (has been assigned a value) and false otherwise */
  public boolean isSetServiceId() {
    return this.serviceId != null;
  }

  public void setServiceIdIsSet(boolean value) {
    if (!value) {
      this.serviceId = null;
    }
  }

  public String getMessage() {
    return this.message;
  }

  public LogEntry setMessage(String message) {
    this.message = message;
    return this;
  }

  public void unsetMessage() {
    this.message = null;
  }

  /** Returns true if field message is set (has been assigned a value) and false otherwise */
  public boolean isSetMessage() {
    return this.message != null;
  }

  public void setMessageIsSet(boolean value) {
    if (!value) {
      this.message = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case GAME_ID:
      if (value == null) {
        unsetGameId();
      } else {
        setGameId((String)value);
      }
      break;

    case SERVICE_ID:
      if (value == null) {
        unsetServiceId();
      } else {
        setServiceId((String)value);
      }
      break;

    case MESSAGE:
      if (value == null) {
        unsetMessage();
      } else {
        setMessage((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case GAME_ID:
      return getGameId();

    case SERVICE_ID:
      return getServiceId();

    case MESSAGE:
      return getMessage();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case GAME_ID:
      return isSetGameId();
    case SERVICE_ID:
      return isSetServiceId();
    case MESSAGE:
      return isSetMessage();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof LogEntry)
      return this.equals((LogEntry)that);
    return false;
  }

  public boolean equals(LogEntry that) {
    if (that == null)
      return false;

    boolean this_present_gameId = true && this.isSetGameId();
    boolean that_present_gameId = true && that.isSetGameId();
    if (this_present_gameId || that_present_gameId) {
      if (!(this_present_gameId && that_present_gameId))
        return false;
      if (!this.gameId.equals(that.gameId))
        return false;
    }

    boolean this_present_serviceId = true && this.isSetServiceId();
    boolean that_present_serviceId = true && that.isSetServiceId();
    if (this_present_serviceId || that_present_serviceId) {
      if (!(this_present_serviceId && that_present_serviceId))
        return false;
      if (!this.serviceId.equals(that.serviceId))
        return false;
    }

    boolean this_present_message = true && this.isSetMessage();
    boolean that_present_message = true && that.isSetMessage();
    if (this_present_message || that_present_message) {
      if (!(this_present_message && that_present_message))
        return false;
      if (!this.message.equals(that.message))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_gameId = true && (isSetGameId());
    list.add(present_gameId);
    if (present_gameId)
      list.add(gameId);

    boolean present_serviceId = true && (isSetServiceId());
    list.add(present_serviceId);
    if (present_serviceId)
      list.add(serviceId);

    boolean present_message = true && (isSetMessage());
    list.add(present_message);
    if (present_message)
      list.add(message);

    return list.hashCode();
  }

  @Override
  public int compareTo(LogEntry other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetGameId()).compareTo(other.isSetGameId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGameId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.gameId, other.gameId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetServiceId()).compareTo(other.isSetServiceId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetServiceId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.serviceId, other.serviceId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMessage()).compareTo(other.isSetMessage());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMessage()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.message, other.message);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("LogEntry(");
    boolean first = true;

    sb.append("gameId:");
    if (this.gameId == null) {
      sb.append("null");
    } else {
      sb.append(this.gameId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("serviceId:");
    if (this.serviceId == null) {
      sb.append("null");
    } else {
      sb.append(this.serviceId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("message:");
    if (this.message == null) {
      sb.append("null");
    } else {
      sb.append(this.message);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (gameId == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'gameId' was not present! Struct: " + toString());
    }
    if (serviceId == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'serviceId' was not present! Struct: " + toString());
    }
    if (message == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'message' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class LogEntryStandardSchemeFactory implements SchemeFactory {
    public LogEntryStandardScheme getScheme() {
      return new LogEntryStandardScheme();
    }
  }

  private static class LogEntryStandardScheme extends StandardScheme<LogEntry> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, LogEntry struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // GAME_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.gameId = iprot.readString();
              struct.setGameIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SERVICE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.serviceId = iprot.readString();
              struct.setServiceIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // MESSAGE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.message = iprot.readString();
              struct.setMessageIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, LogEntry struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.gameId != null) {
        oprot.writeFieldBegin(GAME_ID_FIELD_DESC);
        oprot.writeString(struct.gameId);
        oprot.writeFieldEnd();
      }
      if (struct.serviceId != null) {
        oprot.writeFieldBegin(SERVICE_ID_FIELD_DESC);
        oprot.writeString(struct.serviceId);
        oprot.writeFieldEnd();
      }
      if (struct.message != null) {
        oprot.writeFieldBegin(MESSAGE_FIELD_DESC);
        oprot.writeString(struct.message);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class LogEntryTupleSchemeFactory implements SchemeFactory {
    public LogEntryTupleScheme getScheme() {
      return new LogEntryTupleScheme();
    }
  }

  private static class LogEntryTupleScheme extends TupleScheme<LogEntry> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, LogEntry struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.gameId);
      oprot.writeString(struct.serviceId);
      oprot.writeString(struct.message);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, LogEntry struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.gameId = iprot.readString();
      struct.setGameIdIsSet(true);
      struct.serviceId = iprot.readString();
      struct.setServiceIdIsSet(true);
      struct.message = iprot.readString();
      struct.setMessageIsSet(true);
    }
  }

}

