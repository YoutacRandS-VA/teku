/*
 * Copyright Consensys Software Inc., 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.teku.infrastructure.ssz.containers;

import java.util.List;
import java.util.function.BiFunction;
import tech.pegasys.teku.infrastructure.ssz.SszContainer;
import tech.pegasys.teku.infrastructure.ssz.SszData;
import tech.pegasys.teku.infrastructure.ssz.schema.SszSchema;
import tech.pegasys.teku.infrastructure.ssz.schema.impl.AbstractSszContainerSchema;
import tech.pegasys.teku.infrastructure.ssz.tree.TreeNode;

/** Autogenerated by tech.pegasys.teku.ssz.backing.ContainersGenerator */
public abstract class ContainerSchema13<
        C extends SszContainer,
        V0 extends SszData,
        V1 extends SszData,
        V2 extends SszData,
        V3 extends SszData,
        V4 extends SszData,
        V5 extends SszData,
        V6 extends SszData,
        V7 extends SszData,
        V8 extends SszData,
        V9 extends SszData,
        V10 extends SszData,
        V11 extends SszData,
        V12 extends SszData>
    extends AbstractSszContainerSchema<C> {

  public static <
          C extends SszContainer,
          V0 extends SszData,
          V1 extends SszData,
          V2 extends SszData,
          V3 extends SszData,
          V4 extends SszData,
          V5 extends SszData,
          V6 extends SszData,
          V7 extends SszData,
          V8 extends SszData,
          V9 extends SszData,
          V10 extends SszData,
          V11 extends SszData,
          V12 extends SszData>
      ContainerSchema13<C, V0, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> create(
          SszSchema<V0> fieldSchema0,
          SszSchema<V1> fieldSchema1,
          SszSchema<V2> fieldSchema2,
          SszSchema<V3> fieldSchema3,
          SszSchema<V4> fieldSchema4,
          SszSchema<V5> fieldSchema5,
          SszSchema<V6> fieldSchema6,
          SszSchema<V7> fieldSchema7,
          SszSchema<V8> fieldSchema8,
          SszSchema<V9> fieldSchema9,
          SszSchema<V10> fieldSchema10,
          SszSchema<V11> fieldSchema11,
          SszSchema<V12> fieldSchema12,
          BiFunction<
                  ContainerSchema13<C, V0, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12>,
                  TreeNode,
                  C>
              instanceCtor) {
    return new ContainerSchema13<>(
        fieldSchema0,
        fieldSchema1,
        fieldSchema2,
        fieldSchema3,
        fieldSchema4,
        fieldSchema5,
        fieldSchema6,
        fieldSchema7,
        fieldSchema8,
        fieldSchema9,
        fieldSchema10,
        fieldSchema11,
        fieldSchema12) {
      @Override
      public C createFromBackingNode(TreeNode node) {
        return instanceCtor.apply(this, node);
      }
    };
  }

  protected ContainerSchema13(
      SszSchema<V0> fieldSchema0,
      SszSchema<V1> fieldSchema1,
      SszSchema<V2> fieldSchema2,
      SszSchema<V3> fieldSchema3,
      SszSchema<V4> fieldSchema4,
      SszSchema<V5> fieldSchema5,
      SszSchema<V6> fieldSchema6,
      SszSchema<V7> fieldSchema7,
      SszSchema<V8> fieldSchema8,
      SszSchema<V9> fieldSchema9,
      SszSchema<V10> fieldSchema10,
      SszSchema<V11> fieldSchema11,
      SszSchema<V12> fieldSchema12) {

    super(
        List.of(
            fieldSchema0,
            fieldSchema1,
            fieldSchema2,
            fieldSchema3,
            fieldSchema4,
            fieldSchema5,
            fieldSchema6,
            fieldSchema7,
            fieldSchema8,
            fieldSchema9,
            fieldSchema10,
            fieldSchema11,
            fieldSchema12));
  }

  protected ContainerSchema13(
      String containerName,
      NamedSchema<V0> fieldNamedSchema0,
      NamedSchema<V1> fieldNamedSchema1,
      NamedSchema<V2> fieldNamedSchema2,
      NamedSchema<V3> fieldNamedSchema3,
      NamedSchema<V4> fieldNamedSchema4,
      NamedSchema<V5> fieldNamedSchema5,
      NamedSchema<V6> fieldNamedSchema6,
      NamedSchema<V7> fieldNamedSchema7,
      NamedSchema<V8> fieldNamedSchema8,
      NamedSchema<V9> fieldNamedSchema9,
      NamedSchema<V10> fieldNamedSchema10,
      NamedSchema<V11> fieldNamedSchema11,
      NamedSchema<V12> fieldNamedSchema12) {

    super(
        containerName,
        List.of(
            fieldNamedSchema0,
            fieldNamedSchema1,
            fieldNamedSchema2,
            fieldNamedSchema3,
            fieldNamedSchema4,
            fieldNamedSchema5,
            fieldNamedSchema6,
            fieldNamedSchema7,
            fieldNamedSchema8,
            fieldNamedSchema9,
            fieldNamedSchema10,
            fieldNamedSchema11,
            fieldNamedSchema12));
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V0> getFieldSchema0() {
    return (SszSchema<V0>) getChildSchema(0);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V1> getFieldSchema1() {
    return (SszSchema<V1>) getChildSchema(1);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V2> getFieldSchema2() {
    return (SszSchema<V2>) getChildSchema(2);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V3> getFieldSchema3() {
    return (SszSchema<V3>) getChildSchema(3);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V4> getFieldSchema4() {
    return (SszSchema<V4>) getChildSchema(4);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V5> getFieldSchema5() {
    return (SszSchema<V5>) getChildSchema(5);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V6> getFieldSchema6() {
    return (SszSchema<V6>) getChildSchema(6);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V7> getFieldSchema7() {
    return (SszSchema<V7>) getChildSchema(7);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V8> getFieldSchema8() {
    return (SszSchema<V8>) getChildSchema(8);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V9> getFieldSchema9() {
    return (SszSchema<V9>) getChildSchema(9);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V10> getFieldSchema10() {
    return (SszSchema<V10>) getChildSchema(10);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V11> getFieldSchema11() {
    return (SszSchema<V11>) getChildSchema(11);
  }

  @SuppressWarnings("unchecked")
  public SszSchema<V12> getFieldSchema12() {
    return (SszSchema<V12>) getChildSchema(12);
  }
}
