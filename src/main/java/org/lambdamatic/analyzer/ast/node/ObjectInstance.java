/*******************************************************************************
 * Copyright (c) 2015 Red Hat. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Red Hat - Initial Contribution
 *******************************************************************************/

package org.lambdamatic.analyzer.ast.node;

/**
 * {@link Expression} holding an instance of an Object.
 * 
 * @author Xavier Coulon
 *
 */
public class ObjectInstance extends Expression {

  private final Object value;

  /**
   * Full constructor.
   * <p>
   * Note: the synthetic {@code id} is generated and the inversion flag is set to {@code false}.
   * </p>
   * 
   * @param value the literal value
   */
  public ObjectInstance(final Object value) {
    this(generateId(), value, false);
  }

  /**
   * Full constructor with given id.
   * 
   * @param id the synthetic id of this {@link Expression}.
   * @param value the literal value
   * @param inverted the inversion flag of this {@link Expression}.
   */
  public ObjectInstance(final int id, final Object value, final boolean inverted) {
    super(id, inverted);
    this.value = value;
  }

  @Override
  public Object getValue() {
    return this.value;
  }

  @Override
  public ExpressionType getType() {
    return ExpressionType.OBJECT_INSTANCE;
  }

  @Override
  public Class<?> getJavaType() {
    return this.value != null ? this.value.getClass() : Void.class;
  }

  @Override
  public boolean canBeInverted() {
    return false;
  }

  @Override
  public Expression duplicate(int id) {
    return new ObjectInstance(id, this.value, isInverted());
  }

  @Override
  public String toString() {
    return this.value != null ? this.value.toString() : "null";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ObjectInstance other = (ObjectInstance) obj;
    if (this.value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!this.value.equals(other.value)) {
      return false;
    }
    return true;
  }

}
