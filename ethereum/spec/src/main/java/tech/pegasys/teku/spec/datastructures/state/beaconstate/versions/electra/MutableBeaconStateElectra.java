/*
 * Copyright Consensys Software Inc., 2024
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

package tech.pegasys.teku.spec.datastructures.state.beaconstate.versions.electra;

import java.util.Optional;
import tech.pegasys.teku.infrastructure.ssz.primitive.SszUInt64;
import tech.pegasys.teku.infrastructure.unsigned.UInt64;
import tech.pegasys.teku.spec.datastructures.state.beaconstate.MutableBeaconState;
import tech.pegasys.teku.spec.datastructures.state.beaconstate.common.BeaconStateFields;
import tech.pegasys.teku.spec.datastructures.state.beaconstate.versions.deneb.MutableBeaconStateDeneb;

public interface MutableBeaconStateElectra extends MutableBeaconStateDeneb, BeaconStateElectra {
  static MutableBeaconStateElectra required(final MutableBeaconState state) {
    return state
        .toMutableVersionElectra()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Expected an Electra state but got: " + state.getClass().getSimpleName()));
  }

  @Override
  BeaconStateElectra commitChanges();

  default void setDepositReceiptsStartIndex(final UInt64 depositReceiptsStartIndex) {
    final int fieldIndex =
        getSchema().getFieldIndex(BeaconStateFields.DEPOSIT_RECEIPTS_START_INDEX);
    set(fieldIndex, SszUInt64.of(depositReceiptsStartIndex));
  }

  @Override
  default Optional<MutableBeaconStateElectra> toMutableVersionElectra() {
    return Optional.of(this);
  }
}
