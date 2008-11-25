// Copyright 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gwtorm.schema.sql;

import com.google.gwtorm.jdbc.gen.CodeGenSupport;
import com.google.gwtorm.schema.ColumnModel;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.sql.Types;

public class SqlCharTypeInfo extends SqlTypeInfo {
  @Override
  public String getSqlType(final ColumnModel column) {
    final StringBuilder r = new StringBuilder();
    r.append("CHAR(1)");
    r.append(" DEFAULT ' '");
    r.append(" NOT NULL");
    return r.toString();
  }

  @Override
  protected String getJavaSqlTypeAlias() {
    return "String";
  }

  @Override
  protected int getSqlTypeConstant() {
    return Types.CHAR;
  }

  @Override
  public void generatePreparedStatementSet(final CodeGenSupport cgs) {
    cgs.pushSqlHandle();
    cgs.pushColumnIndex();
    cgs.pushFieldValue();
    cgs.mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type
        .getInternalName(Character.class), "toString", Type
        .getMethodDescriptor(Type.getType(String.class),
            new Type[] {Type.CHAR_TYPE}));
    cgs.invokePreparedStatementSet(getJavaSqlTypeAlias());
  }

  @Override
  public void generateResultSetGet(final CodeGenSupport cgs) {
    cgs.fieldSetBegin();
    cgs.pushSqlHandle();
    cgs.pushColumnIndex();
    cgs.invokeResultSetGet(getJavaSqlTypeAlias());
    cgs.push(0);
    cgs.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type
        .getInternalName(String.class), "charAt", Type.getMethodDescriptor(
        Type.CHAR_TYPE, new Type[] {Type.INT_TYPE}));
    cgs.fieldSetEnd();
  }
}